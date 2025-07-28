package com.project.tripplanner

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.unit.dp
import com.example.travelapp.TravelViewModel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch



@Composable
fun TripActionScreen(
    navController: NavController,
    viewModel: TravelViewModel
) {
    val uiState = viewModel.tripUiState
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Plan a Trip to ${uiState.destination} for ${uiState.days} days")
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.planTrip(
                    location = uiState.destination,
                    days = uiState.days.toIntOrNull() ?: 1,
                    budget = "Medium",
                    interests = uiState.description
                )
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Trip Plan")
        }

        Button(
            onClick = {
                viewModel.getPopularDestinations()
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Popular Destinations")
        }

        Button(
            onClick = {
                viewModel.getStayOptions(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stay Options")
        }
        Button(
            onClick = {
                viewModel.getFoodRestaurants(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Food & Restaurants")
        }

        Button(
            onClick = {
                viewModel.getNearbyAttractions(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Attractions")
        }


        Button(
            onClick = {
                viewModel.getLocalConveyance(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Local Transport")
        }

        Button(
            onClick = {
                viewModel.getMarkets(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Markets")
        }

        Button(
            onClick = {
                viewModel.getThingsToDo(uiState.destination)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Things to Do")
        }
        Button(
            onClick = {
                viewModel.getBudgetEstimate(uiState.destination, uiState.days.toIntOrNull() ?: 1, uiState.travelers.toIntOrNull() ?: 1)
                navController.navigate("trip_response")},
            modifier = Modifier.fillMaxWidth()
            ) {
            Text("Budget Estimate")
        }

    }
}



@Composable
fun TripResponseScreen(
    viewModel: TravelViewModel,
    onBack: () -> Unit
) {
    val isLoading = viewModel.isLoading
    val responseText = viewModel.apiResponse
    val error = viewModel.errorMessage
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            responseText != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Trip Plan",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FormattedText(responseText)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, responseText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Trip Plan")
                            context.startActivity(shareIntent)
                        }) {
                            Text("Share")
                        }

                        Button(onClick = {
                            // TODO: Implement your save logic (DB or file)
                            Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Save")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Back")
                    }
                }
            }
        }
    }
}


@Composable
fun FormattedText(rawText: String) {
    val lines = rawText.split("\n")

    Column {
        for (line in lines) {
            when {
                line.startsWith("##") -> {
                    Text(
                        text = line.removePrefix("##").trim(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                line.startsWith("**") -> {
                    Text(
                        text = line.replace("**", "").trim(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                line.trim().startsWith("*") -> {
                    Text(
                        text = line.trim().replaceFirst("*", "•"),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                    )
                }

                line.isBlank() -> {
                    Spacer(modifier = Modifier.height(6.dp))
                }

                else -> {
                    Text(
                        text = line.trim(),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}


