package com.project.tripplanner

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class TripPlannerViewModel : ViewModel() {


    val destination = MutableStateFlow("")

    val fromDate = MutableStateFlow(LocalDate.now())
    val toDate = MutableStateFlow(LocalDate.now().plusDays(2))


    val itinerary = MutableStateFlow("")
    val food = MutableStateFlow("")
    val attraction = MutableStateFlow("")
    val market = MutableStateFlow("")
    val local = MutableStateFlow("")
    val stay = MutableStateFlow("")
    val thingsToDo = MutableStateFlow("")
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary.asStateFlow()

    private val _origin = MutableStateFlow("")
    val origin: StateFlow<String> = _origin.asStateFlow()
    fun setOrigin(value: String) {
        _origin.value = value
    }


    val loading = MutableStateFlow(false)
    val error = MutableStateFlow("")

    fun fetchTripSummary(origin: String, destination: String, fromDate: LocalDate, toDate: LocalDate, preferredMode: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val travelDates = "${fromDate.format(dateFormatter)} to ${toDate.format(dateFormatter)}"
                val itineraryDays = ChronoUnit.DAYS.between(fromDate, toDate).toInt()

                val req = PlanTripRequest(
                    origin = origin,
                    destination = destination,
                    travel_dates = travelDates,
                    preferred_mode = preferredMode,
                    itinerary_days = itineraryDays
                )
                val res = RetrofitInstance.response2.planTrip(req)
                Log.d("TripPlannerViewModel", "Response: $res")
                _summary.value = res.response
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchItinerary(loc: String, days: Int = getTripDays()) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getItinerary(loc, days)
                itinerary.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchFood(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getFoodRestaurants(loc)
                food.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchAttractions(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getNearbyAttractions(loc)
                attraction.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchMarkets(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getMarkets(loc)
                market.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchLocalConveyance(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getLocalConveyance(loc)
                local.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchStayOptions(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getStayOptions(loc)
                stay.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun fetchThingsToDo(loc: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.response2.getThingsToDo(loc)
                thingsToDo.value = res.response
                Log.d("TripPlannerViewModel", "Response: $res")
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun getTripDays(): Int =
        (toDate.value.toEpochDay() - fromDate.value.toEpochDay()).toInt().coerceAtLeast(1)
}
