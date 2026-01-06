package com.weatherdemo.models

import java.util.UUID

data class City(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val latitude: Double,
    val longitude: Double
)
