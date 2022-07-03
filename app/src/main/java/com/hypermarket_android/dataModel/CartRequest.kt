package com.hypermarket_android.dataModel

data class CartRequest (
    var product_id: String,
    var store_id: String,
    var quantity: String,
    var barcode_id: String,
    var cart_id: String
        )