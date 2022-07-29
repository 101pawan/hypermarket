package com.hypermarket_android.dataModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoriesDataModel(

    var categories: List<categoryModel>? = null
) : Parcelable {


    @Parcelize
    data class categoryModel(
        var id: Int? = null,
        var name: String? = null,
        var parent_id: Int? = null,
        var image: String? = null,
        var subCategory: ArrayList<SubCategoryModel>? = null
    ) : Parcelable

    @Parcelize
    data class SubCategoryModel(
        var id: Int? = null,
        var name: String? = null,
        var parent_id: Int? = null,
        var image: String? = null
    ) : Parcelable {

    }
}