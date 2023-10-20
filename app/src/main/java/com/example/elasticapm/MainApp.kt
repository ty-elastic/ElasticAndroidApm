package com.example.elasticapm

import android.app.Application
import android.util.Log
import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.ElasticApmConfiguration
import co.elastic.apm.android.sdk.connectivity.opentelemetry.SignalConfiguration
import co.elastic.apm.android.sdk.features.persistence.PersistenceConfiguration
import co.elastic.apm.android.sdk.features.persistence.scheduler.ExportScheduler
import com.example.elasticapm.shop.HipsterService
import com.example.elasticapm.weather.OpenMeteo
import com.instacart.library.truetime.TrueTime
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.samplers.Sampler
import io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.Properties
import java.util.Timer
import java.util.TimerTask


class MainApp : Application() {
    private val USE_OTLP_HTTP = false
    private val USE_PERSISTENCE = false
    private val log = LoggerFactory.getLogger("MainApp")
    override fun onCreate() {
        super.onCreate()

        val apmConfigurationBuilder = ElasticApmConfiguration.builder()

        if (USE_OTLP_HTTP) {
            // read properties
            val propertiesFileInputStream =
                this.getAssets().open("co_elastic_apm_android.properties")
            var properties = Properties()
            properties.load(propertiesFileInputStream)
            val serviceUrl = properties.getProperty("server.url")
            var authHeader: String? = null
            if (properties.containsKey("server.secret_token"))
                authHeader = "Bearer " + properties.getProperty("server.secret_token")
            else if (properties.containsKey("server.api_key"))
                authHeader = "ApiKey " + properties.getProperty("server.api_key")

            // setup otlp/http exporters
            val httpMetricExporterBuilder = OtlpHttpMetricExporter.builder()
                .setEndpoint("$serviceUrl/v1/metrics")
                .setAggregationTemporalitySelector(AggregationTemporalitySelector.deltaPreferred())
            if (authHeader != null)
                httpMetricExporterBuilder.addHeader("Authorization", authHeader)
            val httpMetricExporter = httpMetricExporterBuilder.build()

            val httpLogExporterBuilder = OtlpHttpLogRecordExporter.builder()
                .setEndpoint("$serviceUrl/v1/logs")
            if (authHeader != null)
                httpLogExporterBuilder.addHeader("Authorization", authHeader)
            val httpLogExporter = httpLogExporterBuilder.build()

            val httpSpanExporterBuilder = OtlpHttpSpanExporter.builder()
                .setEndpoint("$serviceUrl/v1/traces")
            if (authHeader != null)
                httpSpanExporterBuilder.addHeader("Authorization", authHeader)
            val httpSpanExporter = httpSpanExporterBuilder.build()

            // configure
            apmConfigurationBuilder
                .setSignalConfiguration(
                    SignalConfiguration.custom(httpSpanExporter, httpLogExporter, httpMetricExporter)
                )
            Log.i("APM", "Elastic APM initialized with protobuf/http1.1 exporters")
        }

        // enable persistence
        if (USE_PERSISTENCE) {
            val persistenceConfiguration = PersistenceConfiguration.builder()
                .setEnabled(true)
                .setMaxCacheSize(60 * 1024 * 1024)
                .setExportScheduler(ExportScheduler.getDefault((5 * 1000).toLong()))
                .build()
            apmConfigurationBuilder.setPersistenceConfiguration(persistenceConfiguration)
        }

        val apmAgent: ElasticApmAgent
        if (USE_OTLP_HTTP || USE_PERSISTENCE) {
            val apmConfiguration = apmConfigurationBuilder.build()
            apmAgent = ElasticApmAgent.initialize(this, apmConfiguration)
        }
        else
            apmAgent = ElasticApmAgent.initialize(this)
        // install logback logging hook
        val sdk = GlobalOpenTelemetry.get()
        OpenTelemetryAppender.install(sdk);

        TrueTime.clearCachedInfo()

        // manual metrics
        val meter = GlobalOpenTelemetry.getMeter("MainApp")
        val timerCount = meter.counterBuilder("timer_count").setUnit("1").build()

        val delay = 1000
        val period = 10000
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timerCount.add(1)

                runBlocking {
                    try {
                        val hipsterService = HipsterService(applicationContext)
                        hipsterService.getProducts()
                    }
                    catch (e: Exception) {
                        ExceptionReporter.emit(e)
                    }
                }
            }
        }, delay.toLong(), period.toLong())
    }
}