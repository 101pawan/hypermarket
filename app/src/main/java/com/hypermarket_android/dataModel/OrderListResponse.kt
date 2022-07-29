package com.hypermarket_android.dataModel

data class OrderListResponse(
    val data: ArrayList<OrderData>,
    val total_records: Int? = null,
    val current_page: Int? = null,
    val total_pages: Int? = null,

    ) {
    data class OrderData(
        val id: String,
        val order_id: String,
        val user_id: String,
        val store_id: Int,
        val product_id: String,
        val barcode_id: String,
        val qty: Int,
        val total_amount: String?,
        val expected_delivery: String?,
        val order_status: String,
        val total_discount: String?,
        val payment_mode: String,
        val payment_status: String,
        val total_payable_amount: String,
        val tax_charge: String?,
        val delivery_charge: String?,
        val created_at: String?,
        val address_id: String?,
        val updated_at: String?,
        val coupon_id: String?,
        val coupon_discount: String?,
        val coupon_code: String?,
        val coupon_desc: String?,
        val order_date: String?,
        val status: String?,
        val status_date: String?,
        val reason_id: String?,
        val productrating: String?,
//        val order_products: ArrayList<OrderProducts>?,
//        val product_name: ProductName?,
//        val user_name: UserName?,
//        val shipping_detail: ShippingDetail?
        var product_qty: String?,
        var product_image: String?,
        var available_qty: String?,
        var alt_country_code: String?,
        var alt_mobile_number: String?,
        var city_name: String?,
        var name: String?,
        var house_number: String?,
        var street: String?,
        var rating: String?,
        var building_name: String?,
        var product_name: String?,
        var product_price: String?,
        var main_image: String?,
        var mobile_number: String?,
        var returned: String?,
        var available_for_return: String?,
        var is_returnable: String?,
        var english_address: String?,
        var arabic_address: String?
    )


    data class OrderProducts(
        val id: String?,
        val order_id: String?,
        val product_id: String?,
        val product_price: String?,
        val total_payable_amount: String?,
        val product_name: ArrayList<ProductData>?,
        val rating: String?,
        val available_qty: String?,
        var barcode_id: String?,
        var product_qty: String?,
        var product_image: String?
        /* val store_id: String?,
         val deal_id: String?,
         val name: String?,
         val main_price: String?,
         val main_image: String?,
         val discount: String?,
         val currency: String?,
         val item_number: String?,
         val quantity: String?,
         val sold_out_qty: String?,
         val description: String?,
         val specification: String?,
         val created_at: String?,
         val updated_at: String?*/

    )

    data class ProductData(
        val id: String?,
        val category_id: String?,
        val subCategory_id: String?,
        val store_id: String?,
        val deal_id: String?,
        val name: String?,
        val main_price: String?,
        val main_image: String?,
        val discount: String?,
        val currency: String?,
        val item_number: String?,
        val quantity: String?,
        val sold_out_qty: String?,
        val description: String?,
        val imain_imaged: String?,
        val specification: String?,
        val created_at: String?,
        val updated_at: String?,

        )

    data class ProductName(
        val id: String?,
        val category_id: String?,
        val subCategory_id: String?,
        val store_id: String?,
        val deal_id: String?,
        val name: String?,
        val main_price: String?,
        val main_image: String?,
        val discount: String?,
        val currency: String?,
        val item_number: String?,
        val quantity: String?,
        val sold_out_qty: String?,
        val description: String?,
        val specification: String?,
        val created_at: String?,
        val updated_at: String?
    )

    data class UserName(
        val id: String?,
        val name: String?,
        val email: String?,
        val country_code: String?,
        val mobile_number: String?,
        val otp: String?,
        val image: String?,
        val country_name: String?,
        val shipping_address: String?,
        val otp_verified: String?,
        val otp_verify_time: String?,
        val is_register: String?,
        val is_block: String?,
        val access_token: String?,
        val device_type: String?,
        val social_type: String?,
        val social_id: String?,
        val created_at: String?,
        val updated_at: String?


    )

    data class ShippingDetail(
        val id: String?,
        val order_id: String?,
        val address_id: String?,
        val house_number: String?,
        val building_name: String?,
        val street: String?,
        val name: String?,
        val country_code: String?,
        val mobile_number: String?,
        val alt_country_code: String?,
        val alt_mobile_number: String?,
        val city_name: String?,
        val country_name: String?,
        val address_type: String?,
        val created_at: String?,
        val updated_at: String?
    )
}

data class GetOrdersList(
    val data: ArrayList<OrderData>,
    val total_records: Int? = null,
    val current_page: Int? = null,
    val total_pages: Int? = null,

    ) {
    data class OrderData(
        val order_id: String,
        val order_status: String,
        val total_payable_amount: String,
        val order_date: String?,
        val address: String?,
        var product_image: String?
    )
}