package com.hypermarket_android.dataModel

data class CouponListResponse(
    val message: String,
    val response: ArrayList<CouponModel>
) {
    data class CouponModel(
        val id: String? = null,
        val coupon_code: String? = null,
        val store_id: String? = null,
        val category_id: String? = null,
        val product_id: String? = null,
        val start_date: String? = null,
        val end_date: String? = null,
        val status: String? = null,
        val coupon_type: String? = null,
        val discount: String? = null,
        val description: String? = null,
        val created_at: String? = null,
        val coupon_valid_to: String? = null,
        val coupon_text: String? = null,
        val updated_at: String? = null
    )


}