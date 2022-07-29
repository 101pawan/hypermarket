package com.hypermarket_android.constant

interface NetworkConstants {

    companion object {

        const val DUBAI_STORE_ID = 1
        const val DUBAI_STORE_NAME = "YasMart BaniYas"

        const val OMAN_STORE_ID = 2
        const val OMAN_STORE_NAME = "Yas for Modern center"

        private const val IP_ADDRESS = "3.6.74.92"
        const val BASE_URL = "https://yasmart.azurewebsites.net/api/"

        //For Flags
        const val HIDE_STATUS_BAR_FOR_FRAGMENT = "1"
        const val HIDE_STATUS_BAR_FOR_ACTIVITY = "2"

        const val PREFRENCE_NAME ="search"
        const val PERSISTABLE_PREFRENCE_NAME= "search_manager"

        //For social login
        const val LOGIN_TYPE_GOOGLE = "1"
        const val LOGIN_TYPE_FACEBOOK = "2"
        var lang = "en"

        const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        //For Api's
        const val SIGN_UP = "signup"
        const val SOCIAL_SIGN_UP = "socialSignin"
        const val OTP_VERIFY = "otpVerify"
        const val RESEND_OTP = "resendOtp"
        const val CREATE_PROFILE = "create_profile"
        const val LOGIN = "login"
        const val SEARCH_EMAIL = "search_email"
        const val FORGOT_PASSWORD = "forget_password"
        const val RESET_PASSWORD = "reset_password"
        const val CITY = "get_city"
        const val GetCity = "GetCity"
        const val GetState = "GetState"
        const val LOG_OUT = "logout"
        const val GET_CATEGORY = "category"
        const val GET_TRENDING_PRODUCT = "tranding_product"
        const val TRENDING_PRODUCT = "trending_products"
        const val RANDOM_PRODUCT = "random_products"
        const val ADD_NEW_ADDRESS = "add_address"
        const val UPDATE_ADDRESS = "update_address"
        const val GET_ADDRESS = "get_address"
        const val payment_method = "payment_method"
        const val Order_Place = "order_place"
        const val ProductRating = "ProductRating"
        const val orders_list = "ongoing_past_cancel_orders"
        const val new_orders_list = "view_orders_list"
        const val get_orders_list = "get_orders_list"
        const val view_order_details = "view_order_details"
        const val get_coupon = "get_coupon"
        const val cancellationList = "cancelation_list"
        const val CancelOrder = "cancelation_reason"
        const val productInvoice = "ProductInvoice"
        const val GET_NOTIFICATION = "get-notification"
        const val SEND_SUPPORT = "send-support"
        const val DELETE_ADDRESS = "delete_address"
        const val GET_STORE_LIST = "store_list"
        const val GET_STORE_DETAIL = "store_detail"
        const val GET_PRODUCT_LIST = "product_list"
        const val GET_PRODUCT_DETAIL= "product_detail"
        const val GET_VIEW_PRODUCT_DETAIL= "view_product_detail"
        const val GET_VIEW_BARCODE_PRODUCT= "view_barcode_products"
        const val GET_RELATED_PRODUCT= "related_products_and_wishlist"
        const val POST_ADD_CART= "add_cart"
        const val POST_REMOVE_FROM_CART= "remove_from_cart"
        const val GET_CART_LIST= "cart_list"
        const val POST_ADD_WISH_LIST= "add_wishlist"
        const val POST_MOVE_TO_CART= "moveToCart"
        const val POST_REMOVE_FROM_WISH_LIST= "remove_from_wishlist"
        const val POST_MOVE_TO_CART_FROM_WISH_LIST= "movetocartfrom_mwish"
        const val GET_WISH_LIST= "wishlist"
        const val POST_ADD_SAVE_LATER_DATA= "saveLaterData"
        const val DELETE_SAVE_LATER_DATA= "deleteSaveLater"
        const val GET_SAVE_LATER_LIST ="savelater_list"
        const val POST_DEALS ="deals"
        const val POST_DEALS_PRODUCT ="deal_product"
        const val POST_REMOVE_CARTLIST_AND_WISHLIST ="clr_cartwishlist"
        const val UPDATE_LANGUAGE = "update_language"
        const val GET_CHECK_CART_WISHLIST ="chkcartwishlist"
        const val Get_User_Reward_Point="GetUserRewarPoint"
        const val Add_User_Reward_Points="AddUserRewarPoints"
        const val productServiceCharge="productServiceCharge"
        const val search_product="search_product"
        const val FEEDBACK="feedback"
        const val CHNAGE_PASSWORD="change_password"
        const val DOWNLOADED_INVOICES="ProductInvoiceList"
        const val my_order = "my_orders"
        const val order_status = "order_status"
        const val status_reason = "order_status_reason"
        const val update_order = "add_order_status"
        const val update_refund_replacement_order = "update_refund_replace_order_status"
        const val order_return = "order_return"
        const val my_wallet = "my-wallet"
    }
}