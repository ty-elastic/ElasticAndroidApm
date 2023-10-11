package com.example.elasticapm

import android.app.Application
import android.util.Log

import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.ElasticApmConfiguration
import co.elastic.apm.android.sdk.connectivity.opentelemetry.SignalConfiguration
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector
import java.util.Properties

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val propertiesFileInputStream = this.getAssets().open("co_elastic_apm_android.properties")
        var properties = Properties()
        properties.load(propertiesFileInputStream)
        val serviceUrl = properties.getProperty("server.url")
        var secretToken: String? = null
        if (properties.containsKey("server.secret_token"))
            secretToken = properties.getProperty("server.secret_token")

        val httpMetricExporterBuilder = OtlpHttpMetricExporter.builder()
            .setEndpoint("$serviceUrl/v1/metrics")
            .setAggregationTemporalitySelector(AggregationTemporalitySelector.deltaPreferred())
        if (secretToken != null)
            httpMetricExporterBuilder.addHeader("Authorization", "Bearer $secretToken")
        val httpMetricExporter = httpMetricExporterBuilder.build()

        val httpLogExporterBuilder = OtlpHttpLogRecordExporter.builder()
            .setEndpoint("$serviceUrl/v1/logs")
        if (secretToken != null)
            httpLogExporterBuilder.addHeader("Authorization", "Bearer $secretToken")
        val httpLogExporter = httpLogExporterBuilder.build()

        val httpSpanExporterBuilder = OtlpHttpSpanExporter.builder()
            .setEndpoint("$serviceUrl/v1/traces")
        if (secretToken != null)
            httpSpanExporterBuilder.addHeader("Authorization", "Bearer $secretToken")
        val httpSpanExporter = httpSpanExporterBuilder.build()

        val serviceConfiguration = ElasticApmConfiguration.builder().setSignalConfiguration(
            SignalConfiguration.custom(httpSpanExporter, httpLogExporter, httpMetricExporter)
        ).build()
        ElasticApmAgent.initialize(this, serviceConfiguration)
        Log.i("APM", "Elastic APM initialized with protobuf/http1.1 exporters")
    }
}