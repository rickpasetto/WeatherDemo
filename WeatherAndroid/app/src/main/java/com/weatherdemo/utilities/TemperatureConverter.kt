package com.weatherdemo.utilities

import androidx.core.text.util.LocalePreferences
import java.text.NumberFormat
import java.util.Locale

object TemperatureConverter {
    fun celsiusToFahrenheit(celsius: Int): Double {
        return celsius * 9.0 / 5.0 + 32.0
    }
    
    fun formatTemperature(celsius: Int, locale: Locale): String {
        val usesFahrenheit = getUsesFahrenheit(locale)
        
        val temperature = if (usesFahrenheit) {
            celsiusToFahrenheit(celsius)
        } else {
            celsius.toDouble()
        }
        
        val formatter = NumberFormat.getNumberInstance(locale)
        formatter.maximumFractionDigits = 0
        val unit = if (usesFahrenheit) "°F" else "°C"
        
        return "${formatter.format(temperature)}$unit"
    }
    
    private fun getUsesFahrenheit(locale: Locale): Boolean {
        return try {
            val temperatureUnit = LocalePreferences.getTemperatureUnit()
            temperatureUnit == LocalePreferences.TemperatureUnit.FAHRENHEIT
        } catch (e: Exception) {
            locale.country in fahrenheitCountries
        }
    }
    
    private val fahrenheitCountries = setOf(
        "US", // United States
        "BS", // Bahamas
        "BZ", // Belize
        "KY", // Cayman Islands
        "PW", // Palau
        "FM", // Federated States of Micronesia
        "MH"  // Marshall Islands
    )
}

