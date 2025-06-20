package com.project.tripplanner

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class PlanTripRequest(
    val origin: String,
    val destination: String,
    val travel_dates: String,
    val preferred_mode: String,
    val itinerary_days: Int
)

data class PlanTripResponse(
    val response: String,
    val success: Boolean,
    val trains: List<TrainInfo>? = null
)

data class TrainInfo(
    val arrival_time: String,
    val available_classes: List<String>,
    val departure_time: String,
    val duration: String,
    val train_name: String,
    val train_number: String
)
data class GeminiResponse(val response: String)

interface TripPlannerApiService {
    @POST("plan-trip")
    suspend fun planTrip(@Body request: PlanTripRequest): PlanTripResponse

    @GET("itinerary")
    suspend fun getItinerary(@Query("location") location: String, @Query("days") days: Int): GeminiResponse

    @GET("stay-options")
    suspend fun getStayOptions(@Query("location") location: String): GeminiResponse

    @GET("local-conveyance")
    suspend fun getLocalConveyance(@Query("location") location: String): GeminiResponse

    @GET("nearby-attractions")
    suspend fun getNearbyAttractions(@Query("location") location: String): GeminiResponse

    @GET("markets")
    suspend fun getMarkets(@Query("location") location: String): GeminiResponse

    @GET("food-restaurants")
    suspend fun getFoodRestaurants(@Query("location") location: String): GeminiResponse

    @GET("things-to-do")
    suspend fun getThingsToDo(@Query("location") location: String): GeminiResponse
}


object RetrofitInstance {
    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // ✅ Base URL only, with trailing slash
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val response2: TripPlannerApiService by lazy {
        getInstance().create(TripPlannerApiService::class.java)
    }
}
