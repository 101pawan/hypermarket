package com.hypermarket_android.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hypermarket_android.Adapter.OngoingDeliveryOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.DeliveryOrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.viewModel.DeliveryOrderListViewModel
import kotlinx.android.synthetic.main.fragment_ongoing_orders.rv_ongoing_orders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.android.synthetic.main.activity_update_order_status.*
import kotlinx.coroutines.*

class DeleveryOngoingFragment(private val statusId:Int) : BaseFragment() {
    private lateinit var deliveryOrderViewModel: DeliveryOrderListViewModel
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delevery_ongoing, container, false)
        deliveryOrderViewModel = ViewModelProvider(this).get(DeliveryOrderListViewModel::class.java)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
//        deliveryOrderViewModel.getOnGoingOrders(
//            sharedPreferenceUtil.accessToken,
//            statusId.toString(),
//            sharedPreferenceUtil.userId
//        )
        initViews()
        initControl()
        return view
    }
    override fun initViews() {
        swipeRefreshLayout?.setOnRefreshListener(OnRefreshListener {
            getOrders()
        })
        deliveryOrderViewModel.getOrders(
            sharedPreferenceUtil.accessToken,
            statusId.toString(),
            sharedPreferenceUtil.userId
        )
    }

    fun getOrders(){
        deliveryOrderViewModel.getOrders(
            sharedPreferenceUtil.accessToken,
            statusId.toString(),
            sharedPreferenceUtil.userId
        )
    }

    override fun initControl() {
        activity?.let {context->
            deliveryOrderViewModel.deliveryOrderListResponse.observe(context, Observer {
                if (swipeRefreshLayout?.isRefreshing == true){
                    swipeRefreshLayout?.isRefreshing = false
                }
                rv_ongoing_orders.layoutManager = LinearLayoutManager(context)
                rv_ongoing_orders.adapter = OngoingDeliveryOrderAdapter(
                    context,
                    it.result,
                    statusId,
                    deliveryOrderViewModel.getOnGoingOrderListResponse.value?.result,
                    object : OngoingDeliveryOrderAdapter.OnclickListener {
                        override fun onClick(orderData: DeliveryOrderListResponse.DeliveryOrderData) {
                            //dialogConfirm(orderData.order_id)
                        }

                        override fun onClick(position: Int,orderId: String?) {
                            orderId?.let { orderId -> updateDeliveryStatus(orderId) }
                        }
                    })
            })
            deliveryOrderViewModel.errorOrderList.observe(context, Observer {
                if (swipeRefreshLayout?.isRefreshing == true){
                    swipeRefreshLayout?.isRefreshing = false
                }
                ErrorUtil.handlerGeneralError(context, it)
            })
        }
    }

    fun updateDeliveryStatus(orderId: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Would you like to accept the order?")

        // add the buttons

        // add the buttons
        builder.setPositiveButton("Accept", DialogInterface.OnClickListener { dialog, which ->
                deliveryOrderViewModel.updateOrder(
                    accessToken = prefs.accessToken,
                    orderId = orderId,
                    statusId = "12",
                    comment = ""
                )
            getData()

        })
        builder.setNeutralButton("Reject", DialogInterface.OnClickListener { dialog, which ->
            deliveryOrderViewModel.updateOrder(
                accessToken = prefs.accessToken,
                orderId = orderId,
                statusId = "10",
                comment = ""
            )
            getData()
        })

        // create and show the alert dialog

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            getOrders()
        }
    }

}
