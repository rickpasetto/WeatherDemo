package com.weatherdemo.models

import java.util.UUID

data class WeatherInfo(
    val id: String = UUID.randomUUID().toString(),
    val city: City,
    val currentTemperature: Int?,
    val currentCondition: String?,
    val forecast: List<ForecastPeriod>
)

data class ForecastPeriod(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val temperature: Int,
    val shortForecast: String
)

data class ForecastPeriodDTO(
    val name: String,
    val temperature: Int,
    val shortForecast: String
) {
    fun toForecastPeriod(): ForecastPeriod {
        return ForecastPeriod(name = name, temperature = temperature, shortForecast = shortForecast)
    }
}

data class PointsResponse(
    val properties: PointsProperties
)

data class PointsProperties(
    val gridId: String,
    val gridX: Int,
    val gridY: Int,
    val forecast: String
)

data class ForecastResponse(
    val properties: ForecastProperties
)

data class ForecastProperties(
    val periods: List<ForecastPeriodDTO>
) {
    val forecastPeriods: List<ForecastPeriod>
        get() = periods.map { it.toForecastPeriod() }
}

data class ObservationResponse(
    val properties: ObservationProperties?
)

data class ObservationProperties(
    val temperature: TemperatureValue?,
    val textDescription: String?
)

data class TemperatureValue(
    val value: Double?
)

data class StationsResponse(
    val features: List<StationFeature>
)

data class StationFeature(
    val properties: StationProperties
)

data class StationProperties(
    val stationIdentifier: String
)

