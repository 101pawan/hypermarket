package com.hypermarket_android.dataModel


data class StateResponse(
    var data: List<State>,
    var message: String,
    var status: Int
) {
    data class State(var id: Int, var country_id: Int, var state_name: String)
}
