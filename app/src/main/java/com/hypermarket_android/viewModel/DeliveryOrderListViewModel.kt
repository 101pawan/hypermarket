package com.hypermarket_android.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DeliveryOrderListViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable
    var deliveryOrderListResponse = MutableLiveData<DeliveryOrderListResponse>()
    var getOnGoingOrderListResponse = MutableLiveData<DeliveryOrderListResponse>()
    var deliveryPastOrderListResponse = MutableLiveData<DeliveryOrderListResponse>()
    var deliveryCanceledOrderListResponse = MutableLiveData<DeliveryOrderListResponse>()
    var deliveryOutOrderListResponse = MutableLiveData<DeliveryOrderListResponse>()
    var orderStatusListResponse = MutableLiveData<DeliverOrderStatusResponseList>()
    var orderReasonListResponse = MutableLiveData<DeliveryOrderStatusReasonResponseList>()
    var orderUpdateResponse = MutableLiveData<DeliveryOrderStatusUpdateData>()
    var errorOrderList = MutableLiveData<Throwable>()


    //    getOrders ongoing
    fun getOrders(accessToken: String, statusId: String, deliveryBoyId: String) {
        apiInterface.getMyDeliveryOrders(
            accessToken = accessToken,
            statusId = statusId,
            deliveryBoyId = deliveryBoyId

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderList(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    fun getOnGoingOrders(accessToken: String, statusId: String, deliveryBoyId: String) {
        apiInterface.getMyDeliveryOrders(
            accessToken = accessToken,
            statusId = statusId,
            deliveryBoyId = deliveryBoyId

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOnGoingOrderList(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessOnGoingOrderList(it: DeliveryOrderListResponse) {
        Log.e("onSuccessOnGoingOrderList",it.toString())
        getOnGoingOrderListResponse.value = it
    }

    //    getOrders past
    fun getOrdersPast(accessToken: String, statusId: String, deliveryBoyId: String) {
        apiInterface.getMyDeliveryOrders(
            accessToken = accessToken,
            statusId = statusId,
            deliveryBoyId = deliveryBoyId

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
    fun getOrdersCancelled(accessToken: String, statusId: String, deliveryBoyId: String) {
        apiInterface.getMyDeliveryOrders(
            accessToken = accessToken,
            statusId = statusId,
            deliveryBoyId = deliveryBoyId

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListCancelled(it)
            },
                {
                    onErrorOrderList(it)
                })
    }
    fun getOrdersOutDelivery(accessToken: String, statusId: String, deliveryBoyId: String) {
        apiInterface.getMyDeliveryOrders(
            accessToken = accessToken,
            statusId = statusId,
            deliveryBoyId = deliveryBoyId

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderListOutDelivery(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessOrderList(it: DeliveryOrderListResponse) {
        deliveryOrderListResponse.value = it
    }

    private fun onSuccessOrderListPast(it: DeliveryOrderListResponse) {
        deliveryPastOrderListResponse.value = it
    }

    private fun onSuccessOrderListCancelled(it: DeliveryOrderListResponse) {
        deliveryCanceledOrderListResponse.value = it
    }
    private fun onSuccessOrderListOutDelivery(it: DeliveryOrderListResponse) {
        deliveryOutOrderListResponse.value = it
    }

    private fun onErrorOrderList(it: Throwable) {
        Log.e("Fsfds", it.message!!)
        errorOrderList.value = it
    }

    fun getOrderStatusList(accessToken: String, deliveryBoyId: String) {
        apiInterface.getDeliveryOrdersStatus(
             accessToken = accessToken,
             deliveryBoyId = deliveryBoyId

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessOrderStatusList(it)
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
    private fun onSuccessOrderStatusList(it: DeliverOrderStatusResponseList) {
        orderStatusListResponse.value = it
    }

    fun updateOrder(accessToken: String, orderId: String, statusId: String, reasonId: String? = null, comment: String) {
        apiInterface.updateDeliveryOrderStatus(
            accessToken = accessToken,
            orderId = orderId,
            reasonId = reasonId,
            statusId = statusId,
            comment = comment
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessUpdateOrderResponse(it)
            },
                {
                    onErrorOrderList(it)
                })
    }

    private fun onSuccessUpdateOrderResponse(it: DeliveryOrderStatusUpdateData) {
        orderUpdateResponse.value = it
    }
    
}