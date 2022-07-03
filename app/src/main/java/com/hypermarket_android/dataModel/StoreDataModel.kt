package com.hypermarket_android.dataModel

data class StoreDataModel(var store_list: ArrayList<StoreData>) {

    data class StoreData(
        var id: Int,
        var name: String,
        var country_code: String,
        var mobile_number: String,
        var country: String,
        var city: String,
        var address:String,
        var about:String,
        var is_assigned:Int,
        var image:String,
        var category_id:String,
        var rating:String,
        var isSelected: Boolean
    ) {

    }
}