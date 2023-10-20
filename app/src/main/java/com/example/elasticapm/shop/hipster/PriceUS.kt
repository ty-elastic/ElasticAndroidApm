package com.example.elasticapm.shop.hipster

import com.google.gson.annotations.SerializedName

/*
    "price_usd": {
        "currency_code": "",
        "units": "67",
        "nanos": 990000000
    },
 */
data class PriceUS(
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("units")
    val units: String,
    @SerializedName("nanos")
    val nanos: Float,
)
