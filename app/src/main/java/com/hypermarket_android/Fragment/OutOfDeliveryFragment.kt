package com.hypermarket_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hypermarket_android.Adapter.DeliveryOutOrderAdapter
import com.hypermarket_android.Adapter.OngoingDeliveryOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.DeliveryOrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.viewModel.DeliveryOrderListViewModel
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*


class OutOfDeliveryFragment : BaseFragment() {
    private lateinit var deliveryOrderViewModel: DeliveryOrderListViewModel
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_out_of_delivery, container, false)
        deliveryOrderViewModel = ViewModelProvider(this).get(DeliveryOrderListViewModel::class.java)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        initViews()
        initControl()
        return view
    }
    override fun initViews() {
        swipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            deliveryOrderViewModel.getOrdersOutDelivery(
                sharedPreferenceUtil.accessToken,
                "3",
                sharedPreferenceUtil.userId
            )
        })
        deliveryOrderViewModel.getOrdersOutDelivery(
            sharedPreferenceUtil.accessToken,
            "3",
            sharedPreferenceUtil.userId
        )
    }
    override fun initControl() {
        deliveryOrderViewModel.deliveryOutOrderListResponse.observe(this.requireActivity(), Observer {
            if (swipeRefreshLayout?.isRefreshing == true){
                swipeRefreshLayout?.isRefreshing = false
            }
            Log.e("SuccessCalled",it.result.toString())
            rv_ongoing_orders.layoutManager = LinearLayoutManager(this.requireActivity())
            rv_ongoing_orders.adapter = DeliveryOutOrderAdapter(
                activity!!,
                it.result,
                object : DeliveryOutOrderAdapter.OnclickListener {
                    override fun onClick(orderData: DeliveryOrderListResponse.DeliveryOrderData) {
                        //dialogConfirm(orderData.order_id)
                    }

                    override fun onClick(position: Int) {

                    }

                })
        })

        deliveryOrderViewModel.errorOrderList.observe(this.requireActivity(), Observer {
            if (swipeRefreshLayout?.isRefreshing == true){
                swipeRefreshLayout?.isRefreshing = false
            }
            Log.e("SuccessCalled1","it.result.toString()")
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)
        })
    }


}