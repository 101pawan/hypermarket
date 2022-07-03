package com.hypermarket_android.dataModel


data class WishListDataModel(var wish_list: ArrayList<WishListData>) {
    data class WishListData(
        var id: Int? = null,
        var product_id: Int? = null,
        var wishlist_id: Int? = null,
        var cart_id: Int? = null,
        var name: String? = null,
        var main_price: String? = null,
        var selling_price: String? = null,
        var currency: String? = null,
        var item_number: String? = null,
        var quantity: Int? = null,
        var main_image: String? = null,
        var rating: Int? = null,
        var is_wishlist: Int? = null,
        var is_added: Int? = null
    )
}

