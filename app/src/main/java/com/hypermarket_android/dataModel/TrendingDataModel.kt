package com.hypermarket_android.dataModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TrendingDataModel(var product_list: ProductListDataModel):Parcelable {


    @Parcelize
    data class ProductListDataModel(var current_page: String, var data: ArrayList<ProductData>) :
        Parcelable {

        @Parcelize
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
            var currentPage:String?=null
        ) : Parcelable {

        }
    }
}

@Parcelize
data class RandomProductDataModel(var product_list: ArrayList<ProductData>):Parcelable {


//    @Parcelize
//    data class ProductListDataModel(var current_page: String, var data: ArrayList<ProductData>) :
//        Parcelable {

        @Parcelize
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
            var currentPage:String?=null
        ) : Parcelable {

        }
//    }
}