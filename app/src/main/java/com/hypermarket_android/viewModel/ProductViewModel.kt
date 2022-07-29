package com.hypermarket_android.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProductViewModel : BaseViewModel() {


    var listOfProduct =
        MutableLiveData<ArrayList<TrendingDataModel.ProductListDataModel.ProductData>>();

    var isNetworkError: MutableLiveData<Int> = MutableLiveData()
    var isDataEnd: MutableLiveData<Boolean> = MutableLiveData()
    var carDataList : MutableLiveData<Int> = MutableLiveData()
    var checkAvailablityResponse  = MutableLiveData<CheckAvailablityResponse>()
    var errorList = MutableLiveData<Throwable>()


    fun getProductsDetails(
        accessToken: String,
        productId: Int,
        dealId: Int,
        callback: Callback<ProductDetailDataModel>
    ) {
        apiInterface.getProductDetails(accessToken, productId, dealId).enqueue(callback)
    }

    fun getViewProductsDetails(
        accessToken: String,
        productId: Int,
        dealId: Int,
        callback: Callback<ProductDetailDataModel>
    ) {
        apiInterface.getViewProductDetails(accessToken, productId, dealId).enqueue(callback)
    }


    fun getViewBarcodeProduct(
        accessToken: String,
        productId: Int,
        dealId: Int,
        callback: Callback<ProductDetailDataModel.BarcodeProductDataModel>
    ) {
        apiInterface.getViewBarcodeProduct(accessToken, productId, dealId).enqueue(callback)
    }

    fun getRelatedProduct(
        accessToken: String,
        productId: Int,
        dealId: Int,
        callback: Callback<ProductDetailDataModel>
    ) {
        apiInterface.getRelatedProduct(accessToken, productId, dealId).enqueue(callback)
    }


    // Get Product List from Model
    fun getProductListFromModel(): MutableLiveData<ArrayList<TrendingDataModel.ProductListDataModel.ProductData>> {
        var productListData = listOfProduct.getValue()
        if (productListData == null) {
            productListData = ArrayList<TrendingDataModel.ProductListDataModel.ProductData>()
            listOfProduct.setValue(productListData)

        }
        return listOfProduct

    }



    @SuppressLint("CheckResult")
    fun productServiceCharge(
        accessToken:String,
        zipcode:String
    ){
        apiInterface.productServiceCharge(
            accessToken = accessToken,
            zipcode = zipcode
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it)
            },
                {
                    onErrorCartList(it)
                })
    }

    private fun onSuccess(it: CheckAvailablityResponse?) {
        checkAvailablityResponse.value =it
    }

    fun getProductList(
        accessToken: String,
        categoryId: String,
        storeId: String,
        page: String,
        sort: String
    ) {
        Log.d("response==", accessToken)
        Log.d("response==", categoryId)
        Log.d("response==", storeId)
        Log.d("response==", "start")
        Log.d("response==", page)


        isDataEnd.value = false

        apiInterface.getProductList(accessToken, categoryId, storeId, page, sort)
            .enqueue(object : Callback<TrendingDataModel> {
                override fun onFailure(call: Call<TrendingDataModel>, t: Throwable) {
                    t.printStackTrace()
                    if (page == "1") {
                        isNetworkError.value = 1;
                    } else {
                        isNetworkError.value = 2
                    }

                }

                override fun onResponse(
                    call: Call<TrendingDataModel>,
                    response: Response<TrendingDataModel>
                ) {

                    if (response.isSuccessful) {


                        isNetworkError.value = 0
                        Log.e("ProductListResponse",response.body().toString())
                        if (response.body()!!.product_list.data.size == 0) {
                            isDataEnd.postValue(true)

                        }

                        if ((sort!="")&&(page=="1")){
                            listOfProduct.getValue()!!.clear()
                        }

                        var feedListData = listOfProduct.getValue()
                        if (feedListData == null)
                            feedListData = ArrayList()
                        feedListData.addAll(response.body()!!.product_list.data)
                        if(response.body()!!.product_list.data.size>0) {
                            feedListData.get(0).currentPage =
                                response.body()!!.product_list.current_page
                        }
                        listOfProduct.setValue(feedListData)

                    } else {
                        Log.d("response==", "failed")

                        if (page == "1") {
                            isNetworkError.value = 1
                        } else {
                            isNetworkError.value = 2
                        }

                    }
                }


            })


    }


    //------------------------------------------------Add Cart-----------------------------------------------------------------

    fun postAddToCart(
        accessToken: String,
        productId: Int,
        storeId: Int,
        quantity: Int,
        barcodeId: String,
        callback: Callback<Any>
    ) {
        Log.e("AccessToken",accessToken)
        Log.e("ProductId",productId.toString())
        Log.e("StoreId",storeId.toString())
        Log.e("Quantity",quantity.toString())
        Log.e("BarcodeId",barcodeId)
        apiInterface.postAddCart(accessToken, productId, storeId, quantity,barcodeId).enqueue(callback)
    }

    fun postUpdateAddToCart(
        accessToken: String,
        productId: Int,
        storeId: Int,
        quantity: Int,
        cartId:Int,
        barcodeId: String,
        callback: Callback<Any>
    ) {
        val product = CartRequest(productId.toString(), storeId.toString(), quantity.toString(),barcodeId,cartId.toString())
        apiInterface.postUpdateAddCart(accessToken, product).enqueue(callback)
    }

    fun postRemoveCart(
        accessToken: String,
        cartId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postRemoveFromCart(accessToken, cartId).enqueue(callback)
    }

    fun postMoveToCart(
        accessToken: String,
        cartId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postMoveToCart(accessToken, cartId).enqueue(callback)
    }

    fun getCartList(accessToken: String, callback: Callback<CartDataModel>) {
        apiInterface.getCartList(accessToken).enqueue(callback)
    }
    @SuppressLint("CheckResult")
    fun getCartObserable(accessToken: String){
        apiInterface.getCartObseravle(
            accessToken = accessToken

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("OnCartAPIPage",it.toString())
                onSuccessCartList(it)
            },
                {
                    onErrorCartList(it)
                })
    }

    fun onSuccessCartList(it: CartDataModel) {
        var cartCount = 0
        for (cart in it.cart_list!!){
            cartCount += cart.quantity!!
        }
        carDataList.value = cartCount
    }
    fun postAddSaverLaterData(
        accessToken: String,
        cartId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postAddSaveLaterData(accessToken, cartId).enqueue(callback)
    }
 fun postRemoveSaverLaterData(
        accessToken: String,
        cartId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postRemoveSaverLaterData(accessToken, cartId).enqueue(callback)
    }


    fun getSaverLaterData(
        accessToken: String
        ,callback: Callback<SaverLaterDataModel>
    ) {
        apiInterface.getSaverLaterData(accessToken).enqueue(callback)
    }
    //-------------------------------WishList----------------------------------------------------------------

    fun postAddToWishList(
        accessToken: String,
        productId: Int,
        cartId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postAddWishList(accessToken, productId,cartId).enqueue(callback)
    }
    fun postRemoveToWishList(
        accessToken: String,
        wishId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postRemoveWishList(accessToken, wishId).enqueue(callback)
    }

    fun postMoveToCartFromWishList(
        accessToken: String,
        wishId: Int,
        callback: Callback<Any>
    ) {
        apiInterface.postMoveToCartFromWishList(accessToken, wishId).enqueue(callback)
    }

    fun getWishList(
        accessToken: String,
        callback: Callback<WishListDataModel>
    ) {
        apiInterface.getWishList(accessToken).enqueue(callback)
    }
    //-------------------------------Deals----------------------------------------------------------------

    fun getDeals(
        accessToken: String,
        storeId: Int,
        callback: Callback<DealsDataModel>
    ) {
        apiInterface.getDeals(accessToken, storeId).enqueue(callback)
    }

    fun getDealsProduct(
        deal_id: Int,
        callback: Callback<DealsProductDataModel>
    ) {
        apiInterface.getDealsProduct( deal_id).enqueue(callback)
    }

    //---------------------------------RemoveWishList And CartList---------------------------------------------
    fun postRemoveCartListAndWishList(
        accessToken: String,
        callback: Callback<Any>
    ) {
        apiInterface.postRemoveCartListAndWishList( accessToken).enqueue(callback)
    }


    fun getCheckCartListAndWishList(
        accessToken: String,
        callback: Callback<Any>
    ) {
        apiInterface.getCheckCartListAndWishList( accessToken).enqueue(callback)
    }

    private fun onErrorCartList(it: Throwable) {
        errorList.value = it
    }

}