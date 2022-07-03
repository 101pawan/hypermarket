package com.hypermarket_android.dataModel

data class SaverLaterDataModel(var save_later_count:Int,var savelater_list:ArrayList<SaverLaterData>) {


    data class SaverLaterData(
        var id: Int? = null,
        var user_id:Int?=null,
        var product_id: Int? = null,
        var store_id: Int? = null,
        var name: String? = null,
        var price: String? = null,
        var currency: String? = null,
        var quantity: Int? = null,
        var available_qty:Int?=null,
        var main_image: String? = null,
        var rating: Int? = null


    )
}