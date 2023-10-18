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
    private var city_name: TextView? = null
    private var temperature: TextView? = null
    private var back_button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather)
        val intent: Intent = getIntent()
        val selectedCity = intent.getStringExtra("selectedCity")
        city_name = findViewById(R.id.city_name)
        temperature = findViewById(R.id.temperature)
        back_button = findViewById(R.id.back_button)

        back_button!!.setOnClickListener { view: View? -> finish() }

        val serviceManager = OpenMeteo()
        runBlocking {
            withContext(Dispatchers.IO) {
                val forecast = serviceManager.getCurrentCityWeather(selectedCity as String)
                city_name?.setText(selectedCity)
                temperature?.setText(Math.round(forecast.current.temperature2m).toInt().toString() + "°F")
            }
        }
    }



}