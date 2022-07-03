package com.hypermarket_android.ui.downloadInvoice

data class DownloadInvoiceResponse(
    val `data`: List<Data>,
    val message: String
)

data class Data(
    val address_id: Int,
    val coupon_cart_value: Any,
    val coupon_code: Any,
    val coupon_desc: Any,
    val coupon_discount: Any,
    val coupon_id: Int,
    val created_at: String,
    val delivery_charge: String,
    val expected_delivery: String,
    val id: Int,
    val is_invoice: Int,
    val order_date: String,
    val order_id: String,
    val order_status: String,
    val payment_mode: Int,
    val payment_status: Int,
    val product_id: Any,
    val product_name: String,
    val productrating: Any,
    val qty: Any,
    val reason_id: Any,
    val redeemAmount: Int,
    val shipping_detail: ShippingDetail,
    val status: String,
    val status_date: String,
    val store_id: Int,
    val tax_charge: Any,
    val total_amount: Any,
    val total_discount: Any,
    val total_payable_amount: String,
    val updated_at: String,
    val user_id: Int,
    val user_name: UserName
)

data class ShippingDetail(
    val address_id: Int,
    val address_type: Any,
    val alt_country_code: Any,
    val alt_mobile_number: String,
    val building_name: String,
    val city_name: String,
    val country_code: Any,
    val country_name: String,
    val created_at: String,
    val house_number: Any,
    val id: Int,
    val mobile_number: String,
    val name: String,
    val order_id: String,
    val street: String,
    val updated_at: String
)

data class UserName(
    val access_token: String,
    val country_code: String,
    val country_name: String,
    val created_at: String,
    val device_token: String,
    val device_type: Int,
    val email: String,
    val id: Int,
    val image: String,
    val is_block: Int,
    val is_register: Int,
    val mobile_number: String,
    val name: String,
    val otp: Any,
    val otp_verified: Int,
    val otp_verify_time: Any,
    val referCode: String,
    val shipping_address: Any,
    val social_id: Any,
    val social_type: Any,
    val updated_at: String
)