package com.hypermarket_android.eventPojos

class SelectCartEventPojo (private var isSelectCart: Boolean,var isApiHit:Boolean){

    fun isSelectCart(): Boolean? {
        return isSelectCart
    }

    fun isApiHit(): Boolean? {
        return isApiHit
    }
}
