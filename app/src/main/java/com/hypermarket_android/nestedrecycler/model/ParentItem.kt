package com.hypermarket_android.nestedrecycler.model

import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.dataModel.TrendingDataModel
import java.util.ArrayList

class ParentItem(
    ParentItemTitle: ArrayList<CategoriesDataModel.SubCategoryModel>,
    ChildItemList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>?
) {
      var ParentItemTitle: ArrayList<CategoriesDataModel.SubCategoryModel>? = ParentItemTitle
      var ChildItemList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>? = ChildItemList



//    // Getter and Setter methods
//    // for each parameter
//    fun getParentItemTitle(): ArrayList<CategoriesDataModel.SubCategoryModel>? {
//        return ParentItemTitle
//    }
//
//    fun setParentItemTitle(
//        parentItemTitle: ArrayList<CategoriesDataModel.SubCategoryModel>?
//    ) {
//        ParentItemTitle = parentItemTitle
//    }
//
//    fun getChildItemList(): ArrayList<TrendingDataModel.ProductListDataModel.ProductData>? {
//        return ChildItemList
//    }
//
//    fun setChildItemList(
//        childItemList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>?
//    ) {
//        ChildItemList = childItemList
//    }
}