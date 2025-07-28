
package com.example.travelapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tripplanner.BudgetEstimateResponse
import com.project.tripplanner.GeminiResponse
import com.project.tripplanner.HealthCheckResponse
import com.project.tripplanner.PlanTripRequest
import com.project.tripplanner.PlanTripResponse
import com.project.tripplanner.PopularDestinationsResponse
import com.project.tripplanner.RetrofitInstance
import com.project.tripplanner.TravelGuideRequest
import com.project.tripplanner.TravelGuideResponse
import com.project.tripplanner.WeatherResponse
import kotlinx.coroutines.launch

class TravelViewModel : ViewModel() {



    // UI State holder
    var tripUiState by mutableStateOf(TripUiState())
        private set

    fun onDestinationChanged(value: String) {
        tripUiState = tripUiState.copy(destination = value)
    }

    fun onDaysChanged(value: String) {
        tripUiState = tripUiState.copy(days = value)
    }

    fun onTravelerChanges(value: String) {
        tripUiState = tripUiState.copy(travelers = value)
    }

    fun onInterestsChanged(value: String) {
        tripUiState = tripUiState.copy(interests = value)
    }
    fun onBudgetChanged(value: String) {
        tripUiState = tripUiState.copy(budget = value)
    }


    fun resetTripData() {
        tripUiState = TripUiState()
    }
    var isLoading by mutableStateOf(false)
        private set

    var apiResponse by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun resetState() {
        isLoading = false
        apiResponse = null
        errorMessage = null
    }

    private fun handleError(error: String) {
        isLoading = false
        errorMessage = error
        apiResponse = null
    }

    // Health Check
    fun checkHealth() {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.healthCheck()
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Health check failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Plan Trip
    fun planTrip(location: String, days: Int, budget: String, interests: String?) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val request = PlanTripRequest(location, days, budget, interests)
                Log.d("PlanTripResponse", "Response: $request")
                val response = RetrofitInstance.api.planTrip(request)
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("PlanTripResponse", "Response: $data")
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Plan trip failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Travel Guide
    fun getTravelGuide(location: String, days: Int, budget: String, interests: String?) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val request = TravelGuideRequest(location, days, budget, interests)
                val response = RetrofitInstance.api.getTravelGuide(request)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Travel guide failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Itinerary
    fun getItinerary(location: String, days: Int) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getItinerary(location, days)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Itinerary failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Stay Options
    fun getStayOptions(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getStayOptions(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Stay options failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Local Conveyance
    fun getLocalConveyance(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getLocalConveyance(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Local conveyance failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Nearby Attractions
    fun getNearbyAttractions(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getNearbyAttractions(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Attractions failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Markets
    fun getMarkets(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getMarkets(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Markets failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Food Restaurants
    fun getFoodRestaurants(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getFoodRestaurants(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Food restaurants failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Things To Do
    fun getThingsToDo(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getThingsToDo(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Things to do failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Popular Destinations
    fun getPopularDestinations() {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getPopularDestinations()
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Popular destinations failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Weather
    fun getWeather(location: String) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getWeather(location)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Weather failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Get Budget Estimate
    fun getBudgetEstimate(location: String, days: Int, travelers: Int) {
        viewModelScope.launch {
            resetState()
            isLoading = true
            try {
                val response = RetrofitInstance.api.getBudgetEstimate(location, days, travelers)
                if (response.isSuccessful) {
                    val data = response.body()
                    apiResponse = data?.response ?: "No Data Found"
                } else {
                    handleError("Budget estimate failed: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("Network error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


}




data class TripUiState(
    val destination: String = "",
    val days: String = "",
    val budget: String = "",
    val travelers: String="",
    val interests: String = "",
    val description: String = ""
)
