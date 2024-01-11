package com.example.elasticapm.shop

import android.content.Context
import android.util.Log
import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.traces.ElasticTracers
import com.example.elasticapm.R
import com.example.elasticapm.shop.hipster.ProductService
import com.example.elasticapm.utils.BasicAuthInterceptor
import io.opentelemetry.api.GlobalOpenTelemetry
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HipsterService(context: Context) {
    private val log = LoggerFactory.getLogger("HipsterShop")

    private val productService: ProductService by lazy {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(
                BasicAuthInterceptor(
                    context.getString(R.string.hipster_user),
                    context.getString(R.string.hipster_password)
                )
            ).build()

        val productService = Retrofit.Builder()
            .baseUrl(context.getString(R.string.hipster_url))
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        productService.create(ProductService::class.java)
    }

    suspend fun getProducts() {
//        var okHttpTracer = ElasticTracers.okhttp();
//        val span = okHttpTracer.spanBuilder("getProducts").startSpan()
//        val scope = span.makeCurrent()

        val products = productService.getProducts()
        log.warn("iterated " + products.size + " products in catalog")

//        scope.close()
    }

    suspend fun generateException() {
        val products = productService.getUnknown()
    }
}