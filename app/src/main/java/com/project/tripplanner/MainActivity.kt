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
import com.project.tripplanner.ui.theme.EnhancedTripActionScreen
import com.project.tripplanner.ui.theme.EnhancedTripInputScreen

import com.project.tripplanner.ui.theme.YatraBotTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YatraBotTheme  {
                val navController = rememberNavController()
                val viewModel: TravelViewModel = viewModel()

                NavHost(navController = navController, startDestination = "trip_input") {
                    composable("trip_input") {
                        EnhancedTripInputScreen(navController, viewModel)
                    }
                    composable("trip_actions") {
                        EnhancedTripActionScreen(navController , viewModel)
                    }
                    composable("trip_response") {
                        EnhancedTripResponseScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                    }
                }

            }
        }
    }
}


