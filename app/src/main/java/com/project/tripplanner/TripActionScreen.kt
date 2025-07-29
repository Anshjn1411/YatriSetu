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
import androidx.compose.foundation.background
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
import com.project.tripplanner.ui.theme.YatraColors
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTripResponseScreen(
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    YatraColors.Background,
                                    YatraColors.Surface
                                )
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = YatraColors.SaffronPrimary,
                        strokeWidth = 6.dp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Creating your perfect trip...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = YatraColors.SaffronPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "This might take a few moments",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = YatraColors.TextSecondary
                        )
                    )
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Oops! Something went wrong",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFFE53E3E)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        error,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = YatraColors.TextSecondary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = YatraColors.SaffronPrimary
                        )
                    ) {
                        Text("Try Again")
                    }
                }
            }

            responseText != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(YatraColors.Background)
                ) {
                    // Top App Bar
                    TopAppBar(
                        title = {
                            Text(
                                "Your Trip Plan",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = YatraColors.SaffronDark
                        )
                    )

                    // Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                EnhancedFormattedText(responseText)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Bottom Action Bar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, responseText)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Trip Plan")
                                    context.startActivity(shareIntent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YatraColors.TurquoiseSecondary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    // TODO: Implement save logic
                                    Toast.makeText(context, "Trip saved successfully! ✈️", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YatraColors.ForestGreen
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.BookmarkAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun EnhancedFormattedText(rawText: String) {
    val lines = rawText.split("\n")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (line in lines) {
            when {
                line.startsWith("##") -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = YatraColors.SaffronPrimary.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = line.removePrefix("##").trim(),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = YatraColors.SaffronDark
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                line.startsWith("**") && line.endsWith("**") -> {
                    Text(
                        text = line.replace("**", "").trim(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = YatraColors.SaffronPrimary
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                line.trim().startsWith("*") -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    YatraColors.TurquoiseSecondary,
                                    RoundedCornerShape(50)
                                )
                                .padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = line.trim().removePrefix("*").trim(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                line.contains("Day ") && line.contains(":") -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = YatraColors.WarmGold.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Today,
                                contentDescription = null,
                                tint = YatraColors.WarmGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = line.trim(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = YatraColors.EarthyBrown
                                )
                            )
                        }
                    }
                }

                line.contains("₹") || line.contains("Cost") || line.contains("Budget") -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = YatraColors.ForestGreen.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = YatraColors.ForestGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = line.trim(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = YatraColors.ForestGreen
                                )
                            )
                        }
                    }
                }

                line.isBlank() -> {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                else -> {
                    if (line.trim().isNotEmpty()) {
                        Text(
                            text = line.trim(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 24.sp
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}