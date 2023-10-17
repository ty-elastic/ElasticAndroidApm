package com.example.elasticapm.weather

import com.example.elasticapm.weather.openmeteo.ForecastResponse
import com.example.elasticapm.weather.openmeteo.ForecastService
import com.example.elasticapm.weather.openmeteo.GeoService
import io.opentelemetry.api.GlobalOpenTelemetry
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenMeteo {
    val log = LoggerFactory.getLogger("OpenMeteo")

    private val forcecastService: ForecastService by lazy {
        val forcecastService = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        forcecastService.create(ForecastService::class.java)
    }

    private val geoService: GeoService by lazy {
        val forcecastService = Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        forcecastService.create(GeoService::class.java)
    }

    suspend fun getCurrentCityWeather(city: String): ForecastResponse {
        val tracer = GlobalOpenTelemetry.getTracer("MainApp")
        val span = tracer.spanBuilder("getCurrentCityWeather").startSpan()
        val scope = span.makeCurrent()

        val location = geoService.getLocation(city)
        log.warn("location=" + location.results[0].latitude + "," + location.results[0].longitude)
        val forecast = forcecastService.getForecast(location.results[0].latitude, location.results[0].longitude)
        log.warn("forecast=" + forecast.current.temperature2m)

        scope.close()
        span.end()

        return forecast
    }
}