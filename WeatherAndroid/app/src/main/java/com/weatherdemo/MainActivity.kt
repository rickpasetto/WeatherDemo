package com.weatherdemo

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherdemo.services.GeocodingService
import com.weatherdemo.services.WeatherService
import com.weatherdemo.ui.WeatherListScreen
import com.weatherdemo.viewmodels.WeatherViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val weatherService = WeatherService()
        val geocoder = Geocoder(this, Locale.getDefault())
        val geocodingService = GeocodingService(geocoder)
        
        val viewModel: WeatherViewModel by viewModels {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeatherViewModel(weatherService, geocodingService) as T
                }
            }
        }
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherListScreen(viewModel = viewModel)
                }
            }
        }
    }
}

