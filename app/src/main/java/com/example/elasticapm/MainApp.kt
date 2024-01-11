package com.example.elasticapm

import android.R.attr.resource
import android.app.Application
import android.util.Log
import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.connectivity.ExportProtocol;
import co.elastic.apm.android.sdk.ElasticApmConfiguration
import co.elastic.apm.android.sdk.connectivity.opentelemetry.SignalConfiguration
import co.elastic.apm.android.sdk.features.persistence.PersistenceConfiguration
import co.elastic.apm.android.sdk.features.persistence.scheduler.ExportScheduler
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader
import org.slf4j.LoggerFactory
import java.util.Properties
import java.util.Timer
import java.util.TimerTask


class MainApp : Application() {
    private val USE_OTLP_HTTP = false
    private val log = LoggerFactory.getLogger("MainApp")
    override fun onCreate() {
        super.onCreate()

        val apmConfigurationBuilder = ElasticApmConfiguration.builder()

        if (USE_OTLP_HTTP) {
            apmConfigurationBuilder.setExportProtocol(ExportProtocol.HTTP);
            Log.i("APM", "Elastic APM initialized with protobuf/http1.1 exporters")
        }

        // enable persistence
        val persistenceConfiguration = PersistenceConfiguration.builder()
            .setEnabled(true)
            .setMaxCacheSize(60 * 1024 * 1024)
            .setExportScheduler(ExportScheduler.getDefault((5 * 1000).toLong()))
            .build()

        apmConfigurationBuilder.setPersistenceConfiguration(persistenceConfiguration)
        val apmConfiguration = apmConfigurationBuilder.build()

        ElasticApmAgent.initialize(this, apmConfiguration)
        // install logback logging hook
        val sdk = GlobalOpenTelemetry.get()
        OpenTelemetryAppender.install(sdk);

        // manual metrics
        val meter = GlobalOpenTelemetry.getMeter("MainApp")
        val timerCount = meter.counterBuilder("timer_count").setUnit("1").build()

        val delay = 1000
        val period = 5000
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timerCount.add(1)
            }
        }, delay.toLong(), period.toLong())

    }
}