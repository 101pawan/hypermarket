package com.hypermarket_android.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hypermarket_android.Adapter.DeliveryCancelListAdapter
import com.hypermarket_android.Adapter.DeliveryCanceledOrderAdapter
import com.hypermarket_android.Adapter.OngoingDeliveryOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.DeliveryOrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.viewModel.DeliveryOrderListViewModel
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*


class DeleveryCancelFragment : BaseFragment() {
    private lateinit var deliveryOrderViewModel: DeliveryOrderListViewModel
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delevery_cancel, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        deliveryOrderViewModel = ViewModelProvider(this).get(DeliveryOrderListViewModel::class.java)
        initViews()
        initControl()
        return view
    }
    override fun initViews() {
        swipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            deliveryOrderViewModel.getOrdersCancelled(
                sharedPreferenceUtil.accessToken,
                "5",
                sharedPreferenceUtil.userId
            )
        })
        deliveryOrderViewModel.getOrdersCancelled(
            sharedPreferenceUtil.accessToken,
            "5",
            sharedPreferenceUtil.userId
        )
    }
    override fun initControl() {
        activity?.let { context ->
            deliveryOrderViewModel.deliveryCanceledOrderListResponse.observe(context, Observer {
                if (swipeRefreshLayout?.isRefreshing == true){
                    swipeRefreshLayout?.isRefreshing = false
                }
                rv_ongoing_orders.layoutManager = LinearLayoutManager(context)
                rv_ongoing_orders.adapter = DeliveryCanceledOrderAdapter(
                    activity!!,
                    it.result)
            })
            deliveryOrderViewModel.errorOrderList.observe(context, Observer {
                if (swipeRefreshLayout?.isRefreshing == true){
                    swipeRefreshLayout?.isRefreshing = false
                }
                ErrorUtil.handlerGeneralError(context, it)
            })
        }
    }
}
