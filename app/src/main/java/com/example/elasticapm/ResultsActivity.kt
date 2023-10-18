package com.example.elasticapm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.elasticapm.weather.OpenMeteo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ResultsActivity : ComponentActivity() {
    private lateinit var cityName: TextView
    private lateinit var temperature: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather)

        val intent: Intent = intent
        val selectedCity = intent.getStringExtra("selectedCity")

        cityName = findViewById(R.id.city_name)
        temperature = findViewById(R.id.temperature)

        backButton = findViewById(R.id.back_button)
        backButton!!.setOnClickListener { view: View? -> finish() }

        val serviceManager = OpenMeteo()
        runBlocking {
            withContext(Dispatchers.IO) {
                val forecast = serviceManager.getCurrentCityWeather(selectedCity as String)
                cityName.text = selectedCity
                temperature.text = "${Math.round(forecast.current.temperature2m).toInt()}°F"
            }
        }
    }



}