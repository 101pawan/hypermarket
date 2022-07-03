package com.hypermarket_android.dataModel

data class DealsProductDataModel( var product_list: ArrayList<ProductData>) {

        data class ProductData(
            var id: Int?=null,
            var category_id: Int?=null,
            var subCategory_id: Int?=null,
            var store_id: Int?=null,
            var cart_id:Int?=null,
            var name: String?=null,
            var main_price: String?=null,
            var selling_price: String?=null,
            var currency: String?=null,
            var item_number: String?=null,
            var quantity: Int?=null,
            var description: String?=null,
            var main_image: String?=null,
            var specification: String?=null,
            var rating: Int?=null,
            var is_wishlist: Int?=null,
            var is_added:Int?=null,
            var currentPage:String?=null,
            var deal_name: String? = null,
            var deal_remain: String? = null,
            var deal_start: String? = null,
            var discount: String? = null,
            var deal_id:Int?=null
        )
}