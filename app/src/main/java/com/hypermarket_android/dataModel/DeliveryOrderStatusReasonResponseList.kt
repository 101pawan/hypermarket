package com.hypermarket_android.dataModel

data class DeliveryOrderStatusReasonResponseList(
    val result: ArrayList<DeliveryOrderStatusReasonData>
) {
    data class DeliveryOrderStatusReasonData(
        val id: String?,
        val cancel_reason: String?

    )
}