package com.example.elasticapm

import android.app.Application
import android.util.Log
import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.connectivity.ExportProtocol;
import co.elastic.apm.android.sdk.ElasticApmConfiguration
import co.elastic.apm.android.sdk.connectivity.opentelemetry.SignalConfiguration
import co.elastic.apm.android.sdk.features.persistence.PersistenceConfiguration
import co.elastic.apm.android.sdk.features.persistence.scheduler.ExportScheduler
import co.elastic.apm.android.truetime.TrueTime
import com.example.elasticapm.shop.HipsterService
import com.example.elasticapm.utils.ExceptionReporter
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.Properties
import java.util.Timer
import java.util.TimerTask


class MainApp : Application() {
    private val HEADLESS_SHOP = false

    private val USE_OTLP_HTTP = false
    private val USE_PERSISTENCE = false
    private val log = LoggerFactory.getLogger("MainApp")
    override fun onCreate() {
        super.onCreate()

        val apmConfigurationBuilder = ElasticApmConfiguration.builder()

        if (USE_OTLP_HTTP) {
            apmConfigurationBuilder.setExportProtocol(ExportProtocol.HTTP);
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

                if (HEADLESS_SHOP) {
                    runBlocking {
                        try {
                            val hipsterService = HipsterService(applicationContext)
                            hipsterService.getProducts()
                        } catch (e: Exception) {
                            ExceptionReporter.emit(e)
                        }
                    }
                }
            }
        }, delay.toLong(), period.toLong())
    }
}