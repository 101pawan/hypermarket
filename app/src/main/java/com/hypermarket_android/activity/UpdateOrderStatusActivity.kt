package com.hypermarket_android.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.CancelListAdapter
import com.hypermarket_android.Adapter.DeliveryCancelListAdapter
import com.hypermarket_android.Adapter.DeliveryPastOrderAdapter
import com.hypermarket_android.Adapter.DeliveryStatusListAdapter
import com.hypermarket_android.Adapter.OngoingDeliveryOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.CancelListResponse
import com.hypermarket_android.dataModel.DeliverOrderStatusResponseList
import com.hypermarket_android.dataModel.DeliveryOrderStatusReasonResponseList
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.DeliveryOrderListViewModel
import kotlinx.android.synthetic.main.activity_update_order_status.*
import kotlinx.android.synthetic.main.activity_update_order_status.submit
import kotlinx.android.synthetic.main.cancel_dialog_confirm.*
import kotlinx.android.synthetic.main.cancel_dialog_confirm.tvMesg
import kotlinx.android.synthetic.main.cancel_success_dialog.*
import kotlinx.android.synthetic.main.cancellation_list_dialog.*


class UpdateOrderStatusActivity : BaseActivity() {
    companion object {
        var orderId: String? = null
    }
    private lateinit var deliveryOrderViewModel: DeliveryOrderListViewModel
    var statusId = "2"
    private var cancellationList: ArrayList<DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData> = ArrayList()
    lateinit var dialog: Dialog
    lateinit var cancelListDialog: Dialog
    lateinit var cancelSuccessDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_order_status)
        deliveryOrderViewModel = ViewModelProvider(this).get(DeliveryOrderListViewModel::class.java)
        dialog = Dialog(this)
        cancelListDialog = Dialog(this)
        cancelSuccessDialog = Dialog(this)
        initViews()
        initControl()
    }
    override fun initViews() {
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        submit.setOnClickListener(View.OnClickListener {
            if (statusId == "1" || statusId == "5"){
               dialogConfirm(orderId.toString())
            }else{
                progressbar.visibility = View.VISIBLE
                deliveryOrderViewModel.updateOrder(
                    sharedPreference.accessToken,
                    orderId.toString(),
                    statusId,
                    "",
                    et_comment.text.toString()
                )
            }
        })
        progressbar.visibility = View.VISIBLE
        deliveryOrderViewModel.getOrderStatusList(
            sharedPreference.accessToken,
            sharedPreference.userId
        )
        deliveryOrderViewModel.getOrderReasonList(sharedPreference.accessToken)
    }
    override fun initControl() {
        deliveryOrderViewModel.orderStatusListResponse.observe(this, Observer {
            progressbar.visibility = View.GONE
            status_list.layoutManager = LinearLayoutManager(this)
            status_list.adapter = DeliveryStatusListAdapter(
                this,
                it.result,
                object : DeliveryStatusListAdapter.OnClickListener {
                    override fun onClick(
                        orderData: DeliverOrderStatusResponseList.DeliveryOrderStatusData,
                        type: String
                    ) {
                        statusId = orderData.id.toString()
                    }

                })
        })
        deliveryOrderViewModel.orderReasonListResponse.observe(this, Observer {
           // progressbar.visibility = View.GONE
            cancellationList = it.result

        })
        deliveryOrderViewModel.orderUpdateResponse.observe(this,{
            progressbar.visibility = View.GONE
            if (statusId == "1" || statusId == "5"){
                cancelSuccessDialog()
            }else{
                showToast(it.message.toString())
                finish()
            }

        })
        deliveryOrderViewModel.errorOrderList.observe(this, Observer {
            progressbar.visibility = View.GONE
            ErrorUtil.handlerGeneralError(this, it)
        })
    }
    private fun dialogConfirm(order_id: String) {
        dialog?.setContentView(R.layout.cancel_dialog_confirm)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.tvMesg.text = resources.getString(R.string.are_you_sure_you_want_to_cancel_thisorder)
        dialog?.no?.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.yes?.setOnClickListener {
            dialog?.dismiss()
            showCancellationListDialog(order_id)
        }
        dialog?.show()
    }
    private fun showCancellationListDialog(order_id: String) {
        var cancelItemId = ""
        cancelListDialog?.setContentView(R.layout.cancellation_list_dialog)
        cancelListDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelListDialog?.setCancelable(true)
        cancelListDialog?.setCanceledOnTouchOutside(true)
        cancelListDialog?.cancellationList?.adapter = DeliveryCancelListAdapter(
            this,
            cancellationList,
            object : DeliveryCancelListAdapter.OnClickListener {
                override fun onClick(cancelData: DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData, type: String) {
                    if (type == "cancel") {
                        cancelItemId = cancelData.id.toString()
                    }
                }
            })
        cancelListDialog?.submit?.setOnClickListener {
            if (cancelItemId != "") {
                cancelListDialog?.dismiss()
                ProgressDialogUtils.getInstance()
                    .showProgress(cancelListDialog.context, false)
                    deliveryOrderViewModel.updateOrder(
                    sharedPreference.accessToken,
                    order_id,
                    statusId,
                    cancelItemId,
                    et_comment.text.toString()
                )
                cancelListDialog?.dismiss()
            } else {
                showToast(this, resources.getString(R.string.please_select_reason_for_cancel))
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
            finish()
        }
        cancelSuccessDialog?.show()

    }
}