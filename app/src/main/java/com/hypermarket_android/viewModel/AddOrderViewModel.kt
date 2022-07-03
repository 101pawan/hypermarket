package com.hypermarket_android.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.activity.AddCardAfterOrderActivity
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddOrderViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable
    var addOrderReponse = MutableLiveData<AddOrderResponse>()
    var paymentResponse = MutableLiveData<PaymentProcessResponse>()
    var errorOrderResponse = MutableLiveData<Throwable>()

    fun placeOrder(
        accessToken: String,
        address_id: String,
        store_id: String,
        coupon_id: String,
        payment_mode: String,
        expected_Delivery: String,
        delivery_charge: String,
        redeemCode: String,
        total_payable_amount: String,
        product_id: String,
        remainingPoint: String,
        is_redeemPoint: String,
        redeemAmount: String,
        quantity: String,
        barCodeId: String,
        totalPrice: String,
        product_color: String,
        taxCharge: String,
        cashOnDelivery: String,
        walletAmountDeduction: String,
        total_item_price: String

    ) {
        mDisposable = apiInterface.placeOrder(
            accessToken = accessToken,
            addressId = address_id,
            StoreId = store_id,
            coupon_id = coupon_id,
            paymentMode = payment_mode,
            expected_delivery = expected_Delivery,
            deliveryCharge = delivery_charge,
            redeemCode = redeemCode,
            totalAmountPayable = total_payable_amount,
            productId = product_id,
            quantity = quantity,
            remainingPoint = remainingPoint,
            is_redeemPoint = is_redeemPoint,
            redeemAmount = redeemAmount,
            barCodeId = barCodeId,
            totalPrice = totalPrice,
            product_color = product_color,
            tax_charge = taxCharge,
            cash_on_delivery = cashOnDelivery,
            wallet_amount_deduction = walletAmountDeduction,
            total_item_price = total_item_price
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessAddOrder(it)
            },
                {
                    onErrorAddOrder(it)
                })
    }


    fun doOnlinePayment(
        accessToken: String,
        paymentOrderModel: AddCardAfterOrderActivity.PaymentOrderModel
    ) {
        apiInterface.paymentMethod(accessToken, paymentOrderModel).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                paymentSuccess(it)
            },
                {
                    onErrorAddOrder(it)
                })
    }

    private fun onSuccessAddOrder(it: AddOrderResponse) {
        addOrderReponse.value = it
    }

    private fun onErrorAddOrder(it: Throwable) {
        Log.e("Fsfsd", it.message!!)
        errorOrderResponse.value = it
    }

    private fun paymentSuccess(it: PaymentProcessResponse) {
        paymentResponse.value = it
    }


}