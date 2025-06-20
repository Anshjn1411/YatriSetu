package com.project.tripplanner

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Custom Color Palette
object DivyaKripaColors {
    val Primary = Color(0xFFFF9800) // Saffron Orange
    val Secondary = Color(0xFFFFC107) // Golden Yellow
    val Background = Color(0xFFFFFBF0) // Warm White
    val Surface = Color(0xFFFFECB3) // Light Golden
    val Accent = Color(0xFFD4AF37) // Sandalwood Gold
    val Pink = Color(0xFFFFB6C1) // Floral Pink
    val TextPrimary = Color(0xFF8D6E63) // Brown
    val TextSecondary = Color(0xFF5D4037) // Dark Brown
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DivyaKripaTempleTripScreen(viewModel: TripPlannerViewModel) {
    val destination by viewModel.destination.collectAsState()
    val origin by viewModel.origin.collectAsState()
    val itinerary by viewModel.itinerary.collectAsState()
    val food by viewModel.food.collectAsState()
    val stay by viewModel.stay.collectAsState()
    val market by viewModel.market.collectAsState()
    val local by viewModel.local.collectAsState()
    val things by viewModel.thingsToDo.collectAsState()
    val attraction by viewModel.attraction.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val error by viewModel.error.collectAsState()

    var fromDate by remember { mutableStateOf(LocalDate.now()) }
    var toDate by remember { mutableStateOf(LocalDate.now().plusDays(3)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerType by remember { mutableStateOf("from") }
    var playChants by remember { mutableStateOf(false) }
    var preferredMode by remember { mutableStateOf("train") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Animated background particles
    val particleOffsets = remember { mutableStateListOf<Offset>() }
    val particleAlphas = remember { mutableStateListOf<Float>() }

    LaunchedEffect(Unit) {
        repeat(8) {
            particleOffsets.add(Offset(kotlin.random.Random.nextFloat() * 1000, kotlin.random.Random.nextFloat() * 2000))
            particleAlphas.add(kotlin.random.Random.nextFloat() * 0.3f + 0.1f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DivyaKripaColors.Background,
                        DivyaKripaColors.Surface.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        // Floating particles animation
        FloatingParticles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Input Section
            InputSection(
                destination = destination,
                origin = origin,
                onDestinationChange = { viewModel.destination.value = it },
                onOriginChange = { viewModel.setOrigin(it) },
                fromDate = fromDate,
                toDate = toDate,
                preferredMode = preferredMode,
                onPreferredModeChange = { preferredMode = it },
                onDateClick = { type ->
                    datePickerType = type
                    showDatePicker = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Cards
            ActionCardsSection(
                destination = destination,
                origin = origin,
                fromDate = fromDate,
                toDate = toDate,
                preferredMode = preferredMode,
                viewModel = viewModel,
                context = context
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Display
            if (error.isNotEmpty()) {
                ErrorCard(error)
            }

            // Response Cards
            ResponseCardsSection(
                itinerary = itinerary,
                food = food,
                stay = stay,
                market = market,
                local = local,
                things = things,
                attraction = attraction,
                summary = summary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Footer
            FooterSection()

            Spacer(modifier = Modifier.height(80.dp)) // Space for floating button
        }

        // Floating Om Button
        FloatingOmButton(
            playChants = playChants,
            onToggle = { playChants = !playChants },
            modifier = Modifier.align(Alignment.BottomEnd)
        )

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    if (datePickerType == "from") {
                        fromDate = selectedDate
                        if (toDate.isBefore(selectedDate)) {
                            toDate = selectedDate.plusDays(1)
                        }
                    } else {
                        toDate = selectedDate
                    }
                    showDatePicker = false
                },
                if (datePickerType == "from") fromDate.year else toDate.year,
                if (datePickerType == "from") fromDate.monthValue - 1 else toDate.monthValue - 1,
                if (datePickerType == "from") fromDate.dayOfMonth else toDate.dayOfMonth
            ).show()
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateCard(
    modifier: Modifier = Modifier,
    label: String,
    date: LocalDate,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DivyaKripaColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                label,
                fontSize = 12.sp,
                color = DivyaKripaColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                date.format(DateTimeFormatter.ofPattern("dd MMM")),
                fontWeight = FontWeight.Bold,
                color = DivyaKripaColors.Primary
            )
        }
    }
}



