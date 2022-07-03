package com.hypermarket_android.nestedrecycler.model

class ChildItem(childItemTitle: String?) {
    private var ChildItemTitle: String? = childItemTitle

    // Constructor of the class
    // to initialize the variable*
    fun ChildItem(childItemTitle: String?) {
        ChildItemTitle = childItemTitle
    }

    // Getter and Setter method
    // for the parameter
    fun getChildItemTitle(): String? {
        return ChildItemTitle
    }

    fun setChildItemTitle(
        childItemTitle: String?
    ) {
        ChildItemTitle = childItemTitle
    }
}