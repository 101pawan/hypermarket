package com.hypermarket_android.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OrderDetailVm : BaseViewModel() {

    private lateinit var mDisposable: Disposable
    //    var orderListResponse = MutableLiveData<OrderListResponse>()
    var orderListResponse = MutableLiveData<OrderListResponse>()
    var pastorderListResponse = MutableLiveData<OrderListResponse>()
    var cancelledorderListResponse = MutableLiveData<OrderListResponse>()
    var cancelListResponse = MutableLiveData<CancelListResponse>()
    var cancelOrderResponse = MutableLiveData<CancelOrderResponse>()
    var orderInvoiceResponse = MutableLiveData<OrderInvoiceResponse>()
    var errorOrderList = MutableLiveData<Throwable>()
    var status = MutableLiveData<Int>()
    var orderReasonListResponse = MutableLiveData<DeliveryOrderStatusReasonResponseList>()

    //    getOrders ongoing
    fun getOrders(accessToken: String, type: String, user_id: String) {
//        apiInterface.getOrders(
//            accessToken = accessToken,
//            orderType = type,
//            userId = user_id
//
//        ).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                onSuccessOrderList(it)
//            },
//                {
//                    onErrorOrderList(it)
//                })
    }

    fun getCompleteOrdersDetails(accessToken: String, order_number: String, user_id: String) {
        apiInterface.getCompleteOrdersDetails(
            accessToken = accessToken,
            order_number = order_number,
            user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderList(it)
            },
                {
                    onErrorOrderList(it)
                })

    }

    //    getOrders past
    fun getNewOrdersPast(accessToken: String, type: String, user_id: String, page_no: String) {
        apiInterface.getNewOrdersList(
            accessToken = accessToken,
            orderType = type,
            userId = user_id,
            pageNumber = page_no

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListPast(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    fun getOrdersPast(accessToken: String, type: String, user_id: String) {
        apiInterface.getOrders(
            accessToken = accessToken,
            orderType = type,
            userId = user_id

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListPast(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    //    getOrders cancelled

    fun getNewOrdersCancelled(accessToken: String, type: String, user_id: String, page_no: String) {
        apiInterface.getNewOrdersList(
            accessToken = accessToken,
            orderType = type,
            userId = user_id,
            pageNumber = page_no

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListCancelled(it)
            },
                {
                    onErrorOrderList(it)
                })
    }
    fun getOrdersCancelled(accessToken: String, type: String, user_id: String) {
        apiInterface.getOrders(
            accessToken = accessToken,
            orderType = type,
            userId = user_id

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListCancelled(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessOrderList(it: OrderListResponse) {
        orderListResponse.value = it
    }

//    private fun onSuccessOrderList(it: OrderListResponse) {
//        orderListResponse.value = it
//    }

    private fun onSuccessOrderListPast(it: OrderListResponse) {
        pastorderListResponse.value = it
    }

    private fun onSuccessOrderListCancelled(it: OrderListResponse) {
        cancelledorderListResponse.value = it
    }

    private fun onErrorOrderList(it: Throwable) {
        Log.e("Fsfds", it.message!!)
        errorOrderList.value = it
    }

    fun getCancellationList(accessToken: String) {
        apiInterface.getCancellationList(
            accessToken = accessToken

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCancellationList(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessCancellationList(it: CancelListResponse) {
        cancelListResponse.value = it
    }

    fun cancelOrder(accessToken: String, reason_id: String, order_id: String) {
        apiInterface.cancelOrder(
            accessToken = accessToken,
            reason_id = reason_id,
            order_id = order_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCancelResponse(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessCancelResponse(it: CancelOrderResponse) {
        cancelOrderResponse.value = it
    }


    fun getInvoiceDetails(accessToken: String, user_id: String, order_id: String) {
        apiInterface.getInvoiceDetails(
            accessToken = accessToken,
            user_id = user_id,
            order_id = order_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessInvoiceResponse(it)
            },
                {
                    onErrorOrderList(it)
                })

    }

    fun getOrderReasonList(accessToken: String) {
        apiInterface.getDeliveryOrdersReason(
            accessToken = accessToken

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderReasonList(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessOrderReasonList(it: DeliveryOrderStatusReasonResponseList) {
        orderReasonListResponse.value = it
    }

    fun onSuccessInvoiceResponse(it: OrderInvoiceResponse) {
        orderInvoiceResponse.value = it
    }


    @SuppressLint("CheckResult")
    fun refundOrder(
        accessToken: String,
        orderId: String,
        product_id: String,
        barcode_id: String,
        type: String,
        reason_id: String,
        user_id: String,
        qty: String,
        productPrice: String
    ) {
        apiInterface.updateRefundOrderStatus(
            accessToken = accessToken,
            orderId = orderId,
            product_id = product_id,
            barcode_id = barcode_id,
            type = type,
            reason_id = reason_id,
            user_id = user_id,
            qty = qty,
            productPrice = productPrice
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessUpdateOrderResponse(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessUpdateOrderResponse(it: DeliveryOrderStatusUpdateData?) {
        status.value = it?.status
    }
}