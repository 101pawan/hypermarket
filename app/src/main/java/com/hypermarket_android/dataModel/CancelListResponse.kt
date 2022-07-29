package com.hypermarket_android.dataModel

data class CancelListResponse(
    val message: String,
    val data: ArrayList<CancelModel>
) {
    data class CancelModel(
        val id: String,
        val created_at: String?,
        val cancel_reason: String?,
        val updated_at: String?
    )


}