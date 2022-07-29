package com.hypermarket_android.util

import com.hypermarket_android.activity.AddCardAfterOrderActivity
import com.hypermarket_android.constant.NetworkConstants
import com.hypermarket_android.dataModel.*
import com.hypermarket_android.ui.changepassword.ChangePasswordResponse
import com.hypermarket_android.ui.downloadInvoice.DownloadInvoiceResponse
import com.hypermarket_android.ui.feedback.FeedbackResponse
import com.hypermarket_android.ui.forgotPassword.ResponseForgot
import com.hypermarket_android.ui.login.AutoSearchingEmail.ResponseSearchEmail
import com.hypermarket_android.ui.login.ResponseLogin
import com.hypermarket_android.ui.personalDetailActivity.ResponseCity
import com.hypermarket_android.ui.personalDetailActivity.ResponseProfileCreate
import com.hypermarket_android.ui.register.ResponseRegister
import com.hypermarket_android.ui.register.socialSignUp.ResponseSocialSignUp
import com.hypermarket_android.ui.resetPassword.ResponseResetPassword
import com.hypermarket_android.ui.selectLanguage.ResponseLanguage
import com.hypermarket_android.ui.setting.ResponseLogOut
import com.hypermarket_android.ui.verification.ResponseResendOtp
import com.hypermarket_android.ui.verification.ResposeOtp
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST(NetworkConstants.SIGN_UP)
    fun signup(
        @Field("name") name: String,
        @Field("mobile_number") mobile_number: String,
        @Field("email") email: String,
        @Field("country_name") country_name: String,
        @Field("password") password: String,
        @Field("country_code") country_code: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("language") language: String

    ): Observable<ResponseRegister>

    @FormUrlEncoded
    @POST(NetworkConstants.OTP_VERIFY)
    fun otp(
        @Field("user_id") user_id: String,
        @Field("otp") otp: String
    ): Observable<ResposeOtp>

    @FormUrlEncoded
    @POST(NetworkConstants.RESEND_OTP)
    fun resendOtp(
        @Field("user_id") user_id: String
    ): Observable<ResponseResendOtp>

    @Multipart
    @POST(NetworkConstants.CREATE_PROFILE)
    fun editProfile(
        @Header("accessToken") accessToken: String,
        @Part("user_id") user_id: RequestBody,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("alt_mobile_number") alt_mobile_number: RequestBody,
        @Part("country_name") country_name: RequestBody,
        @Part("city_name") city_name: RequestBody,
        @Part("building_name") building_name: RequestBody,
        @Part("street") street: RequestBody,
        @Part("near_location") near_location: RequestBody,
        @Part("zip_code") zip_code: RequestBody,
        @Part() image: MultipartBody.Part? = null
    ): Observable<ResponseProfileCreate>

    @Multipart
    @POST(NetworkConstants.CREATE_PROFILE)
    fun editProfile(
        @Header("accessToken") accessToken: String,
        @Part("user_id") user_id: RequestBody,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part() image: MultipartBody.Part? = null
    ): Observable<ResponseProfileCreate>


    /*   @FormUrlEncoded
       @POST(NetworkConstants.FORGOT_PASSWORD)
       fun forgetPassword(
           @Field("mobile_number") mobile_number:String
       ):Observable<ResponseForgot>
       */
    @FormUrlEncoded
    @POST(NetworkConstants.FORGOT_PASSWORD)
    fun forgetPassword(
        @Field("mobile_number") mobile_number: String
    ): Call<ResponseForgot>

    @FormUrlEncoded
    @POST(NetworkConstants.RESET_PASSWORD)
    fun resetPassword(
        @Field("user_id") user_id: String,
        @Field("password") password: String
    ): Observable<ResponseResetPassword>

    @FormUrlEncoded
    @POST(NetworkConstants.LOGIN)
    fun login(
        @Field("mobile_number") mobile_number: String,
        @Field("country_code") country_code: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("password") password: String
    ): Observable<ResponseLogin>

    @FormUrlEncoded
    @POST(NetworkConstants.SEARCH_EMAIL)
    fun getEmail(
        @Field("mobile_number") mobile_number: String
    ): Observable<ResponseSearchEmail>

    @FormUrlEncoded
    @POST(NetworkConstants.CITY)
    fun getCity(
        @Header("accessToken") accessToken: String,
        @Field("country_id") country_id: String
    ): Observable<ResponseCity>

    //TODO productServiceCharge
    @FormUrlEncoded
    @POST(NetworkConstants.productServiceCharge)
    fun productServiceCharge(
        @Header("accessToken") accessToken: String,
        @Field("city_id") zipcode: String
    ): Observable<CheckAvailablityResponse>



    @FormUrlEncoded
    @POST(NetworkConstants.GetState)
    fun getStates(
        @Header("accessToken") accessToken: String,
        @Field("countryId") country_id: String
    ): Observable<StateResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.GetCity)
    fun getCityNames(
        @Header("accessToken") accessToken: String,
        @Field("stateId") state_id: String
    ): Observable<CityResponse>

    @FormUrlEncoded
    @GET(NetworkConstants.LOG_OUT)
    fun getLogOut(
        @Header("accessToken") accessToken: String
    ): Observable<ResponseLogOut>

    @FormUrlEncoded
    @POST(NetworkConstants.SOCIAL_SIGN_UP)
    fun socialSignUp(
        @Field("email") email: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("social_id") social_id: String,
        @Field("social_type") social_type: String,
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("language") language: String
    ): Observable<ResponseSocialSignUp>

    @FormUrlEncoded
    @POST(NetworkConstants.GET_CATEGORY)
    fun getCategory(
        @Header("accessToken") accessToken: String,
        @Field("store_id") storeId: Int): Call<CategoriesDataModel>

    @FormUrlEncoded
    @POST(NetworkConstants.GET_TRENDING_PRODUCT)
    fun getTrendingProduct(
        @Header("accessToken") accessToken: String,
        @Field("store_id") storeId: String,
        @Field("page") page: String
    ): Call<TrendingDataModel>

    @POST(NetworkConstants.TRENDING_PRODUCT)
    fun getTrendingProduct(
        @Header("accessToken") accessToken: String
    ): Call<TrendingDataModel>

    @FormUrlEncoded
    @POST(NetworkConstants.RANDOM_PRODUCT)
    fun getRandomProduct(
        @Header("accessToken") accessToken: String,
        @Field("category_id") category_id: String
    ): Call<RandomProductDataModel>

    @POST(NetworkConstants.GET_STORE_LIST)
    fun getStoreList(@Query("country") storeId: String): Call<StoreDataModel>

    @POST(NetworkConstants.GET_STORE_DETAIL)
    fun getStoreDetail(@Query("store_id") storeId: Int): Call<StoreDetailDataModel>

    /* @Multipart
     @POST(NetworkConstants.GET_PRODUCT_LIST)
     fun getProductList(
         @Header("accessToken") accessToken: String,
         @Part("category_id")categoryId:RequestBody,
         @Query("store_id") storeId: RequestBody,
         @Part("page") page: RequestBody,
         @Part("sort")sort:RequestBody
     ): Call<TrendingDataModel>*/

    @POST(NetworkConstants.GET_PRODUCT_LIST)
    fun getProductList(
        @Header("accessToken") accessToken: String,
        @Query("category_id") categoryId: String,
        @Query("store_id") storeId: String,
        @Query("page") page: String,
        @Query("sort") sort: String
    ): Call<TrendingDataModel>

    @POST(NetworkConstants.GET_PRODUCT_DETAIL)
    fun getProductDetails(
        @Header("accessToken") accessToken: String,
        @Query("product_id") product_id: Int,
        @Query("deal_id") deal_id: Int
    ): Call<ProductDetailDataModel>

    @POST(NetworkConstants.GET_VIEW_PRODUCT_DETAIL)
    fun getViewProductDetails(
        @Header("accessToken") accessToken: String,
        @Query("product_id") product_id: Int,
        @Query("deal_id") deal_id: Int
    ): Call<ProductDetailDataModel>

    @POST(NetworkConstants.GET_VIEW_BARCODE_PRODUCT)
    fun getViewBarcodeProduct(
        @Header("accessToken") accessToken: String,
        @Query("product_id") product_id: Int,
        @Query("deal_id") deal_id: Int
    ): Call<ProductDetailDataModel.BarcodeProductDataModel>

    @POST(NetworkConstants.GET_RELATED_PRODUCT)
    fun getRelatedProduct(
        @Header("accessToken") accessToken: String,
        @Query("product_id") product_id: Int,
        @Query("deal_id") deal_id: Int
    ): Call<ProductDetailDataModel>

    @POST(NetworkConstants.POST_ADD_CART)
    fun postAddCart(
        @Header("accessToken") accessToken: String,
        @Query("product_id") product_id: Int,
        @Query("store_id") storeId: Int,
        @Query("quantity") quantity: Int,
        @Query("barcode_id") barcodeId: String
    ): Call<Any>

    @POST(NetworkConstants.POST_ADD_CART)
    fun postUpdateAddCart(
        @Header("accessToken") accessToken: String,
        @Body cartRequest: CartRequest
    ): Call<Any>

    @POST(NetworkConstants.POST_REMOVE_FROM_CART)
    fun postRemoveFromCart(
        @Header("accessToken") accessToken: String,
        @Query("cart_id") cartId: Int
    ): Call<Any>

    @GET(NetworkConstants.GET_CART_LIST)
    fun getCartList(
        @Header("accessToken") accessToken: String
    ): Call<CartDataModel>

    @GET(NetworkConstants.GET_CART_LIST)
    fun getCartObseravle(
        @Header("accessToken") accessToken: String
    ): Observable<CartDataModel>


    @POST(NetworkConstants.POST_MOVE_TO_CART)
    fun postMoveToCart(
        @Header("accessToken") accessToken: String,
        @Query("cart_id") cartId: Int
    ): Call<Any>

    @POST(NetworkConstants.POST_ADD_SAVE_LATER_DATA)
    fun postAddSaveLaterData(
        @Header("accessToken") accessToken: String,
        @Query("cart_id") cartId: Int
    ): Call<Any>

    @POST(NetworkConstants.DELETE_SAVE_LATER_DATA)
    fun postRemoveSaverLaterData(
        @Header("accessToken") accessToken: String,
        @Query("cart_id") cartId: Int
    ): Call<Any>

    @GET(NetworkConstants.GET_SAVE_LATER_LIST)
    fun getSaverLaterData(
        @Header("accessToken") accessToken: String
    ): Call<SaverLaterDataModel>

    @POST(NetworkConstants.POST_ADD_WISH_LIST)
    fun postAddWishList(
        @Header("accessToken") accessToken: String,
        @Query("product_id") productId: Int,
        @Query("cart_id") cartId: Int
    ): Call<Any>

    @POST(NetworkConstants.POST_REMOVE_FROM_WISH_LIST)
    fun postRemoveWishList(
        @Header("accessToken") accessToken: String,
        @Query("wishlist_id") wishlistID: Int
    ): Call<Any>


    @POST(NetworkConstants.POST_MOVE_TO_CART_FROM_WISH_LIST)
    fun postMoveToCartFromWishList(
        @Header("accessToken") accessToken: String,
        @Query("wishlist_id") wishlistID: Int
    ): Call<Any>

    @GET(NetworkConstants.GET_WISH_LIST)
    fun getWishList(
        @Header("accessToken") accessToken: String
    ): Call<WishListDataModel>


    @POST(NetworkConstants.POST_DEALS)
    fun getDeals(
        @Header("accessToken") accessToken: String,
        @Query("store_id") storeId: Int
    ): Call<DealsDataModel>

    @POST(NetworkConstants.POST_DEALS_PRODUCT)
    fun getDealsProduct(
        @Query("deal_id") storeId: Int
    ): Call<DealsProductDataModel>


    @GET(NetworkConstants.POST_REMOVE_CARTLIST_AND_WISHLIST)
    fun postRemoveCartListAndWishList(
        @Header("accessToken") accessToken: String
    ): Call<Any>

    @FormUrlEncoded
    @POST(NetworkConstants.UPDATE_LANGUAGE)
    fun updateLanguage(
        @Header("accessToken") accessToken: String,
        @Field("language") language:String,
        @Field("user_id") user_id:String
    ): Observable<ResponseLanguage>

    @GET(NetworkConstants.GET_CHECK_CART_WISHLIST)
    fun getCheckCartListAndWishList(
        @Header("accessToken") accessToken: String
    ): Call<Any>


    @FormUrlEncoded
    @POST(NetworkConstants.Get_User_Reward_Point)
    fun getUserRewardPoint(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id:String
    ): Observable<GetEarnResponse>


    @FormUrlEncoded
    @POST(NetworkConstants.Add_User_Reward_Points)
    fun addUserRewardPoints(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id:String,
        @Field("is_points") is_points:String="1"
    ): Observable<AddRewardsResposne>


    @FormUrlEncoded
    @POST(NetworkConstants.ADD_NEW_ADDRESS)

    fun addNewAddress(
        @Header("accessToken") accessToken: String,
        @Field("name") name: String,
        @Field("house_number") houseNumber: String,
        @Field("building_tower") buildingTower: String,
        @Field("society_locality") societyLocality: String,
        @Field("alt_country_code") altCountryCode: String,
        @Field("alt_mobile_number") altMobileNumber: String,
        @Field("pincode") pincode: String,
        @Field("city") city: Int,
        @Field("district") district: Int,
        @Field("save_as") saveAs: String,


        ): Observable<AddUpdateAddressResponse>


    @FormUrlEncoded
    @POST(NetworkConstants.UPDATE_ADDRESS)

    fun updateAddress(
        @Header("accessToken") accessToken: String,
        @Field("name") name: String,
        @Field("house_number") houseNumber: String,
        @Field("building_tower") buildingTower: String,
        @Field("society_locality") societyLocality: String,
        @Field("alt_country_code") altCountryCode: String,
        @Field("alt_mobile_number") altMobileNumber: String,
        @Field("pincode") pincode: String,
        @Field("city") city: Int,
        @Field("district") district: Int,
        @Field("save_as") saveAs: String,
        @Field("address_id") addressId: Int


    ): Observable<AddUpdateAddressResponse>


    //     get address
    @GET(NetworkConstants.GET_ADDRESS)
    fun getMangeAddressList(
        @Header("accessToken") accessToken: String
    ): Observable<ManageAddressResponse>


    //     get Orders
    @FormUrlEncoded
    @POST(NetworkConstants.orders_list)
    fun getOrders(
        @Header("accessToken") accessToken: String,
        @Field("user_id") userId: String,
        @Field("order_type") orderType: String
    ): Observable<OrderListResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.new_orders_list)
    fun getNewOrdersList(
        @Header("accessToken") accessToken: String,
        @Field("user_id") userId: String,
        @Field("order_type") orderType: String,
        @Field("page_number") pageNumber: String,
    ): Observable<OrderListResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.get_orders_list)
    fun getCompleteOrdersList(
        @Header("accessToken") accessToken: String,
        @Field("user_id") userId: String,
        @Field("order_type") orderType: String,
        @Field("page_number") pageNumber: String,
    ): Observable<GetOrdersList>

    @FormUrlEncoded
    @POST(NetworkConstants.view_order_details)
    fun getCompleteOrdersDetails(
        @Header("accessToken") accessToken: String,
        @Field("order_number") order_number: String,
        @Field("user_id") user_id: String
    ): Observable<OrderListResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.my_order)
    fun getMyDeliveryOrders(
        @Header("accessToken") accessToken: String,
        @Field("delivery_boy_id") deliveryBoyId: String,
        @Field("status_id") statusId: String
    ): Observable<DeliveryOrderListResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.order_status)
    fun getDeliveryOrdersStatus(
        @Header("accessToken") accessToken: String,
        @Field("delivery_boy_id") deliveryBoyId: String,
    ): Observable<DeliverOrderStatusResponseList>


    @POST(NetworkConstants.status_reason)
    fun getDeliveryOrdersReason(
        @Header("accessToken") accessToken: String,
    ): Observable<DeliveryOrderStatusReasonResponseList>

    @FormUrlEncoded
    @POST(NetworkConstants.update_order)
    fun updateDeliveryOrderStatus(
        @Header("accessToken") accessToken: String,
        @Field("order_id") orderId: String,
        @Field("status_id") statusId: String,
        @Field("reason_id") reasonId: String?,
        @Field("comment") comment: String
    ): Observable<DeliveryOrderStatusUpdateData>

    @FormUrlEncoded
    @POST(NetworkConstants.update_refund_replacement_order)
    fun updateRefundReplaceOrderStatus(
        @Header("accessToken") accessToken: String,
        @Field("product_return_id") orderId: String,
        @Field("status_id") statusId: String
    ): Observable<DeliveryOrderStatusUpdateData>

    @FormUrlEncoded
    @POST(NetworkConstants.order_return)
    fun updateRefundOrderStatus(
        @Header("accessToken") accessToken: String,
        @Field("order_id") orderId: String,
        @Field("product_id") product_id: String,
        @Field("barcode_id") barcode_id: String,
        @Field("type") type: String,
        @Field("reason_id") reason_id: String,
        @Field("user_id") user_id: String,
        @Field("qty") qty: String,
        @Field("product_price") productPrice: String
    ): Observable<DeliveryOrderStatusUpdateData>

    @FormUrlEncoded
    @POST(NetworkConstants.my_wallet)
    fun getWalletData(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id: String
    ): Observable<WalletModel>

    //     get Coupons
    @FormUrlEncoded
    @POST(NetworkConstants.get_coupon)
    fun get_coupon(
        @Header("accessToken") accessToken: String,
        @Field("store_id") storeId: String,
        @Field("product_id") ProductId: String
    ): Observable<CouponListResponse>
    //     do Payment
    @POST(NetworkConstants.payment_method)
    fun paymentMethod(
        @Header("accessToken") accessToken: String,
        @Body parama: AddCardAfterOrderActivity.PaymentOrderModel
    ): Observable<PaymentProcessResponse>


    //     add Order
    @FormUrlEncoded
    @POST(NetworkConstants.Order_Place)
    fun placeOrder(
        @Header("accessToken") accessToken: String,
        @Field("address_id") addressId: String,
        @Field("store_id") StoreId: String,
        @Field("coupon_id") coupon_id: String,
        @Field("payment_mode") paymentMode: String,
        @Field("expected_delivery") expected_delivery: String,
        @Field("delivery_charge") deliveryCharge: String,
        @Field("redeemCode") redeemCode: String,
        @Field("total_payable_amount") totalAmountPayable: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: String,
        @Field("remainingPoint") remainingPoint: String="0",
        @Field("is_redeemPoint") is_redeemPoint: String="0",
        @Field("redeemAmount") redeemAmount: String="0",
        @Field("barcode_id") barCodeId: String,
        @Field("price") totalPrice: String,
        @Field("product_color") product_color: String,
        @Field("tax_charge") tax_charge: String,
        @Field("cash_on_delivery") cash_on_delivery: String,
        @Field("wallet_amount_deduction") wallet_amount_deduction: String,
        @Field("total_item_price") total_item_price: String
    ): Observable<AddOrderResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.ProductRating)
    fun addRating(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id: String,
        @Field("product_id") product_id: String,
        @Field("rating") rating: String,
        @Field("review") review: String
    ): Observable<AddRatingResponse>
    // cancel Order
    @FormUrlEncoded
    @POST(NetworkConstants.CancelOrder)
    fun cancelOrder(
        @Header("accessToken") accessToken: String,
        @Field("reason_id") reason_id: String,
        @Field("order_id") order_id: String
    ): Observable<CancelOrderResponse>
    // cancel Order
    @FormUrlEncoded
    @POST(NetworkConstants.productInvoice)
    fun getInvoiceDetails(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id: String,
        @Field("order_id") order_id: String
    ): Observable<OrderInvoiceResponse>

    //     get cancelationList
    @GET(NetworkConstants.cancellationList)
    fun getCancellationList(
        @Header("accessToken") accessToken: String,
    ): Observable<CancelListResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.DELETE_ADDRESS)
    fun deleteAddress(
        @Header("accessToken") accessToken: String,
        @Field("address_id") addressId: String
    ): Observable<DeleteAddressResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.SEND_SUPPORT)
    fun sendMessage(
        @Header("accessToken") accessToken: String,
        @Field("full_name") fullName: String,
        @Field("country_code") countryCode: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("about") about: String,
        @Field("message") message: String

    ): Observable<SendMessageResponse>

    @GET(NetworkConstants.GET_NOTIFICATION)
    fun getNotification(
        @Header("accessToken") accessToken: String
    ): Observable<NotificationResponse>

    //    TuEldj4S5nh7wQcCLRMtoK0D4DJI2LIdfRUgkXFgCfnZR6z9FBZVTvbc0oPW

//    cTLmZipBKy0:APA91bG9Br-6oxCzjN7oLJrF5vxtqW5rqqx14iAxp6Yg1VSWiB6tCJ6HUijdz1eWc7jpc0uBGCe31LFV0egKxA2C_M8wJ9Khw3wGZZnK24WccpPoyPVUGHY--ImQFkKbp55zGUOjHkBr

    @POST(NetworkConstants.search_product)
    fun getSearchProduct(
        @Header("accessToken") accessToken: String,
        @Query("store_id") storeId: String,
        @Query("page") page: String,
        @Query("product_name") product_name: String
    ): Call<HomeSearchResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.FEEDBACK)
    fun feedback(
        @Header("accessToken") accessToken: String,
        @Field("message") message: String
    ): Observable<FeedbackResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.CHNAGE_PASSWORD)
    fun change_password(
        @Header("accessToken") accessToken: String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String
    ): Observable<ChangePasswordResponse>

    @FormUrlEncoded
    @POST(NetworkConstants.DOWNLOADED_INVOICES)
    fun download_invoice(
        @Header("accessToken") accessToken: String,
        @Field("user_id") user_id: String
    ): Observable<DownloadInvoiceResponse>

}