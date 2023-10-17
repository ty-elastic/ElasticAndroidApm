package com.example.elasticapm.weather.openmeteo

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("current")
    val current: CurrentForecastResponse
)