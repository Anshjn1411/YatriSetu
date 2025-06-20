package com.project.tripplanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResponseCardsSection(
    itinerary: String,
    food: String,
    stay: String,
    market: String,
    local: String,
    things: String,
    attraction: String,
    summary: String
) {
    if (summary.isNotEmpty()) ExpandableResponseCard("🗺️ Travel options Summary", summary)
    if (itinerary.isNotEmpty()) ExpandableResponseCard("📜 Itinerary", itinerary)
    if (food.isNotEmpty()) ExpandableResponseCard("🍛 Food & Restaurants", food)
    if (stay.isNotEmpty()) ExpandableResponseCard("🛏️ Stay Options", stay)
    if (market.isNotEmpty()) ExpandableResponseCard("🛍️ Markets", market)
    if (local.isNotEmpty()) ExpandableResponseCard("🛺 Local Transport", local)
    if (things.isNotEmpty()) ExpandableResponseCard("🎭 Things To Do", things)
    if (attraction.isNotEmpty()) ExpandableResponseCard("🏛️ Nearby Attractions", attraction)
}
@Composable
fun ExpandableResponseCard(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }

    fun parseStyledContent(input: String): AnnotatedString {
        val regex = Regex("\\*\\*(.+?)\\*\\*")
        val cleaned = input.replace("**", "")
        val builder = AnnotatedString.Builder()

        var lastIndex = 0
        regex.findAll(input).forEach { match ->
            val range = match.range
            if (range.first > lastIndex) {
                builder.append(input.substring(lastIndex, range.first).replace("**", ""))
            }
            builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                append(match.groupValues[1])
            }
            lastIndex = range.last + 1
        }
        if (lastIndex < input.length) {
            builder.append(input.substring(lastIndex).replace("**", ""))
        }
        return builder.toAnnotatedString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DivyaKripaColors.TextPrimary
                )
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = DivyaKripaColors.Primary,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DivyaKripaColors.Surface.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            parseStyledContent(content),
                            fontSize = 14.sp,
                            color = DivyaKripaColors.TextSecondary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}