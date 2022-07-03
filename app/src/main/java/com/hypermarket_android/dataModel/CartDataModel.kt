package com.hypermarket_android.dataModel

data class CartDataModel(var cart_list: ArrayList<CartData>? = null) {

    data class CartData(var id: Int? = null,
                        var user_id: String? = null,
                        var product_id: Int? = null,
                        var barcode_id: String? = null,
                        var colored_images_id: String? = null,
                        var store_id: Int? = null,
                        var name: String? = null,
                        var price: String? = null,
                        var available_qty: Int? = null,
                        var current_inventory: Int? = null,
                        var currency: String? = null,
                        var quantity: Int? = null,
                        var image: String? = null,
                        var is_wishlist:Int?=null,
                        var is_added:Int?=null,
                        var cart_id:Int?=null,
                        var rating: Int? = null){

    }
}