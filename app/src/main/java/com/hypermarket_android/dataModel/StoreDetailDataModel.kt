package com.hypermarket_android.dataModel

data class StoreDetailDataModel(var store_detail: StoreDetailData) {

    data class StoreDetailData(
        var id: Int,
        var name: String,
        var country_code: String,
        var mobile_number: String,
        var country:String,
        var city:String,
        var address:String,
        var image:String,
        var rating:String,
        var about:String

    )
}