package com.example.elasticapm.weather.openmeteo

import retrofit2.http.GET
import retrofit2.http.Query

// https://geocoding-api.open-meteo.com/v1/search?name=Chicago&count=10&language=en&format=json
interface GeoService {
    @GET("v1/search?count=1&language=en&format=json")
    suspend fun getLocation(
        @Query("name") cityName: String
    ): LocationsResponse
}