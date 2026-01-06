package com.weatherdemo.services

import com.weatherdemo.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WeatherApi {
    @GET("points/{latitude},{longitude}")
    suspend fun getGridPoints(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): PointsResponse

    @GET
    suspend fun getForecast(@Url url: String): ForecastResponse

    @GET("gridpoints/{gridId}/{gridX},{gridY}/stations")
    suspend fun getStations(
        @Path("gridId") gridId: String,
        @Path("gridX") gridX: Int,
        @Path("gridY") gridY: Int
    ): StationsResponse

    @GET("stations/{stationId}/observations/latest")
    suspend fun getLatestObservation(
        @Path("stationId") stationId: String
    ): ObservationResponse
}

