package com.example.elasticapm.shop.hipster

import com.google.gson.annotations.SerializedName

/*
{
    "id": "OLJCESPC7Z",
    "name": "Vintage Typewriter",
    "description": "This typewriter looks good in your living room.",
    "picture": "/static/img/products/typewriter.jpg",
    "price_usd": {
        "currency_code": "",
        "units": "67",
        "nanos": 990000000
    },
    "categories": [
        "Vintage"
    ]
},
 */

data class ProductResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("price_usd")
    val priceUS: PriceUS,
    @SerializedName("categories")
    val categories: List<String>,
)