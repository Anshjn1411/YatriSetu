package com.project.tripplanner

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.travelapp.TravelViewModel

import com.example.travelapp.TripPlannerTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripPlannerTheme {
                val navController = rememberNavController()
                val viewModel: TravelViewModel = viewModel()

                NavHost(navController = navController, startDestination = "trip_input") {
                    composable("trip_input") {
                        TripInputScreen(navController, viewModel)
                    }
                    composable("trip_actions") {
                        TripActionScreen(navController , viewModel)
                    }
                    composable("trip_response") {
                        TripResponseScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                    }
                }

            }
        }
    }
}


