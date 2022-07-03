package com.hypermarket_android

import com.hypermarket_android.dataModel.OrderListResponse

object Singleton{

    init {
        println("Singleton class invoked.")
    }
    var variableName = "I am Var"
    fun printVarName(){
        println(variableName)
    }
    var orderData: OrderListResponse.OrderData? = null
    var allOrderData: ArrayList<OrderListResponse.OrderData>? = null
    var allPastOrderData: ArrayList<OrderListResponse.OrderData>? = null
}