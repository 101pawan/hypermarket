package com.hypermarket_android.dataModel

data class HomeSearchResponse(
    val message: String,
    val product_list: ProductList,
    val categories: ProductList,
    val status: Int
)

data class ProductList(
    val current_page: Int?,
    val `data`: List<UserData>?,
    val first_page_url: String?,
    val from: Int?,
    val last_page: Int?,
    val last_page_url: String?,
    val next_page_url: Any?,
    val path: String?,
    val per_page: Int?,
    val prev_page_url: Any?,
    val to: Int?,
    val total: Int?
)

data class UserData(
    val cart_id: Any?,
    val category_id: Int?,
    val created_at: String?,
    val currency: String?,
    val deal_id: Any?,
    val description: String?,
    val discount: Any?,
    val id: Int?,
    val is_added: Int?,
    val is_wishlist: Int?,
    val item_number: String?,
    val main_image: String?,
    val main_price: String?,
    val name: String?,
    val quantity: Int?,
    val rating: Int?,
    val selling_price: String?,
    val sold_out_qty: String?,
    val specification: Any?,
    val store_id: Int?,
    val subCategory_id: Int?,
    val updated_at: String?,
    val wishlist_id: Any?
)