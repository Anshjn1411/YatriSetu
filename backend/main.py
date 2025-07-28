from fastapi import FastAPI, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field, validator
from typing import Optional, Dict, Any
from firebase_functions import https_fn
from starlette.testclient import TestClient
import google.generativeai as genai
import asyncio
import logging
from datetime import datetime

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Travel Recommendation API",
    description="Get comprehensive travel recommendations for any city",
    version="2.0.0"
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Configure Gemini AI
GENAI_API_KEY = "YOUR_GEMINI_API_KEY"
genai.configure(api_key=GENAI_API_KEY)
model = genai.GenerativeModel("models/gemini-1.5-flash")

# ----------- Request/Response Models -----------

class TravelRequest(BaseModel):
    location: str = Field(..., min_length=2, max_length=100, description="City or destination name")
    days: Optional[int] = Field(3, ge=1, le=14, description="Number of days for the trip")
    budget: Optional[str] = Field("medium", description="Budget preference: low, medium, high")
    interests: Optional[str] = Field(None, description="Special interests or preferences")

    @validator('location')
    def validate_location(cls, v):
        return v.strip().title()

    @validator('budget')
    def validate_budget(cls, v):
        if v.lower() not in ['low', 'medium', 'high']:
            return 'medium'
        return v.lower()

class TravelResponse(BaseModel):
    success: bool
    location: str
    days: int
    data: Dict[str, Any]
    error: Optional[str] = None

class SimpleResponse(BaseModel):
    success: bool
    response: str
    error: Optional[str] = None

# ----------- Utility Functions -----------

def create_optimized_prompt(category: str, location: str, days: int = 3, budget: str = "medium") -> str:
    """Create optimized prompts for different travel categories"""

    budget_context = {
        "low": "budget-friendly, affordable",
        "medium": "mid-range, good value",
        "high": "luxury, premium"
    }

    prompts = {
        "itinerary": f"""
        Create a detailed {days}-day travel itinerary for {location}.
        Budget: {budget_context[budget]}

        Format as:
        **Day 1:**
        - Morning: [Activity] (Time, Cost estimate)
        - Afternoon: [Activity] (Time, Cost estimate)
        - Evening: [Activity] (Time, Cost estimate)

        Include rest periods and travel time between locations.
        """,

        "attractions": f"""
        List the top 10 must-visit attractions in {location}.
        Budget: {budget_context[budget]}

        For each attraction, provide:
        - Name and brief description
        - Best time to visit
        - Entry fee (if any)
        - How to reach from city center
        - Time needed for visit
        """,

        "food": f"""
        Recommend the best food experiences in {location}.
        Budget: {budget_context[budget]}

        Include:
        - 5 must-try local dishes
        - 3 best restaurants for each budget category
        - Popular street food areas
        - Food markets and timings
        - Average cost per meal
        """,

        "accommodation": f"""
        Suggest accommodation options in {location} for {days} days.
        Budget focus: {budget_context[budget]}

        Provide 3 options each for:
        - Budget stays (₹500-1500/night)
        - Mid-range hotels (₹1500-4000/night)
        - Luxury options (₹4000+/night)

        Include location, amenities, and booking tips.
        """,

        "transport": f"""
        Provide comprehensive transport guide for {location}.

        Cover:
        - How to reach {location} (nearest airport, railway station)
        - Local transport options (metro, bus, auto, cab)
        - Daily transport costs
        - Best transport apps to use
        - Walking vs transport recommendations
        """,

        "shopping": f"""
        Guide to shopping in {location}.
        Budget: {budget_context[budget]}

        Include:
        - Popular markets and shopping areas
        - Best items to buy as souvenirs
        - Local specialties and handicrafts
        - Bargaining tips
        - Market timings and days
        """,

        "culture": f"""
        Cultural experiences and activities in {location}.

        Suggest:
        - Cultural events and festivals
        - Museums and heritage sites
        - Local traditions to experience
        - Photography spots
        - Evening entertainment options
        - Unique local experiences
        """
    }

    return prompts.get(category, f"Provide information about {category} in {location}")

async def generate_ai_content(prompt: str) -> str:
    """Generate content using Gemini AI with error handling"""
    try:
        response = model.generate_content(prompt)
        return response.text.strip()
    except Exception as e:
        logger.error(f"Gemini AI error: {str(e)}")
        return f"Unable to generate content for this section. Please try again later."

async def generate_multiple_contents(prompts: Dict[str, str]) -> Dict[str, str]:
    """Generate multiple AI contents concurrently"""
    tasks = {key: generate_ai_content(prompt) for key, prompt in prompts.items()}
    results = {}

    for key, task in tasks.items():
        try:
            results[key] = await task
        except Exception as e:
            logger.error(f"Error generating {key}: {str(e)}")
            results[key] = f"Content unavailable for {key}"

    return results

# ----------- API Routes -----------

@app.get("/", response_model=dict)
def root():
    """Health check endpoint"""
    return {
        "message": "Travel Recommendation API is running!",
        "version": "2.0.0",
        "status": "healthy",
        "timestamp": datetime.now().isoformat()
    }

@app.post("/plan-trip", response_model=SimpleResponse)
async def plan_trip(request: TravelRequest):
    """Plan a trip based on user preferences (same as travel-guide for now)"""
    try:
        # You can extend this later with custom logic
        prompt = f"Plan a trip to {request.location} for {request.days} days on a {request.budget} budget."
        response = await generate_ai_content(prompt)

        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error in plan trip: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))

@app.get("/stay-options", response_model=SimpleResponse)
async def get_stay_options(location: str):
    """Get stay/accommodation options"""
    try:
        prompt = create_optimized_prompt("accommodation", location, 3, "medium")
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting stay options: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))

@app.get("/local-conveyance", response_model=SimpleResponse)
async def get_local_conveyance(location: str):
    """Get local transport/conveyance options"""
    try:
        prompt = create_optimized_prompt("transport", location, 3, "medium")
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting local conveyance: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))


@app.get("/nearby-attractions", response_model=SimpleResponse)
async def get_nearby_attractions(location: str):
    """Get nearby attractions"""
    try:
        prompt = create_optimized_prompt("attractions", location, 3, "medium")
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting nearby attractions: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))



@app.get("/markets", response_model=SimpleResponse)
async def get_markets(location: str):
    """Get famous markets/shopping areas"""
    try:
        prompt = create_optimized_prompt("shopping", location, 3, "medium")
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting markets: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))



@app.get("/food-restaurants", response_model=SimpleResponse)
async def get_food_restaurants(location: str):
    """Get recommended food and restaurants"""
    try:
        prompt = create_optimized_prompt("food", location, 3, "medium")
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting food/restaurants: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))




@app.get("/things-to-do", response_model=SimpleResponse)
async def get_things_to_do(location: str):
    """Get interesting activities and things to do"""
    try:
        prompt = f"""
        What are the top fun, cultural, and adventurous things to do in {location}?
        Include day-wise or category-wise breakdown.
        """
        response = await generate_ai_content(prompt)
        return SimpleResponse(success=True, response=response)
    except Exception as e:
        logger.error(f"Error getting things to do: {str(e)}")
        return SimpleResponse(success=False, response="", error=str(e))



@app.post("/travel-guide", response_model=TravelResponse)
async def get_complete_travel_guide(request: TravelRequest):
    """Get comprehensive travel guide for a location"""
    try:
        location = request.location
        days = request.days
        budget = request.budget

        # Create all prompts
        categories = ["itinerary", "attractions", "food", "accommodation", "transport", "shopping", "culture"]
        prompts = {
            category: create_optimized_prompt(category, location, days, budget)
            for category in categories
        }

        # Generate all content
        travel_data = await generate_multiple_contents(prompts)

        # Add metadata
        travel_data["metadata"] = {
            "location": location,
            "days": days,
            "budget": budget,
            "generated_at": datetime.now().isoformat(),
            "interests": request.interests
        }

        return TravelResponse(
            success=True,
            location=location,
            days=days,
            data=travel_data
        )

    except Exception as e:
        logger.error(f"Error in travel guide generation: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/quick-info/{category}", response_model=SimpleResponse)
async def get_quick_info(category: str, location: str, days: int = 3, budget: str = "medium"):
    """Get quick information for a specific category"""
    try:
        valid_categories = ["itinerary", "attractions", "food", "accommodation", "transport", "shopping", "culture"]

        if category not in valid_categories:
            raise HTTPException(status_code=400, detail=f"Invalid category. Use: {', '.join(valid_categories)}")

        prompt = create_optimized_prompt(category, location, days, budget)
        response = await generate_ai_content(prompt)

        return SimpleResponse(
            success=True,
            response=response
        )

    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error in quick info: {str(e)}")
        return SimpleResponse(
            success=False,
            response="",
            error=str(e)
        )

@app.get("/destinations/popular", response_model=SimpleResponse)
async def get_popular_destinations():
    print("Fetching popular destinations in ..." f"(SimpleResponse)")
    """Get list of popular travel destinations in India"""
    try:
        prompt = """
        List the top 20 popular travel destinations in India with brief descriptions.

        Format each as:
        **City Name, State**
        - Best for: [Type of travel - heritage/adventure/beach/hill station]
        - Best time: [Months]
        - Key attractions: [2-3 main attractions]

        Cover diverse destinations including metros, hill stations, beaches, heritage sites.
        """

        response = await generate_ai_content(prompt)
        print(f"Fetching popular destinations in {response}")

        return SimpleResponse(
            success=True,
            response=response
        )

    except Exception as e:
        logger.error(f"Error getting popular destinations: {str(e)}")
        print(f"Fetching popular destinations in {e}")
        return SimpleResponse(
            success=False,
            response="",
            error=str(e)
        )

@app.get("/weather/{location}", response_model=SimpleResponse)
async def get_weather_info(location: str):
    """Get weather and best time to visit information"""
    try:
        prompt = f"""
        Provide weather information and best time to visit {location}.

        Include:
        - Current season and weather
        - Best months to visit and why
        - Weather to avoid and reasons
        - What to pack for each season
        - Seasonal festivals or events
        """

        response = await generate_ai_content(prompt)

        return SimpleResponse(
            success=True,
            response=response
        )

    except Exception as e:
        logger.error(f"Error getting weather info: {str(e)}")
        return SimpleResponse(
            success=False,
            response="",
            error=str(e)
        )

@app.get("/budget-estimate/{location}", response_model=SimpleResponse)
async def get_budget_estimate(location: str, days: int = 3, travelers: int = 1):
    """Get budget estimation for the trip"""
    try:
        prompt = f"""
        Create a detailed budget estimate for {travelers} person(s) visiting {location} for {days} days.

        Break down costs for:
        - Accommodation (budget/mid-range/luxury per night)
        - Food (meals per day)
        - Local transport (daily)
        - Attractions and activities
        - Shopping and miscellaneous
        - Total estimated cost for {days} days

        Provide costs in INR and mention cost-saving tips.
        """

        response = await generate_ai_content(prompt)

        return SimpleResponse(
            success=True,
            response=response
        )

    except Exception as e:
        logger.error(f"Error getting budget estimate: {str(e)}")
        return SimpleResponse(
            success=False,
            response="",
            error=str(e)
        )

# ----------- Error Handlers -----------

@app.exception_handler(404)
async def not_found_handler(request: Request, exc):
    return {"error": "Endpoint not found", "status": 404}

@app.exception_handler(500)
async def internal_error_handler(request: Request, exc):
    return {"error": "Internal server error", "status": 500}

# ----------- Firebase Functions Integration -----------

client = TestClient(app)

@https_fn.on_request()
def firebase_api(request: https_fn.Request):
    """Firebase Functions bridge"""
    try:
        method = request.method
        path = request.full_path if request.query_string else request.path
        headers = dict(request.headers)
        data = request.get_data()

        response = client.request(
            method=method,
            url=path,
            headers=headers,
            data=data
        )

        return (response.content, response.status_code, response.headers.items())

    except Exception as e:
        logger.error(f"Firebase bridge error: {str(e)}")
        return ({"error": "Service temporarily unavailable"}, 500, [])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)