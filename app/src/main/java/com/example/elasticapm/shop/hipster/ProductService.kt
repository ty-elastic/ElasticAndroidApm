package com.example.elasticapm.shop.hipster

import retrofit2.http.GET
import retrofit2.http.Query

// https://hipster.prod-3.eden.elastic.dev/api/product
interface ProductService {
    @GET("api/product")
    suspend fun getProducts(
    ): List<ProductResponse>

    @GET("api/unknown")
    suspend fun getUnknown(
    ): Void
}