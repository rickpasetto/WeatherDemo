package com.weatherdemo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherdemo.models.City
import com.weatherdemo.models.WeatherInfo
import com.weatherdemo.services.GeocodingService
import com.weatherdemo.services.WeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherService: WeatherService,
    private val geocodingService: GeocodingService
) : ViewModel() {
    
    private val _weatherData = MutableStateFlow<List<WeatherInfo>>(emptyList())
    val weatherData: StateFlow<List<WeatherInfo>> = _weatherData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val cities = mutableListOf(
        City(name = "New York", state = "NY", latitude = 40.7128, longitude = -74.0060),
        City(name = "Los Angeles", state = "CA", latitude = 34.0522, longitude = -118.2437),
        City(name = "Chicago", state = "IL", latitude = 41.8781, longitude = -87.6298),
        City(name = "Houston", state = "TX", latitude = 29.7604, longitude = -95.3698),
        City(name = "Phoenix", state = "AZ", latitude = 33.4484, longitude = -112.0740),
        City(name = "Philadelphia", state = "PA", latitude = 39.9526, longitude = -75.1652),
        City(name = "San Antonio", state = "TX", latitude = 29.4241, longitude = -98.4936),
        City(name = "San Diego", state = "CA", latitude = 32.7157, longitude = -117.1611),
        City(name = "Dallas", state = "TX", latitude = 32.7767, longitude = -96.7970),
        City(name = "San Jose", state = "CA", latitude = 37.3382, longitude = -121.8863)
    )
    
    fun loadWeather() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val results = mutableListOf<WeatherInfo>()
            
            cities.forEach { city ->
                try {
                    val weather = weatherService.fetchWeather(city)
                    results.add(weather)
                } catch (e: Exception) {
                    _errorMessage.value = "Failed to load weather: ${e.message}"
                }
            }
            
            _weatherData.value = results
            _isLoading.value = false
        }
    }
    
    fun addCity(name: String) {
        viewModelScope.launch {
            _isSearching.value = true
            _errorMessage.value = null
            
            try {
                val city = geocodingService.geocode(name)
                
                if (cities.any { it.name.equals(city.name, ignoreCase = true) }) {
                    _errorMessage.value = "City already in list"
                    _isSearching.value = false
                    return@launch
                }
                
                cities.add(city)
                
                try {
                    val weather = weatherService.fetchWeather(city)
                    _weatherData.value = _weatherData.value + weather
                } catch (e: Exception) {
                    _errorMessage.value = "Failed to load weather for ${city.name}: ${e.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Could not find city: ${e.message}"
            } finally {
                _isSearching.value = false
            }
        }
    }
}

