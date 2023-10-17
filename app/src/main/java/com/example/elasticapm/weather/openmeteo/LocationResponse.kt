package com.example.elasticapm.weather.openmeteo

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float,
)
