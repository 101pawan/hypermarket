package com.hypermarket_android.Fragment.myorder

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CancelListAdapter
import com.hypermarket_android.Adapter.DeliveryCancelListAdapter
import com.hypermarket_android.Adapter.NewOrdersAdapter
import com.hypermarket_android.Adapter.OrdersListAdapter
import com.hypermarket_android.Fragment.ReturnType
import com.hypermarket_android.R
import com.hypermarket_android.Singleton
import com.hypermarket_android.activity.OrderItemsActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.CancelListResponse
import com.hypermarket_android.dataModel.DeliveryOrderStatusReasonResponseList
import com.hypermarket_android.dataModel.GetOrdersList
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.OrderDetailVm
import kotlinx.android.synthetic.main.cancel_dialog_confirm.*
import kotlinx.android.synthetic.main.cancel_success_dialog.*
import kotlinx.android.synthetic.main.cancellation_list_dialog.*
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*

class OrderDetailFragment(val order_id: String,val fragment_position: String): BaseFragment() {
    private lateinit var orderDetailVm: OrderDetailVm 
    private var cancellationList: ArrayList<CancelListResponse.CancelModel> = ArrayList()
    private var reasonCancellationList: ArrayList<DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData> = ArrayList()
    var allOrders:List<OrderListResponse.OrderData> = emptyList()
    lateinit var dialog: Dialog
    lateinit var cancelListDialog: Dialog
    lateinit var cancelSuccessDialog: Dialog
    var selected_position = 0
    var orderPosition = 0
    var productPosition = 0
    var totalQty = 0
    var selectedQty = 0
    var productID = ""
    var barcodeID = ""
    var orderID = ""
    var productPrice = ""
    var returnType: ReturnType? = null
    var alert: AlertDialog? = null

    private var completeListOfOrder: List<OrderListResponse.OrderData>? = null

    val dataCollection =  ArrayList<OrderListResponse.OrderData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.order_detail_fragment, container, false)
//        selected_position = order_id

        dialog = Dialog(this.requireActivity())
        cancelListDialog = Dialog(this.requireActivity())
        cancelSuccessDialog = Dialog(this.requireActivity())
        orderDetailVm = ViewModelProvider(this).get(OrderDetailVm::class.java)
        initViews()
        initControl()
        orderDetailVm.status.observe(requireActivity(), Observer {
            Log.e("orderDetailVm",it.toString())
            if (it == 200){
                activity?.onBackPressed();
            }
        })
        return view
    }
    override fun initViews() {
        orderDetailVm.getCompleteOrdersDetails(
            sharedPreferenceUtil.accessToken,
            order_id,
            sharedPreferenceUtil.userId
        )
        orderDetailVm.getCancellationList(sharedPreferenceUtil.accessToken)
//        orderDetailVm.getOrders(
//            sharedPreferenceUtil.accessToken,
//            fragment_position!!,
//            sharedPreferenceUtil.userId
//        )
        orderDetailVm.getOrderReasonList(sharedPreferenceUtil.accessToken)
    }

    fun mapRecycler(){
        activity.let { view ->
            rv_ongoing_orders?.let { rv->

//                if (fragment_position == "2") {
//                    allOrders = Singleton.allOrderData!!
//                }else{
//
//                    allOrders = Singleton.allPastOrderData!!
//                }
                rv.layoutManager = LinearLayoutManager(view)
                val adapter = NewOrdersAdapter(
                    activity!!,
                    dataCollection,dataCollection,selected_position,fragment_position.toInt(),
                    object :  NewOrdersAdapter.OnclickListener {
                        override fun onClick(selected_position: Int, indexPosition: Int) {
//                            if ( fragment_position == "2"){
//                                allOrders = Singleton.allOrderData!!
//                            }else{
//                                allOrders = Singleton.allPastOrderData!!
//                            }
                            dataCollection.let {
                                Log.e("checkpastorder",it.toString())
                                Log.e("availabe_qty_return",it?.get(indexPosition).toString())
                                Log.e("availabe_qty_return",it?.get(indexPosition)?.available_for_return.toString())
                                totalQty = it?.get(indexPosition)?.available_for_return?.toInt()
                                    ?: 0
                                completeListOfOrder = it
//                                orderPosition = selected_position
//                                productPosition = indexPosition
                                productID =
                                    it?.get(indexPosition)?.product_id.toString()
                                barcodeID =
                                    it?.get(indexPosition)?.barcode_id.toString()
                                orderID =
                                    it?.get(indexPosition)?.order_id.toString()
                                productPrice =
                                    it?.get(indexPosition)?.product_price.toString()

                                openModifyOrder()
                            }
                        }
                    })
                rv.adapter = adapter
                activity?.let { activity ->
                    orderDetailVm.orderListResponse.observe(activity, Observer {
                        ProgressDialogUtils.getInstance().hideProgress()
                        if (it.data.size > 0) {
                            dataCollection.addAll(it.data)
//                    Singleton.allOrderData = dataCollection

                            adapter.notifyDataSetChanged()
                        }
                    })
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapRecycler()
    }

    override fun initControl() {



//        orderDetailVm.orderListResponse.observe(this.requireActivity(), Observer {
//            Log.e("initControl","mapping")
////            mapRecycler()
//        })

        activity?.let {
            orderDetailVm.orderReasonListResponse.observe(it, Observer {
                // progressbar.visibility = View.GONE
                reasonCancellationList = it.result

            })
        }

        orderDetailVm.cancelOrderResponse.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showToast(this.requireActivity(), it.message)
            cancelSuccessDialog()
        })
        orderDetailVm.cancelListResponse.observe(this.requireActivity(), Observer {
            cancellationList = it.data
        })
        orderDetailVm.errorOrderList.observe(this.requireActivity(), Observer {
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)
        })
    }

    fun openModifyOrder(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Update order status")

        // add the buttons

        // add the buttons
        builder.setPositiveButton("Refund", DialogInterface.OnClickListener { dialog, which ->
            Log.e("Refund","Refund")
            returnType = ReturnType.refund
            dialog()
        })
        builder.setNeutralButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            Log.e("Cancel","Cancel")
        })
        builder.setNegativeButton("Replace", DialogInterface.OnClickListener { dialog, which ->
            Log.e("Replace","Replace")
            returnType = ReturnType.replace
            dialog()
        })

        // create and show the alert dialog

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun dialog() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Select Quantity to return")
        val charSequences: MutableList<Int> = ArrayList()
        for (i in 1..totalQty) {
            charSequences.add(i)
        }
        val ar: Array<String?> = arrayOfNulls<String>(charSequences.size)
        for (i in 0 until totalQty) {
            ar.set(i,(i+1).toString())
        }

        val checkedItem = 1
        alertDialog.setSingleChoiceItems(
            ar, checkedItem
        ) { dialog, which ->

            selectedQty = which + 1
            Log.e("testrefundselct",selectedQty.toString()+" == "+which.toString() +" ="+completeListOfOrder)
            completeListOfOrder?.let {
                alert?.dismiss()
                showCancellationListDialog()
            }
//                activity?.onBackPressed();
        }
        alert = alertDialog.create()
        alert?.setCanceledOnTouchOutside(true)
        alert?.show()
    }

    private fun showCancellationListDialog() {
        var cancelItemId = ""
        cancelListDialog?.setContentView(R.layout.cancellation_list_dialog)
        cancelListDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelListDialog?.setCancelable(true)
        cancelListDialog?.setCanceledOnTouchOutside(true)
        cancelListDialog?.cancellationList?.adapter = DeliveryCancelListAdapter(
            activity,
            reasonCancellationList,
            object : DeliveryCancelListAdapter.OnClickListener {
                override fun onClick(cancelData: DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData, type: String) {
                    if (type == "cancel") {
                        cancelItemId = cancelData.id.toString()
                        Log.e("cancelItemId",cancelItemId)
                    }
                }
            })
        cancelListDialog?.submit?.setOnClickListener {
            if (cancelItemId != "") {
                cancelListDialog?.dismiss()
//                ProgressDialogUtils.getInstance()
//                    .showProgress(cancelListDialog.context, false)

                when (returnType){
                    ReturnType.refund -> returnOrder("Refund",cancelItemId)
                    ReturnType.replace -> returnOrder("Replace",cancelItemId)
                }

                cancelListDialog?.dismiss()
            } else {
                activity?.let { it1 -> showToast(it1, resources.getString(R.string.please_select_reason_for_cancel)) }
            }
        }
        cancelListDialog?.show()
    }

    private fun returnOrder(type: String,reason_id:String){
        Log.e("type",type+"=="+reason_id)
        orderDetailVm.refundOrder(
            sharedPreferenceUtil.accessToken,
            orderID,
            productID,
            barcodeID,
            type,
            reason_id,
            sharedPreferenceUtil.userId,
            selectedQty.toString(),
            productPrice
        )
    }


    private fun dialogConfirm() {
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
//            showCancellationListDialog()
//        }
//        dialog?.show()
    }
    //    private fun showCancellationListDialog(order_id: String) {
//        var cancelItemId = ""
//        cancelListDialog?.setContentView(R.layout.cancellation_list_dialog)
//        cancelListDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//        cancelListDialog?.setCancelable(true)
//        cancelListDialog?.setCanceledOnTouchOutside(true)
//        cancelListDialog?.cancellationList?.adapter = CancelListAdapter(
//            this.requireActivity(),
//            cancellationList,
//            object : CancelListAdapter.onClickListener {
//                override fun onClick(cancelData: CancelListResponse.CancelModel, type: String) {
//                    if (type == "cancel") {
//                        cancelItemId = cancelData.id
//                    }
//                }
//
//            })
//        cancelListDialog?.submit?.setOnClickListener {
//            if (cancelItemId != "") {
//                cancelListDialog?.dismiss()
//                ProgressDialogUtils.getInstance()
//                    .showProgress(cancelListDialog.context, false)
//                orderDetailVm.cancelOrder(
//                    sharedPreferenceUtil.accessToken, cancelItemId
//                    , order_id
//                )
//                cancelListDialog?.dismiss()
//            } else {
//                showToast(this.requireActivity(), resources.getString(R.string.please_select_reason_for_cancel))
//            }
//        }
//        cancelListDialog?.show()
//    }
    private fun cancelSuccessDialog() {
        cancelSuccessDialog?.setContentView(R.layout.cancel_success_dialog)
        cancelSuccessDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelSuccessDialog?.setCancelable(false)
        cancelSuccessDialog?.done?.setOnClickListener {
            cancelSuccessDialog.dismiss()
            orderDetailVm.getOrders(
                sharedPreferenceUtil.accessToken,
                "1",
                sharedPreferenceUtil.userId
            )
        }
        cancelSuccessDialog?.show()

    }
}
