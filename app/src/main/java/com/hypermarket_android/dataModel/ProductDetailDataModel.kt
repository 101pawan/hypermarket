package com.hypermarket_android.dataModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProductDetailDataModel(
      var related_product: ArrayList<ProductData>? = null
    , var product_details: ProductData
//    , var colors: ArrayList<ColorData>? = null
) : Parcelable {

//    @Parcelize
//    data class ColorData(
//        var id: Int? = null,
//        var color_name: String? = null,
//        var arabic_name: String? = null,
//    )

    @Parcelize
    data class ProductData(
        var id: Int? = null,
        var category_id: Int? = null,
        var subCategory_id: Int? = null,
        var store_id: Int? = null,
        var cart_id:Int?=null,
        var name: String? = null,
        var main_price: String? = null,
        var selling_price: String? = null,
        var currency: String? = null,
        var item_number: String? = null,
        var quantity: Int? = null,
        var description: String? = null,
        var main_image: String? = null,
        var specification: String? = null,
        var rating: Int? = null,
        var is_wishlist: Int? = null,
        var is_added: Int? = null,
        var deal_name: String? = null,
        var deal_remaining: String? = null,
        var deal_desc: String? = null,
        var discount: String? = null,
        var images: ArrayList<ImageData>? = null,
        var measurement_unit:ArrayList<MesurementUnit>? = null
      ) : Parcelable {

        @Parcelize
        data class ImageData(var images: String? = null,var isSelectedImage:Boolean) : Parcelable {
        }
        @Parcelize
        data class MesurementUnit(
            var measurement_unit: String? = null,
            var isSelected: Boolean,
            var color_info: ArrayList<ColourInfo>? = null
        ):Parcelable{
            @Parcelize
            data class ColourInfo(
                var barcode: String? = null,
                var id: Int? = null,
                var color_name: String? = null,
                var isSelected: Boolean,
                var price: String? = null,
                var discount: String? = null,
                var quantity: Int? = null,
                var images: ArrayList<ImageData>? = null,
                var selling_price: String? = null
            ):Parcelable{


            }
        }
    }




/*
    @Parcelize
    data class RelatedProductData(
        var id: Int? = null,
        var category_id: Int? = null,
        var subCategory_id: Int? = null,
        var store_id: Int? = null,
        var name: String? = null,
        var main_price: String? = null,
        var selling_price: String? = null,
        var currency: String? = null,
        var item_number: String? = null,
        var quantity: Int? = null,
        var description: String? = null,
        var main_image: String? = null,
        var specification: String? = null,
        var rating: Int? = null,
        var is_wishlist: Int? = null,
        var is_added: Int? = null
    ) : Parcelable*/

    @Parcelize
    data class BarcodeProductDataModel(
        var barcode_products: ArrayList<ProductDetailDataModel.ProductData.MesurementUnit>? = null
    ) : Parcelable {
        @Parcelize
        data class MesurementUnit(
            var measurement_unit: String? = null,
            var isSelected: Boolean,
            var color_info: ArrayList<ColourInfo>? = null
        ) : Parcelable {
            @Parcelize
            data class ColourInfo(
                var barcode: String? = null,
                var id: Int? = null,
                var color_name: String? = null,
                var isSelected: Boolean,
                var price: String? = null,
                var discount: String? = null,
                var quantity: Int? = null,
                var images: ArrayList<ProductDetailDataModel.ProductData.ImageData>? = null,
                var selling_price: String? = null
            ) : Parcelable {
            }
        }
    }
}


