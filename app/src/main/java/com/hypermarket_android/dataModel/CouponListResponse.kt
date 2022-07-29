package com.hypermarket_android.dataModel

data class CouponListResponse(
    val message: String,
    val response: ArrayList<CouponModel>
) {
    data class CouponModel(
        val id: String,
        val coupon_code: String,
        val store_id: String,
        val category_id: String,
        val product_id: String,
        val start_date: String,
        val end_date: String,
        val status: Int,
        val discount: String,
        val description: String,
        val created_at: String?,
        val coupon_valid_to: String?,
        val coupon_text: String?,
        val updated_at: String?
    )


}