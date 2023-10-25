package com.example.elasticapm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import com.example.elasticapm.utils.ExceptionReporter

class MainActivity : ComponentActivity() {
    private val HEADLESS = false

    private lateinit var getWeather: Button
    private lateinit var citySelection: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_selection)

        citySelection = findViewById(R.id.city_spinner)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this, R.array.city_array, R.layout.spinner_item
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        citySelection.adapter = spinnerAdapter

        getWeather = findViewById(R.id.get_weather)
        getWeather.setOnClickListener(View.OnClickListener { _: View? ->
            val selectedCity = citySelection.selectedItem.toString()
            if (selectedCity == "Unsupported") {
                try {
                    throw Exception("unknown city")
                } catch (e: Exception) {
                    ExceptionReporter.emit(e)
                }
            }
            else {
                val intent = Intent(this@MainActivity, ResultsActivity::class.java)
                // pass city selection to weather intent
                intent.putExtra("selectedCity", selectedCity)
                //startActivity(intent)
            }
        })

        if (HEADLESS) {
            val delay = 1000
            val period = 60000
            val timer = Timer()
            val rand = Random()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        citySelection.setSelection(rand.nextInt(resources.getStringArray(R.array.city_array).size - 1))
                        getWeather.performClick()
                    }
                }
            }, delay.toLong(), period.toLong())
        }
    }
}