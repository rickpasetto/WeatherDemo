package com.weatherdemo.services

import com.weatherdemo.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class WeatherService {
    private val baseURL = "https://api.weather.gov"
    
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "WeatherApp (Android)")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val api = retrofit.create<WeatherApi>()
    
    suspend fun fetchWeather(city: City): WeatherInfo {
        val points = api.getGridPoints(city.latitude, city.longitude)
        val forecastUrl = points.properties.forecast
        val forecast = api.getForecast(forecastUrl)
        
        val currentWeather = try {
            fetchCurrentWeather(
                gridId = points.properties.gridId,
                gridX = points.properties.gridX,
                gridY = points.properties.gridY
            )
        } catch (e: Exception) {
            null
        }
        
        return WeatherInfo(
            city = city,
            currentTemperature = currentWeather?.temperature,
            currentCondition = currentWeather?.condition,
            forecast = forecast.properties.forecastPeriods
        )
    }
    
    private suspend fun fetchCurrentWeather(
        gridId: String,
        gridX: Int,
        gridY: Int
    ): CurrentWeather? {
        val stations = api.getStations(gridId, gridX, gridY)
        val stationId = stations.features.firstOrNull()?.properties?.stationIdentifier ?: return null
        
        val observation = api.getLatestObservation(stationId)
        val temperature = observation.properties?.temperature?.value?.toInt()
        val condition = observation.properties?.textDescription
        
        return CurrentWeather(temperature = temperature, condition = condition)
    }
    
    private data class CurrentWeather(
        val temperature: Int?,
        val condition: String?
    )
}

