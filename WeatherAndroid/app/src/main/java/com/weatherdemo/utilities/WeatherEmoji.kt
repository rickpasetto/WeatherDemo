package com.weatherdemo.utilities

object WeatherEmoji {
    fun emoji(condition: String?): String {
        val lowerCondition = condition?.lowercase() ?: return "🌤️"
        
        return when {
            lowerCondition.contains("sunny") || lowerCondition.contains("clear") -> "☀️"
            lowerCondition.contains("partly cloudy") -> "⛅"
            lowerCondition.contains("cloudy") || lowerCondition.contains("overcast") -> "☁️"
            lowerCondition.contains("rain") || lowerCondition.contains("shower") -> "🌧️"
            lowerCondition.contains("thunderstorm") || lowerCondition.contains("storm") -> "⛈️"
            lowerCondition.contains("snow") || lowerCondition.contains("sleet") -> "❄️"
            lowerCondition.contains("fog") || lowerCondition.contains("mist") -> "🌫️"
            lowerCondition.contains("wind") -> "💨"
            lowerCondition.contains("haze") -> "🌫️"
            else -> "🌤️"
        }
    }
}


