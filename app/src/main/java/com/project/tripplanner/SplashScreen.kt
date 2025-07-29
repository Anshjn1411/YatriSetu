// YatraBotTheme.kt - Design System
package com.project.tripplanner.ui.theme

// =============================================================================
// Enhanced TripInputScreen.kt with Beautiful UI
// =============================================================================


import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.travelapp.TravelViewModel
import com.project.tripplanner.ui.theme.YatraColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTripInputScreen(
    navController: NavController,
    viewModel: TravelViewModel
) {
    val uiState = viewModel.tripUiState
    val context = LocalContext.current
    val budgetOptions = listOf("low", "medium", "high")
    val budgetIcons = mapOf(
        "low" to Icons.Outlined.AttachMoney,
        "medium" to Icons.Filled.MonetizationOn,
        "high" to Icons.Filled.Diamond
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(YatraColors.Background, YatraColors.Surface)
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        // Beautiful Header with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            YatraColors.SaffronPrimary,
                            YatraColors.SaffronDark
                        ),
                        radius = 800f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Flight,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "YatraBot",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontSize = 32.sp
                    )
                )
                Text(
                    "Your AI Travel Companion",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }

        // Progress Indicator
        LinearProgressIndicator(
            progress = 0.33f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = YatraColors.TurquoiseSecondary,
            trackColor = YatraColors.TurquoiseLight.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(tween(300)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.EditNote,
                        contentDescription = null,
                        tint = YatraColors.SaffronPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Plan Your Journey",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Destination Input with Beautiful Styling
                OutlinedTextField(
                    value = uiState.destination,
                    onValueChange = { viewModel.onDestinationChanged(it) },
                    label = { Text("Where do you want to go?") },
                    placeholder = { Text("e.g., Goa, Rajasthan, Kerala") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = YatraColors.SaffronPrimary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YatraColors.SaffronPrimary,
                        focusedLabelColor = YatraColors.SaffronPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Days and Travelers Row
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.days,
                        onValueChange = { viewModel.onDaysChanged(it) },
                        label = { Text("Days") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                null,
                                tint = YatraColors.TurquoiseSecondary
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YatraColors.TurquoiseSecondary,
                            focusedLabelColor = YatraColors.TurquoiseSecondary
                        )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = uiState.travelers,
                        onValueChange = { viewModel.onTravelerChanges(it) },
                        label = { Text("Travelers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Group,
                                null,
                                tint = YatraColors.ForestGreen
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YatraColors.ForestGreen,
                            focusedLabelColor = YatraColors.ForestGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Budget Selection with Beautiful Cards
                Text(
                    "Choose Your Budget",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    budgetOptions.forEach { option ->
                        val isSelected = uiState.budget == option
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            onClick = { viewModel.onBudgetChanged(option) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    YatraColors.WarmGold.copy(alpha = 0.2f)
                                else Color.White
                            ),
                            border = if (isSelected)
                                CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.linearGradient(
                                        colors = listOf(YatraColors.WarmGold, YatraColors.SaffronPrimary)
                                    )
                                )
                            else CardDefaults.outlinedCardBorder(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    budgetIcons[option] ?: Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = if (isSelected) YatraColors.SaffronPrimary else YatraColors.TextSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    option.capitalize(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (isSelected) YatraColors.SaffronPrimary else YatraColors.TextSecondary,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interests Input
                OutlinedTextField(
                    value = uiState.interests,
                    onValueChange = { viewModel.onInterestsChanged(it) },
                    label = { Text("Your Interests (Optional)") },
                    placeholder = { Text("beaches, temples, adventure, food") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Favorite,
                            null,
                            tint = Color(0xFFE91E63)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        focusedLabelColor = Color(0xFFE91E63)
                    ),
                    minLines = 2
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enhanced Next Button
        Button(
            onClick = { navController.navigate("trip_actions") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = YatraColors.SaffronPrimary
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Continue Planning",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// =============================================================================
// Enhanced TripActionScreen.kt with Beautiful Action Cards
// =============================================================================

@Composable
fun EnhancedTripActionScreen(
    navController: NavController,
    viewModel: TravelViewModel
) {
    val uiState = viewModel.tripUiState

    data class ActionItem(
        val title: String,
        val description: String,
        val icon: ImageVector,
        val color: Color,
        val action: () -> Unit
    )

    val actionItems = listOf(
        ActionItem(
            "Complete Trip Plan",
            "Get a detailed itinerary for ${uiState.destination}",
            Icons.Default.Map,
            YatraColors.SaffronPrimary
        ) {
            viewModel.planTrip(
                location = uiState.destination,
                days = uiState.days.toIntOrNull() ?: 1,
                budget = "Medium",
                interests = uiState.interests
            )
            navController.navigate("trip_response")
        },
        ActionItem(
            "Budget Estimate",
            "Calculate costs for ${uiState.days} days trip",
            Icons.Default.AccountBalance,
            YatraColors.WarmGold
        ) {
            viewModel.getBudgetEstimate(
                uiState.destination,
                uiState.days.toIntOrNull() ?: 1,
                uiState.travelers.toIntOrNull() ?: 1
            )
            navController.navigate("trip_response")
        },
        ActionItem(
            "Food & Restaurants",
            "Discover local cuisine and dining spots",
            Icons.Default.Restaurant,
            Color(0xFFFF6B35)
        ) {
            viewModel.getFoodRestaurants(uiState.destination)
            navController.navigate("trip_response")
        },
        ActionItem(
            "Stay Options",
            "Find hotels and accommodations",
            Icons.Default.Hotel,
            YatraColors.TurquoiseSecondary
        ) {
            viewModel.getStayOptions(uiState.destination)
            navController.navigate("trip_response")
        },
        ActionItem(
            "Attractions & Places",
            "Must-visit landmarks and attractions",
            Icons.Default.Place,
            Color(0xFF8E24AA)
        ) {
            viewModel.getNearbyAttractions(uiState.destination)
            navController.navigate("trip_response")
        },
        ActionItem(
            "Local Transport",
            "Transportation options and routes",
            Icons.Default.DirectionsBus,
            YatraColors.ForestGreen
        ) {
            viewModel.getLocalConveyance(uiState.destination)
            navController.navigate("trip_response")
        },
        ActionItem(
            "Shopping & Markets",
            "Local markets and shopping areas",
            Icons.Default.ShoppingBag,
            Color(0xFFE91E63)
        ) {
            viewModel.getMarkets(uiState.destination)
            navController.navigate("trip_response")
        },
        ActionItem(
            "Activities & Things to Do",
            "Fun activities and experiences",
            Icons.Default.Celebration,
            Color(0xFFFF9800)
        ) {
            viewModel.getThingsToDo(uiState.destination)
            navController.navigate("trip_response")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(YatraColors.Background, YatraColors.Surface)
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        // Header with Trip Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.TravelExplore,
                    contentDescription = null,
                    tint = YatraColors.SaffronPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    uiState.destination,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = YatraColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${uiState.days} days",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = YatraColors.TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Group,
                        contentDescription = null,
                        tint = YatraColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${uiState.travelers} travelers",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = YatraColors.TextSecondary
                        )
                    )
                }
            }
        }

        // Progress Indicator
        LinearProgressIndicator(
            progress = 0.66f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = YatraColors.TurquoiseSecondary,
            trackColor = YatraColors.TurquoiseLight.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "What would you like to explore?",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Items Grid
        actionItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp),
                        onClick = item.action,
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        item.color.copy(alpha = 0.1f),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = null,
                                    tint = item.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                item.title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                item.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = YatraColors.TextSecondary
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                        }
                    }
                }

                // Fill empty space if odd number of items
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

//// =============================================================================
//// Enhanced TripResponseScreen.kt with Beautiful Response Display
//// =============================================================================
//
//
//
//// =============================================================================
//// Additional Utility Components for Enhanced UI
//// =============================================================================
//
//@Composable
//fun TripStepIndicator(
//    currentStep: Int,
//    totalSteps: Int,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        repeat(totalSteps) { step ->
//            val isCompleted = step < currentStep
//            val isCurrent = step == currentStep
//
//            Box(
//                modifier = Modifier
//                    .size(if (isCurrent) 16.dp else 12.dp)
//                    .background(
//                        when {
//                            isCompleted -> YatraColors.ForestGreen
//                            isCurrent -> YatraColors.SaffronPrimary
//                            else -> YatraColors.TextSecondary.copy(alpha = 0.3f)
//                        },
//                        RoundedCornerShape(50)
//                    )
//            )
//
//            if (step < totalSteps - 1) {
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(2.dp)
//                        .background(
//                            if (isCompleted) YatraColors.ForestGreen
//                            else YatraColors.TextSecondary.copy(alpha = 0.3f)
//                        )
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WeatherInfoCard(
//    location: String,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = YatraColors.TurquoiseSecondary.copy(alpha = 0.1f)
//        ),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                Icons.Default.WbSunny,
//                contentDescription = null,
//                tint = YatraColors.WarmGold,
//                modifier = Modifier.size(32.dp)
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(
//                    "Weather in $location",
//                    style = MaterialTheme.typography.titleSmall.copy(
//                        fontWeight = FontWeight.SemiBold
//                    )
//                )
//                Text(
//                    "Perfect weather for traveling!",
//                    style = MaterialTheme.typography.bodySmall.copy(
//                        color = YatraColors.TextSecondary
//                    )
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun QuickTipCard(
//    tip: String,
//    icon: ImageVector,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = YatraColors.WarmGold.copy(alpha = 0.1f)
//        ),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                icon,
//                contentDescription = null,
//                tint = YatraColors.WarmGold,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Text(
//                tip,
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = YatraColors.EarthyBrown
//                )
//            )
//        }
//    }
//}
//
//// =============================================================================
//// Popular Destinations Component
//// =============================================================================
//
//@Composable
//fun PopularDestinationsCard(
//    onDestinationClick: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val popularPlaces = listOf(
//        "Goa" to "🏖️",
//        "Rajasthan" to "🏰",
//        "Kerala" to "🌴",
//        "Himachal Pradesh" to "⛰️",
//        "Uttarakhand" to "🏔️",
//        "Tamil Nadu" to "🛕"
//    )
//
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(modifier = Modifier.padding(20.dp)) {
//            Text(
//                "Popular Destinations",
//                style = MaterialTheme.typography.titleMedium.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = YatraColors.SaffronDark
//                )
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            popularPlaces.chunked(2).forEach { rowPlaces ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    rowPlaces.forEach { (place, emoji) ->
//                        Button(
//                            onClick = { onDestinationClick(place) },
//                            modifier = Modifier.weight(1f),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = YatraColors.CreamWhite,
//                                contentColor = YatraColors.SaffronDark
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Text("$emoji $place")
//                        }
//                    }
//                    if (rowPlaces.size == 1) {
//                        Spacer(modifier = Modifier.weight(1f))
//                    }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
//}