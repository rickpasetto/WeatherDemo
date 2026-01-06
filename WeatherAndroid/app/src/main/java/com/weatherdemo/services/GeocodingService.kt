package com.weatherdemo.services

import android.location.Geocoder
import com.weatherdemo.models.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeocodingService(private val geocoder: Geocoder) {
    
    suspend fun geocode(cityName: String): City = withContext(Dispatchers.IO) {
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocationName(cityName, 1)
        
        if (addresses.isNullOrEmpty()) {
            throw GeocodingException("Location not found")
        }
        
        val address = addresses.first() ?: throw GeocodingException("Location not found")
        
        val name = address.locality ?: address.adminArea ?: cityName
        val latitude = address.latitude
        val longitude = address.longitude
        
        City(
            name = name,
            latitude = latitude,
            longitude = longitude
        )
    }
}

class GeocodingException(message: String) : Exception(message)

