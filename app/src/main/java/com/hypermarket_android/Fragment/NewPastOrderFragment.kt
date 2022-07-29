package com.hypermarket_android.Fragment

import android.app.Dialog
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
import com.hypermarket_android.Adapter.NewPastOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.Singleton
import com.hypermarket_android.activity.OrderItemsActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.OrderInvoiceResponse
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.fragment_past_orders.*
import kotlinx.android.synthetic.main.invoice_dialog_detail.*


class NewPastOrderFragment : BaseFragment() {
    lateinit var inoviceDialog: Dialog
    private lateinit var orderListViewModel: OrderListViewModel
    val dataCollection =  ArrayList<OrderListResponse.OrderData>()
    var pageNo = 1
    lateinit var rv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_past_order, container, false)
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        rv = view.findViewById<RecyclerView>(R.id.rv_past_orders)
        initViews()
        initControl()
        dataCollection.clear()
        return view
    }

    override fun initViews() {
//        ProgressDialogUtils.getInstance().showProgress(this.requireActivity(), false)
//        orderListViewModel.getNewOrdersPast(
//            sharedPreferenceUtil.accessToken,
//            "4",
//            sharedPreferenceUtil.userId,
//            "1"
//        )
    }

    override fun initControl() {
        rv.layoutManager = LinearLayoutManager(this.requireActivity())
        val orderAdapter = NewPastOrderAdapter(
            this.requireActivity(),
            dataCollection,
            object : NewPastOrderAdapter.OnclickListener {
                override fun onClickItem(
                    orderData: OrderListResponse.OrderData,
                    position: Int
                ) {
                    Singleton.orderData = orderData
                    val intent = Intent(activity, OrderItemsActivity::class.java)
                    intent.putExtra("order_position",position.toString())
                    intent.putExtra("fragment_position","4")
                    activity!!.startActivity(intent)
                }

                override fun onClick(orderData: OrderListResponse.OrderData) {
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
            orderListViewModel.pastorderListResponse.observe(activity, Observer {
                Log.e("observerpastorder","observing")
                if (pageNo == 1){
                    dataCollection.clear()
                }
                ProgressDialogUtils.getInstance().hideProgress()
                if (it.data.size > 0) {
                    dataCollection.addAll(it.data)
                    Singleton.allPastOrderData = dataCollection
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
                        orderListViewModel.getNewOrdersPast(
                            sharedPreferenceUtil.accessToken,
                            "4",
                            sharedPreferenceUtil.userId,
                            (it.current_page!!.toInt() + 1).toString(),
                        )
//                }
                    }
                })
            })
        }

        orderListViewModel.errorOrderList.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)

        })

        orderListViewModel.orderInvoiceResponse.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showInvoiceDetailsDialog(it)
        })

    }

    override fun onResume() {
        super.onResume()
        pageNo = 1
        Log.e("testonresume","testingonresume")
        orderListViewModel.getNewOrdersPast(
            sharedPreferenceUtil.accessToken,
            "4",
            sharedPreferenceUtil.userId,
            pageNo.toString()
        )
    }

    fun showInvoiceDetailsDialog(it: OrderInvoiceResponse) {
        inoviceDialog = requireActivity().let { Dialog(it) }
        inoviceDialog?.setContentView(R.layout.invoice_dialog_detail)
        inoviceDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        inoviceDialog?.setCancelable(true)
        inoviceDialog?.setCanceledOnTouchOutside(true)
        inoviceDialog?.order_id.text = "#" + it.data?.order_id ?: ""
        inoviceDialog?.created_At.text = convertDate(it.data!!.created_at)
        inoviceDialog?.total_amount_pay.text = it.data?.total_payable_amount + " AED"
        inoviceDialog?.net_payable_amount.text = it.data?.total_payable_amount + " AED"
        inoviceDialog?.done.setOnClickListener {
            inoviceDialog.dismiss()
        }

        inoviceDialog?.show()

    }
}
