package com.hypermarket_android.Fragment

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CancelListAdapter
import com.hypermarket_android.Adapter.OrdersListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.Singleton
import com.hypermarket_android.activity.MyOrderActivity
import com.hypermarket_android.activity.OrderItemsActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.CancelListResponse
import com.hypermarket_android.dataModel.GetOrdersList
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.cancel_success_dialog.*
import kotlinx.android.synthetic.main.cancellation_list_dialog.*
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*

class OngoingOrderListFragment : BaseFragment() {
    private lateinit var orderListViewModel: OrderListViewModel
    private var cancellationList: ArrayList<CancelListResponse.CancelModel> = ArrayList()
    lateinit var dialog: Dialog
    lateinit var cancelListDialog: Dialog
    lateinit var cancelSuccessDialog: Dialog
    lateinit var rv: RecyclerView

    val dataCollection =  ArrayList<GetOrdersList.OrderData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ongoing_order_lst_fragment, container, false)
        dialog = Dialog(this.requireActivity())
        cancelListDialog = Dialog(this.requireActivity())
        cancelSuccessDialog = Dialog(this.requireActivity())
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        rv = view.findViewById<RecyclerView>(R.id.rv_ongoing_orders)
        initViews()
        initControl()


        return view
    }
    override fun initViews() {
        Log.e("OngoingOrder","OngoingOrderListFragment")

        orderListViewModel.getCancellationList(sharedPreferenceUtil.accessToken)

    }

    override fun onResume() {
        super.onResume()
        dataCollection.clear()
        ProgressDialogUtils.getInstance().showProgress(this.requireActivity(), false)
        orderListViewModel.getNewOrdersList(
            sharedPreferenceUtil.accessToken,
            "2,3,11,12",
            sharedPreferenceUtil.userId,
            "1"
        )
    }

    override fun initControl() {

        rv.layoutManager = LinearLayoutManager(activity)
        val orderAdapter = OrdersListAdapter(
            activity!!,
            dataCollection,
            object : OrdersListAdapter.OnclickListener {
                override fun onClick(orderData: GetOrdersList.OrderData,position:Int) {
//                    Singleton.orderData = orderData
//                        dialogConfirm(orderData.order_id)
                    val intent = Intent(activity, OrderItemsActivity::class.java)
                    intent.putExtra("order_id",orderData.order_id)
                    intent.putExtra("fragment_position","2")
//                        intent.putExtra("products",orderData.order_products)
                    activity!!.startActivity(intent)
                }

            })
        rv.adapter = orderAdapter
        activity?.let { activity ->
            orderListViewModel.orderListResponse.observe(activity, Observer {
                ProgressDialogUtils.getInstance().hideProgress()
                if (it.data.size > 0) {
                    dataCollection.addAll(it.data)
//                    Singleton.allOrderData = dataCollection

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
                        orderListViewModel.getNewOrdersList(
                            sharedPreferenceUtil.accessToken,
                            "2,3,11,12",
                            sharedPreferenceUtil.userId,
                            (it.current_page!!.toInt() + 1).toString(),
                        )
//                }
                    }
                })
            })
        }

        orderListViewModel.cancelOrderResponse.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showToast(this.requireActivity(), it.message)
            cancelSuccessDialog()
        })
        orderListViewModel.cancelListResponse.observe(this.requireActivity(), Observer {
            cancellationList = it.data
        })
        orderListViewModel.errorOrderList.observe(this.requireActivity(), Observer {
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)
        })
    }
//    private fun dialogConfirm(order_id: String) {
//        dialog?.setContentView(R.layout.cancel_dialog_confirm)
//        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog?.setCancelable(true)
//        dialog?.setCanceledOnTouchOutside(true)
//        dialog?.tvMesg.text = resources.getString(R.string.are_you_sure_you_want_to_cancel_thisorder)
//        dialog?.no?.setOnClickListener {
//            dialog?.dismiss()
//        }
//        dialog?.yes?.setOnClickListener {
//            dialog?.dismiss()
//            showCancellationListDialog(order_id)
//        }
//        dialog?.show()
//    }
    private fun showCancellationListDialog(order_id: String) {
        var cancelItemId = ""
        cancelListDialog?.setContentView(R.layout.cancellation_list_dialog)
        cancelListDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelListDialog?.setCancelable(true)
        cancelListDialog?.setCanceledOnTouchOutside(true)
        cancelListDialog?.cancellationList?.adapter = CancelListAdapter(
            this.requireActivity(),
            cancellationList,
            object : CancelListAdapter.onClickListener {
                override fun onClick(cancelData: CancelListResponse.CancelModel, type: String) {
                    if (type == "cancel") {
                        cancelItemId = cancelData.id
                    }
                }

            })
        cancelListDialog?.submit?.setOnClickListener {
            if (cancelItemId != "") {
                cancelListDialog?.dismiss()
                ProgressDialogUtils.getInstance()
                    .showProgress(cancelListDialog.context, false)
                orderListViewModel.cancelOrder(
                    sharedPreferenceUtil.accessToken, cancelItemId
                    , order_id
                )
                cancelListDialog?.dismiss()
            } else {
                showToast(this.requireActivity(), resources.getString(R.string.please_select_reason_for_cancel))
            }
        }
        cancelListDialog?.show()
    }
    private fun cancelSuccessDialog() {
        cancelSuccessDialog?.setContentView(R.layout.cancel_success_dialog)
        cancelSuccessDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelSuccessDialog?.setCancelable(false)
        cancelSuccessDialog?.done?.setOnClickListener {
            cancelSuccessDialog.dismiss()
            orderListViewModel.getNewOrdersList(
                sharedPreferenceUtil.accessToken,
                "2",
                sharedPreferenceUtil.userId,
            "1"
            )
        }
        cancelSuccessDialog?.show()

    }
}
