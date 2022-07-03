package com.hypermarket_android.dataModel

data class DeliverOrderStatusResponseList(
    val result: ArrayList<DeliveryOrderStatusData>
) {
    data class DeliveryOrderStatusData(
        val id: String?,
        val order_status: String?

    )
}