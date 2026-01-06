package com.weatherdemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.weatherdemo.models.WeatherInfo
import com.weatherdemo.utilities.TemperatureConverter
import com.weatherdemo.utilities.WeatherEmoji

@Composable
fun WeatherRow(weather: WeatherInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(
                text = weather.city.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            weather.city.state?.let { state ->
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = state,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        val configuration = LocalConfiguration.current
        val locale = configuration.locales[0]
        
        weather.currentTemperature?.let { temp ->
            val currentEmoji = WeatherEmoji.emoji(weather.currentCondition)
            val conditionText = weather.currentCondition?.takeIf { it.isNotEmpty() }
                ?.let { " - $it" } ?: ""
            Text(
                text = "$currentEmoji Current: ${TemperatureConverter.formatTemperature(temp, locale)}$conditionText",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        weather.forecast.firstOrNull()?.let { firstForecast ->
            val forecastEmoji = WeatherEmoji.emoji(firstForecast.shortForecast)
            Text(
                text = "$forecastEmoji ${firstForecast.name}: ${TemperatureConverter.formatTemperature(firstForecast.temperature, locale)} - ${firstForecast.shortForecast}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

