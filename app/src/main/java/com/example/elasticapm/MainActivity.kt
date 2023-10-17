package com.example.elasticapm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.elasticapm.ui.theme.ElasticAPMTheme
import com.example.elasticapm.weather.OpenMeteo
import io.opentelemetry.api.GlobalOpenTelemetry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private var citySelection: Spinner? = null
    private var getWeather: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_selection)

        // Creating an Array Adapter to populate the spinner with the data in the string resources
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this, R.array.city_array, android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        citySelection = findViewById(R.id.city_spinner)
        citySelection?.adapter = spinnerAdapter

        getWeather = findViewById(R.id.get_weather)
        getWeather?.setOnClickListener(View.OnClickListener { view: View? ->
            val selectedCity = citySelection?.selectedItem.toString()


            val intent = Intent(this@MainActivity, ResultsActivity::class.java)
            intent.putExtra("selectedCity", selectedCity)
            startActivity(intent)


        })
    }
}