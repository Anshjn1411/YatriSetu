package com.project.tripplanner

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActionCardsSection(
    destination: String,
    origin: String,
    fromDate: LocalDate,
    toDate: LocalDate,
    preferredMode: String,
    viewModel: TripPlannerViewModel,
    context: Context
) {
    val actionItems = listOf(
        ActionItem("🗺️", "Complete Trip Plan", DivyaKripaColors.Primary) {
            if (destination.isBlank() || origin.isBlank())
                showToast(context, "Please enter both origin and destination 🙏")
            else viewModel.fetchTripSummary(origin, destination, fromDate, toDate, preferredMode)
        },
        ActionItem("📜", "Itinerary", DivyaKripaColors.Secondary) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchItinerary(destination)
        },
        ActionItem("🍛", "Food & Restaurants", DivyaKripaColors.Accent) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchFood(destination)
        },
        ActionItem("🛏️", "Stay Options", DivyaKripaColors.Pink) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchStayOptions(destination)
        },
        ActionItem("🛺", "Local Transport", DivyaKripaColors.Primary) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchLocalConveyance(destination)
        },
        ActionItem("🛍️", "Markets", DivyaKripaColors.Secondary) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchMarkets(destination)
        },
        ActionItem("🎭", "Things To Do", DivyaKripaColors.Accent) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchThingsToDo(destination)
        },
        ActionItem("🏛️", "Nearby Attractions", DivyaKripaColors.Pink) {
            if (destination.isBlank()) showToast(context, "Please enter a temple name first 🙏")
            else viewModel.fetchAttractions(destination)
        }
    )

    // First row - Complete Trip Plan (full width)
    ActionCard(
        modifier = Modifier.fillMaxWidth(),
        actionItem = actionItems[0]
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Remaining items in grid
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        actionItems.drop(1).chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    ActionCard(
                        modifier = Modifier.weight(1f),
                        actionItem = item
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class ActionItem(
    val icon: String,
    val title: String,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    actionItem: ActionItem
) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .clickable {
                isPressed = true
                actionItem.onClick()
            }
            .scale(if (isPressed) 0.95f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = actionItem.color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, actionItem.color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                actionItem.icon,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                actionItem.title,
                fontWeight = FontWeight.Medium,
                color = actionItem.color,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

