package com.example.elasticapm.utils

import co.elastic.apm.android.sdk.logs.ElasticEvents
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.semconv.SemanticAttributes
import java.io.PrintWriter

import java.io.StringWriter

class ExceptionReporter {
    /* the Elastic APM Agent automatically catches uncaught exceptions. This code exists
    to synthetically generate exceptions (for APM testing) without causing the app to exit
     */
    companion object {
        fun emit(e: Throwable) {
            ElasticEvents.crashReporter().emit(
                "crash", Attributes.builder()
                    .put(SemanticAttributes.EXCEPTION_MESSAGE, e.message)
                    .put(SemanticAttributes.EXCEPTION_STACKTRACE, stackTraceToString(e))
                    .put(SemanticAttributes.EXCEPTION_TYPE, e.javaClass.name)
                    .build()
            );
        }

        private fun stackTraceToString(throwable: Throwable): String? {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            throwable.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
    }
}