from flask import Flask, request, jsonify
import google.generativeai as genai
import requests
import json
from datetime import datetime
import re

app = Flask(__name__)

# Gemini API
GENAI_API_KEY = "AIzaSyCRIeEQKQEHi-3hdaD87i2FAkDQT5ISW9c"
genai.configure(api_key=GENAI_API_KEY)
model = genai.GenerativeModel("models/gemini-1.5-flash")

# RapidAPI details
RAPID_API_KEY = "b0d6f1a392mshdccedf8dce5241ap1c6a20jsnc9d0b2fc9530"
RAPID_API_HOST = "irctc1.p.rapidapi.com"

def get_train_data(from_station, to_station, journey_date=None):
    url = "https://irctc1.p.rapidapi.com/api/v3/trainBetweenStations"
    headers = {
        "X-Rapidapi-Key": RAPID_API_KEY,
        "X-Rapidapi-Host": RAPID_API_HOST
    }

    if journey_date is None:
        journey_date = datetime.today().strftime('%Y-%m-%d')

    params = {
        "fromStationCode": from_station.upper(),
        "toStationCode": to_station.upper(),
        "dateOfJourney": journey_date
    }

    try:
        response = requests.get(url, headers=headers, params=params)
        data = response.json()
        train_list = []
        for train in data.get("data", []):
            train_info = {
                "train_name": train.get("train_name"),
                "train_number": train.get("train_number"),
                "departure_time": train.get("from_sta"),
                "arrival_time": train.get("to_sta"),
                "duration": train.get("duration"),
                "available_classes": train.get("available_classes", [])
            }
            train_list.append(train_info)
        return train_list

    except Exception as e:
        return [{"error": str(e)}]

def format_trains(trains):
    if not trains:
        return "No trains found."

    formatted = ""
    for t in trains:
        if "error" in t:
            return f"Error fetching train data: {t['error']}"
        formatted += f"- **{t['train_name']}** ({t['train_number']}) | Departs: {t['departure_time']} | Arrives: {t['arrival_time']} | Duration: {t['duration']} | Classes: {', '.join(t['available_classes'])}\n"
    return formatted

def create_fallback_prompt(origin, destination):
    return f"""
There are no direct trains between {origin} and {destination}.

Please return two closest major railway station codes near:
1. **{origin}**
2. **{destination}**

Only output JSON in this format:
{{
  "from_station": "XXX",
  "to_station": "YYY"
}}
Do NOT include explanation, only valid IRCTC codes.
"""

def create_summary_prompt(data, train_options):
    origin = data.get("origin", "Not specified")
    destination = data.get("destination", "Not specified")
    travel_dates = data.get("travel_dates", "Not specified")
    interests = data.get("interests", "temples, nature, food")
    train_summary = format_trains(train_options)

    return f"""
You are a friendly Indian travel expert AI.

The user wants to travel from **{origin}** to **{destination}** on **{travel_dates}**.

🎯 Generate a helpful travel summary covering only transportation:

🚆 **Train Travel**:
{train_summary}

🚌 **Bus Travel**:
- List govt & private buses, duration, and costs.

✈️ **Air Travel**:
- Nearest airports, flights, fare, and duration.

Avoid sightseeing, food, stays, or culture. Only transport info.
"""

@app.route("/plan-trip", methods=["POST"])
def plan_trip():
    try:
        data = request.get_json()
        origin = data.get("origin", "NDLS")
        destination = data.get("destination", "BCT")
        travel_date = data.get("travel_dates", datetime.today().strftime('%Y-%m-%d'))
        print(f"Planning trip from {origin} to {destination} on {travel_date}"
        )

        train_data = get_train_data(origin, destination, travel_date)

        if not train_data:
            fallback_prompt = create_fallback_prompt(origin, destination)
            fallback_response = model.generate_content(fallback_prompt)
            clean_text = re.sub(r"```json|```", "", fallback_response.text.strip()).strip()

            try:
                station_suggestion = json.loads(clean_text)
                fallback_from = station_suggestion.get("from_station", origin)
                fallback_to = station_suggestion.get("to_station", destination)
                train_data = get_train_data(fallback_from, fallback_to, travel_date)
            except Exception as e:
                train_data = [{"error": "Could not fetch alternate stations from Gemini."}]

        summary_prompt = create_summary_prompt(data, train_data)
        summary_response = model.generate_content(summary_prompt)

        return jsonify({
            "success": True,
            "response": summary_response.text,
            "trains": train_data
        })

    except Exception as e:
        return jsonify({"success": False, "error": str(e)})


def ask_gemini(prompt):
    try:
        response = model.generate_content(prompt)
        return response.text.strip()
    except Exception as e:
        return f"Gemini error: {str(e)}"

@app.route("/itinerary", methods=["GET"])
def itinerary():
    location = request.args.get("location")
    days = request.args.get("days", 3)
    prompt = f"Generate a detailed {days}-day travel itinerary for {location}, including sightseeing, rest periods, and local experiences."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/stay-options", methods=["GET"])
def stay_options():
    location = request.args.get("location")
    prompt = f"Suggest the best stay options (budget, mid-range, luxury) in {location}. Include 2-3 options per category with brief descriptions."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/local-conveyance", methods=["GET"])
def local_conveyance():
    location = request.args.get("location")
    prompt = f"Suggest local transport options in {location} such as autos, cabs, metro, and rental bikes. Provide costs and tips."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/nearby-attractions", methods=["GET"])
def nearby_attractions():
    location = request.args.get("location")
    prompt = f"List popular nearby attractions around {location} with short descriptions. Include distance and how to reach."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/markets", methods=["GET"])
def markets():
    location = request.args.get("location")
    prompt = f"List top local markets in {location} for shopping, souvenirs, and street shopping. Mention what they're known for."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/food-restaurants", methods=["GET"])
def food_restaurants():
    location = request.args.get("location")
    prompt = f"Suggest top local food and restaurant places to try in {location}. Include street food, cafes, and dine-in."
    return jsonify({"response": ask_gemini(prompt)})

@app.route("/things-to-do", methods=["GET"])
def things_to_do():
    location = request.args.get("location")
    prompt = f"Suggest fun and cultural things to do in {location}. Include experiences like boat rides, local shows, adventure, etc."
    return jsonify({"response": ask_gemini(prompt)})



@app.route("/full-trip-summary", methods=["POST"])
def full_trip_summary():
    try:
        data = request.get_json()
        location = data.get("location", "Varanasi")
        days = data.get("days", 3)

        # Prompts for each section
        prompts = {
            "itinerary": f"Generate a detailed {days}-day travel itinerary for {location}, including sightseeing, rest periods, and local experiences.",
            "stay_options": f"Suggest the best stay options (budget, mid-range, luxury) in {location}. Include 2-3 options per category with brief descriptions.",
            "local_conveyance": f"Suggest local transport options in {location} such as autos, cabs, metro, and rental bikes. Provide costs and tips.",
            "nearby_attractions": f"List popular nearby attractions around {location} with short descriptions. Include distance and how to reach.",
            "markets": f"List top local markets in {location} for shopping, souvenirs, and street shopping. Mention what they're known for.",
            "food_restaurants": f"Suggest top local food and restaurant places to try in {location}. Include street food, cafes, and dine-in.",
            "things_to_do": f"Suggest fun and cultural things to do in {location}. Include experiences like boat rides, local shows, adventure, etc."
        }

        combined_response = {}
        for key, prompt in prompts.items():
            try:
                gemini_response = model.generate_content(prompt)
                combined_response[key] = gemini_response.text.strip()
            except Exception as e:
                combined_response[key] = f"Error: {str(e)}"

        return jsonify({"success": True, "data": combined_response})

    except Exception as e:
        return jsonify({"success": False, "error": str(e)})




if __name__ == "__main__":
    app.run(debug=True)
