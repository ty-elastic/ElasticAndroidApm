package com.example.elasticapm.weather.openmeteo

import retrofit2.http.GET
import retrofit2.http.Query

// https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m&hourly=temperature_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&forecast_days=1
interface ForecastService {
    @GET("v1/forecast?current=temperature_2m&hourly=temperature_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&forecast_days=1")
    suspend fun getForecast(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float
    ): ForecastResponse
}