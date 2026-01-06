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
        val state = extractStateAbbreviation(address)
        val latitude = address.latitude
        val longitude = address.longitude
        
        City(
            name = name,
            state = state,
            latitude = latitude,
            longitude = longitude
        )
    }
    
    private fun extractStateAbbreviation(address: android.location.Address): String? {
        val state = address.adminArea ?: return null
        return StateAbbreviationMapper.abbreviation(state)
    }
}

object StateAbbreviationMapper {
    private val stateMap = mapOf(
        "Alabama" to "AL", "Alaska" to "AK", "Arizona" to "AZ", "Arkansas" to "AR",
        "California" to "CA", "Colorado" to "CO", "Connecticut" to "CT", "Delaware" to "DE",
        "Florida" to "FL", "Georgia" to "GA", "Hawaii" to "HI", "Idaho" to "ID",
        "Illinois" to "IL", "Indiana" to "IN", "Iowa" to "IA", "Kansas" to "KS",
        "Kentucky" to "KY", "Louisiana" to "LA", "Maine" to "ME", "Maryland" to "MD",
        "Massachusetts" to "MA", "Michigan" to "MI", "Minnesota" to "MN", "Mississippi" to "MS",
        "Missouri" to "MO", "Montana" to "MT", "Nebraska" to "NE", "Nevada" to "NV",
        "New Hampshire" to "NH", "New Jersey" to "NJ", "New Mexico" to "NM", "New York" to "NY",
        "North Carolina" to "NC", "North Dakota" to "ND", "Ohio" to "OH", "Oklahoma" to "OK",
        "Oregon" to "OR", "Pennsylvania" to "PA", "Rhode Island" to "RI", "South Carolina" to "SC",
        "South Dakota" to "SD", "Tennessee" to "TN", "Texas" to "TX", "Utah" to "UT",
        "Vermont" to "VT", "Virginia" to "VA", "Washington" to "WA", "West Virginia" to "WV",
        "Wisconsin" to "WI", "Wyoming" to "WY", "District of Columbia" to "DC"
    )
    
    fun abbreviation(stateName: String): String? {
        return stateMap[stateName] ?: stateName
    }
}

class GeocodingException(message: String) : Exception(message)

