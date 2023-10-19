package com.example.elasticapm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.semconv.SemanticAttributes

class MainActivity : ComponentActivity() {

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
            Log.i("test","" + selectedCity)
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
                startActivity(intent)
            }
        })
    }
}