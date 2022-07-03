package com.hypermarket_android.eventPojos

class ProductShortEventPojo(private var isShort: Boolean, private var sort: String) {

    fun isShort(): Boolean? {
        return isShort
    }

    fun getSort(): String {
        return sort

    }
}
