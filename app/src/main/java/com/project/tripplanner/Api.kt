package com.project.tripplanner

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

// Data Classes for Requests
data class PlanTripRequest(
    val location: String,
    val days: Int? = null,
    val budget: String? = null, // "low", "medium", "high"
    val interests: String? = null
)

data class TravelGuideRequest(
    val location: String,
    val days: Int? = null,
    val budget: String? = null,
    val interests: String? = null
)

// Data Classes for Responses
data class PlanTripResponse(
    val success: Boolean,
    val response: String?,
)

data class GeminiResponse(
    val success: Boolean,
    val response: String?,
)

data class HealthCheckResponse(
    val message: String,
    val version: String,
    val status: String,
    val timestamp: String
)

data class TravelGuideResponse(
    val success: Boolean,
    val location: String,
    val days: Int,
    val budget: String,
    val data: TravelGuideData?
)

data class TravelGuideData(
    val attractions: List<Attraction>?,
    val accommodation: List<Accommodation>?,
    val food: List<Restaurant>?,
    val transport: List<Transport>?,
    val itinerary: List<ItineraryDay>?,
    val shopping: List<ShoppingPlace>?,
    val culture: List<CulturalSite>?,
    val budgetBreakdown: BudgetBreakdown?
)

data class QuickInfoResponse(
    val success: Boolean,
    val category: String,
    val location: String,
    val data: Any?
)

data class PopularDestinationsResponse(
    val success: Boolean,
    val response : String?
)

data class WeatherResponse(
    val success: Boolean,
    val location: String,
    val data: WeatherData?
)

data class BudgetEstimateResponse(
    val success: Boolean,
    val location: String,
    val days: Int,
    val travelers: Int,
    val data: BudgetData?
)

// Supporting Data Classes
data class TripPlan(
    val location: String,
    val duration: Int,
    val budget: String,
    val itinerary: List<ItineraryDay>,
    val accommodation: List<Accommodation>,
    val attractions: List<Attraction>
)

data class ItineraryDay(
    val day: Int,
    val title: String,
    val activities: List<String>,
    val estimatedCost: String?
)

data class Accommodation(
    val name: String,
    val type: String,
    val priceRange: String,
    val rating: Double?,
    val amenities: List<String>?
)

data class Attraction(
    val name: String,
    val description: String,
    val category: String,
    val rating: Double?,
    val entryFee: String?,
    val timings: String?
)

data class Restaurant(
    val name: String,
    val cuisine: String,
    val priceRange: String,
    val rating: Double?,
    val speciality: String?
)

data class Transport(
    val type: String,
    val description: String,
    val estimatedCost: String?
)

data class ShoppingPlace(
    val name: String,
    val type: String,
    val description: String,
    val specialItems: List<String>?
)

data class CulturalSite(
    val name: String,
    val description: String,
    val significance: String,
    val visitingHours: String?
)

data class BudgetBreakdown(
    val accommodation: String,
    val food: String,
    val transport: String,
    val activities: String,
    val shopping: String,
    val total: String
)

data class Destination(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    val rating: Double,
    val bestTime: String
)

data class WeatherData(
    val temperature: String,
    val condition: String,
    val humidity: String,
    val windSpeed: String,
    val forecast: List<DayForecast>?
)

data class DayForecast(
    val date: String,
    val temperature: String,
    val condition: String
)

data class BudgetData(
    val totalEstimate: String,
    val perPersonPerDay: String,
    val breakdown: BudgetBreakdown
)

// Main API Interface
interface TripPlannerApiService {

    // Health Check
    @GET("/")
    suspend fun healthCheck(): Response<GeminiResponse>

    // Your existing endpoints
    @POST("plan-trip")
    suspend fun planTrip(@Body request: PlanTripRequest): Response<PlanTripResponse>

    @GET("itinerary")
    suspend fun getItinerary(
        @Query("location") location: String,
        @Query("days") days: Int
    ): Response<GeminiResponse>

    @GET("stay-options")
    suspend fun getStayOptions(@Query("location") location: String): Response<GeminiResponse>

    @GET("local-conveyance")
    suspend fun getLocalConveyance(@Query("location") location: String): Response<GeminiResponse>

    @GET("nearby-attractions")
    suspend fun getNearbyAttractions(@Query("location") location: String): Response<GeminiResponse>

    @GET("markets")
    suspend fun getMarkets(@Query("location") location: String): Response<GeminiResponse>

    @GET("food-restaurants")
    suspend fun getFoodRestaurants(@Query("location") location: String): Response<GeminiResponse>

    @GET("things-to-do")
    suspend fun getThingsToDo(@Query("location") location: String): Response<GeminiResponse>

    // Complete Travel Guide (from documentation)
    @POST("travel-guide")
    suspend fun getTravelGuide(@Body request: TravelGuideRequest): Response<GeminiResponse>

    // Quick Category Information endpoints
    @GET("quick-info/food")
    suspend fun getQuickFood(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/attractions")
    suspend fun getQuickAttractions(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/accommodation")
    suspend fun getQuickAccommodation(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/itinerary")
    suspend fun getQuickItinerary(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/transport")
    suspend fun getQuickTransport(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/shopping")
    suspend fun getQuickShopping(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    @GET("quick-info/culture")
    suspend fun getQuickCulture(
        @Query("location") location: String,
        @Query("days") days: Int? = null,
        @Query("budget") budget: String? = null
    ): Response<QuickInfoResponse>

    // Popular Destinations
    @GET("destinations/popular")
    suspend fun getPopularDestinations(): Response<GeminiResponse>

    // Weather Information
    @GET("weather/{location}")
    suspend fun getWeather(@Path("location") location: String): Response<GeminiResponse>

    // Budget Estimation
    @GET("budget-estimate/{location}")
    suspend fun getBudgetEstimate(
        @Path("location") location: String,
        @Query("days") days: Int,
        @Query("travelers") travelers: Int = 1
    ): Response<GeminiResponse>
}

// ✅ Retrofit Instance with Timeout Handling
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    private fun getInstance(): Retrofit {
        // Custom OkHttpClient with timeout
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // ⏳ connect timeout
            .readTimeout(60, TimeUnit.SECONDS)    // 📖 read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // ✍️ write timeout
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TripPlannerApiService by lazy {
        getInstance().create(TripPlannerApiService::class.java)
    }

    val response2: TripPlannerApiService by lazy {
        getInstance().create(TripPlannerApiService::class.java)
    }
}


// Usage Examples and Helper Functions
object ApiHelper {

    // Example usage functions
    suspend fun planCompleteTrip(
        location: String,
        days: Int = 3,
        budget: String = "medium",
        interests: String? = null
    ): Response<PlanTripResponse> {
        val request = PlanTripRequest(location, days, budget, interests)
        return RetrofitInstance.api.planTrip(request)
    }

    suspend fun getCompleteTravelGuide(
        location: String,
        days: Int = 3,
        budget: String = "medium",
        interests: String? = null
    ): Response<GeminiResponse> {
        val request = TravelGuideRequest(location, days, budget, interests)
        return RetrofitInstance.api.getTravelGuide(request)
    }

    suspend fun getAllQuickInfo(location: String, days: Int = 3, budget: String = "medium") = mapOf(
        "food" to RetrofitInstance.api.getQuickFood(location, days, budget),
        "attractions" to RetrofitInstance.api.getQuickAttractions(location, days, budget),
        "accommodation" to RetrofitInstance.api.getQuickAccommodation(location, days, budget),
        "transport" to RetrofitInstance.api.getQuickTransport(location, days, budget),
        "shopping" to RetrofitInstance.api.getQuickShopping(location, days, budget),
        "culture" to RetrofitInstance.api.getQuickCulture(location, days, budget)
    )

    // Error handling helper
    fun <T> handleApiResponse(response: Response<T>): ApiResult<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Empty response body")
        } else {
            ApiResult.Error("API Error: ${response.code()} ${response.message()}")
        }
    }
}

// Result wrapper for better error handling
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String) : ApiResult<T>()
    data class Loading<T>(val message: String = "Loading...") : ApiResult<T>()
}

// Extension functions for easier API calls
suspend inline fun <reified T> Response<T>.handleResult(): ApiResult<T> {
    return ApiHelper.handleApiResponse(this)
}