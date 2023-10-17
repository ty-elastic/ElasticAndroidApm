package com.example.elasticapm.weather.openmeteo

import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @SerializedName("results")
    val results: List<LocationResponse>
)