package com.hypermarket_android.dataModel

data class DeliveryOrderListResponse(
    val result: ArrayList<DeliveryOrderData>
) {
    data class DeliveryOrderData(
        val order_id: String?,
        val address: String?,
        val status: String?,
        val status_id: String?,
        val payment_mode: Int?,
        val total_payable_amount: String,
        val mobile_number: String,
        val alt_mobile_number: String,
        var replacement:Replacement?
    )
    data class Replacement(
        val product_name:String?,
        val product_image:String?,
        val product_qty:String?
    )
}

