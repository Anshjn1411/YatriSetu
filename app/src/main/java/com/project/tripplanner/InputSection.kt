package com.project.tripplanner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InputSection(
    destination: String,
    origin: String,
    onDestinationChange: (String) -> Unit,
    onOriginChange: (String) -> Unit,
    fromDate: LocalDate,
    toDate: LocalDate,
    preferredMode: String,
    onPreferredModeChange: (String) -> Unit,
    onDateClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Origin Input
            OutlinedTextField(
                value = origin,
                onValueChange = onOriginChange,
                label = { Text("From (Origin City)") },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = DivyaKripaColors.Primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DivyaKripaColors.Primary,
                    focusedLabelColor = DivyaKripaColors.Primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Temple City Input
            OutlinedTextField(
                value = destination,
                onValueChange = onDestinationChange,
                label = { Text("To (Temple City/Destination)") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = DivyaKripaColors.Primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DivyaKripaColors.Primary,
                    focusedLabelColor = DivyaKripaColors.Primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Selection Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DateCard(
                    modifier = Modifier.weight(1f),
                    label = "From Date",
                    date = fromDate,
                    onClick = { onDateClick("from") }
                )

                DateCard(
                    modifier = Modifier.weight(1f),
                    label = "To Date",
                    date = toDate,
                    onClick = { onDateClick("to") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transport Mode Selection
            Text(
                "Preferred Transport Mode",
                fontSize = 14.sp,
                color = DivyaKripaColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("train", "bus", "flight").forEach { mode ->
                    FilterChip(
                        onClick = { onPreferredModeChange(mode) },
                        label = { Text(mode.capitalize()) },
                        selected = preferredMode == mode,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DivyaKripaColors.Primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}