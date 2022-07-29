package com.hypermarket_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CancelledOrderAdapter
import com.hypermarket_android.Adapter.CancelledAdapter
import com.hypermarket_android.Adapter.NewPastOrderAdapter

import com.hypermarket_android.R
import com.hypermarket_android.Singleton
import com.hypermarket_android.activity.OrderItemsActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.GetOrdersList
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.fragment_cancelled_orders.*

class CancelledOrdersFragment : BaseFragment() {
    lateinit var rv: RecyclerView
    private lateinit var orderListViewModel: OrderListViewModel
    val dataCollection =  ArrayList<GetOrdersList.OrderData>()
    var pageNo = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cancelled_orders, container, false)
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        rv = view.findViewById<RecyclerView>(R.id.rv_cancelled_orders)
        initViews()
        initControl()
        dataCollection.clear()
        return view
    }
    override fun initViews() {
//        ProgressDialogUtils.getInstance().showProgress(this.requireActivity(), false)
        orderListViewModel.getNewOrdersCancelled(
            sharedPreferenceUtil.accessToken,
            "5",
            sharedPreferenceUtil.userId,
            "1"
        )
    }
    override fun initControl() {
        rv.layoutManager = LinearLayoutManager(this.requireActivity())
        val orderAdapter = CancelledAdapter(
            this.requireActivity(),
            dataCollection,
            object : CancelledAdapter.OnclickListener {
                override fun onClickItem(
                    orderData: GetOrdersList.OrderData,
                    position: Int
                ) {
//                    Singleton.orderData = orderData
                    val intent = Intent(activity, OrderItemsActivity::class.java)
                    intent.putExtra("order_id",orderData.order_id)
                    intent.putExtra("fragment_position","5")
                    activity!!.startActivity(intent)
                }

                override fun onClick(orderData: GetOrdersList.OrderData) {
                    ProgressDialogUtils.getInstance()
                        .showProgress(requireActivity(), false)
                    orderListViewModel.getInvoiceDetails(
                        sharedPreferenceUtil.accessToken,
                        sharedPreferenceUtil.userId,
                        orderData.order_id
                    )
                }

            })
        rv.adapter = orderAdapter
        activity?.let { activity ->
            orderListViewModel.newCancelledOrderListResponse.observe(activity, Observer {
                Log.e("observerpastorder","observing")
                if (pageNo == 1){
                    dataCollection.clear()
                }
                ProgressDialogUtils.getInstance().hideProgress()
                if (it.data.size > 0) {
                    dataCollection.addAll(it.data)
//                    Singleton.allPastOrderData = dataCollection
                    orderAdapter.notifyDataSetChanged()
                }
                orderAdapter.setOnBottomReachedListener(object :
                    OnBottomReachedListener {
                    override fun onBottomReached(position: Int) {
                        ProgressDialogUtils.getInstance().showProgress(activity,true)
                        Log.e("onBottomReached", position.toString())
//                    if ((!productViewModel.isDataEnd.value!!) && (!isBottomError) == true) {
                        Log.e("bottom==", "bottom1")
//                        productListGridAdapter!!.footerVisibility(false, true)
                        pageNo = it.current_page!!.toInt() + 1
                        orderListViewModel.getNewOrdersCancelled(
                            sharedPreferenceUtil.accessToken,
                            "5",
                            sharedPreferenceUtil.userId,
                            (it.current_page!!.toInt() + 1).toString(),
                        )
//                }
                    }
                })
            })
        }

        orderListViewModel.errorOrderList.observe(this.requireActivity(), Observer {
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)

        })
    }

    override fun onResume() {
        super.onResume()
        pageNo = 1
        orderListViewModel.getNewOrdersCancelled(
            sharedPreferenceUtil.accessToken,
            "5",
            sharedPreferenceUtil.userId,
            pageNo.toString()
        )
    }
}
