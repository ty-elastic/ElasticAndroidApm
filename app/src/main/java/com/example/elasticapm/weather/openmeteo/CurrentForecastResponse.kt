package com.example.elasticapm.weather.openmeteo

import com.google.gson.annotations.SerializedName

data class CurrentForecastResponse(
    @SerializedName("temperature_2m")
    val temperature2m: Float
)