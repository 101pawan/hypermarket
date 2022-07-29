package com.hypermarket_android.dataModel

data class DealsDataModel(var deals:ArrayList<DealsData>) {

    data class DealsData(var id:Int,var deal_name:String,var deal_image:String)
}