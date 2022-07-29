package com.hypermarket_android.dataModel
enum class OrderTracType { despatch , shipping , OFD , Delivery }
data class AddOrderResponse(
    val message: String,
    val order_summary: OrderSummary
) {
    data class OrderSummary(
        val id: String,
        val order_id: String,
        val user_id: String,
        val store_id: Int,
        val product_id: String,
        val qty: Int,
        val total_amount: String?,
        val expected_delivery: Long,
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
        val is_invoice: String?,
        val productrating: String?,
        val order_products: ArrayList<OrderProduct>?,
        val order_status_tracking: ArrayList<OrderStatusTracking>?,
        val shipping_detail: ShippingDetail?,
    )

    data class OrderProduct(
        val id: String?,
        val order_id: String?,
        val product_id: String?,
        val product_qty: String?,
        val product_price: String?,
        val product_name: ArrayList<OrderListResponse.ProductData>?,
        val rating: String?

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

        );


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

    data class OrderStatusTracking(
        val status: String?,
        val status_date: String?,
        val order_status: String?,
    )
}