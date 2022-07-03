package com.hypermarket_android.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.CancelListAdapter
import com.hypermarket_android.Adapter.OrderProductAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.AddOrderResponse
import com.hypermarket_android.dataModel.CancelListResponse
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.activity_order_summary.btn_back
import kotlinx.android.synthetic.main.activity_order_summary.btn_cancel
import kotlinx.android.synthetic.main.activity_order_summary.btn_continue
import kotlinx.android.synthetic.main.activity_order_summary.net_payable_amount
import kotlinx.android.synthetic.main.activity_order_summary.order_date
import kotlinx.android.synthetic.main.activity_order_summary.order_id
import kotlinx.android.synthetic.main.activity_order_summary.products_recyclerview
import kotlinx.android.synthetic.main.activity_order_summary.total_amount_pay
import kotlinx.android.synthetic.main.activity_order_summary.tv_address_detail
import kotlinx.android.synthetic.main.activity_order_summary.tv_user_name
import kotlinx.android.synthetic.main.cancel_dialog_confirm.*
import kotlinx.android.synthetic.main.cancel_dialog_confirm.tvMesg
import kotlinx.android.synthetic.main.cancel_success_dialog.*
import kotlinx.android.synthetic.main.cancellation_list_dialog.*
import kotlin.math.roundToInt


class OrderSummaryActivity : BaseActivity() {
    companion object {
        var addOrderResponse: AddOrderResponse? = null
        var status=0
        var payment_mode = ""
        var shipingCharges = ""
    }
    private lateinit var orderListViewModel: OrderListViewModel
    private var cancellationList: ArrayList<CancelListResponse.CancelModel> = ArrayList()
    lateinit var cancelListDialog: Dialog
    lateinit var cancelSuccessDialog: Dialog
    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        initViews()
        initControl()
    }
    override fun initViews() {
        delivery_charge_value.text = "$shipingCharges ${getString(R.string.aed)}"
        Log.e("checkcashondelibvery",payment_mode)
        if (payment_mode == "1"){
            cod.visibility = View.GONE
        }else{
            cod.visibility = View.VISIBLE
        }
        when(status){
            2->{
                right_icon.background= ContextCompat.getDrawable(this,R.drawable.circle_bg_red)
                order_placed.text = resources.getString(R.string.your_order_has_been_failed)
                linearPay.visibility = View.GONE
                ivOrderStatus.setImageResource(R.drawable.white_check_icon)
            }
        }
        dialog = Dialog(this)
        cancelListDialog = Dialog(this)
        cancelSuccessDialog = Dialog(this)
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        ProgressDialogUtils.getInstance().showProgress(this,true)
        orderListViewModel.getCancellationList(sharedPreference.accessToken)
        products_recyclerview.layoutManager = LinearLayoutManager(this)
        products_recyclerview.adapter = OrderProductAdapter(
            this,
            addOrderResponse?.order_summary!!.order_products!!,
            addOrderResponse?.order_summary!!.expected_delivery
        )
        sub_address_detail.text =  " ${addOrderResponse!!.order_summary.shipping_detail?.name ?: ""}, ${addOrderResponse!!.order_summary.shipping_detail?.alt_mobile_number ?: ""}"

        tv_address_detail.text =
            " ${addOrderResponse!!.order_summary.shipping_detail?.house_number ?: ""}, ${addOrderResponse!!.order_summary.shipping_detail?.building_name ?: ""}, ${addOrderResponse!!.order_summary.shipping_detail?.street ?: ""} "
        tv_user_name.text = sharedPreference.full_name
        net_payable_amount.text = addOrderResponse!!.order_summary.total_payable_amount + resources.getString(R.string.aed)
        total_amount_pay.text = (((addOrderResponse!!.order_summary.total_payable_amount).toDouble() * 100).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
        order_id.text = "#" + addOrderResponse!!.order_summary.order_id
        order_date.text = addOrderResponse!!.order_summary.order_date
        when(addOrderResponse!!.order_summary.order_status){
            "1" -> {
                iv_dispatched.setBackgroundResource(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
            }
            "2" -> {
                iv_dispatched.setBackgroundResource(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_shipped.setBackgroundResource(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
            }
            "3" -> {
                iv_dispatched.setBackgroundResource(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_shipped.setBackgroundResource(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.setBackgroundResource(R.drawable.circle_bg)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
            }
            "4" -> {
                iv_dispatched.setBackgroundResource(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_shipped.setBackgroundResource(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.setBackgroundResource(R.drawable.circle_bg)
                out_for_Delivery.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.setBackgroundResource(R.drawable.circle_bg)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_delivered_food.setBackgroundResource(R.drawable.circle_bg)
            }
            else -> {

            }
        }
    }

    override fun initControl() {
        orderListViewModel.cancelOrderResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showToast(this, it.message)
            cancelSuccessDialog()
        })

        orderListViewModel.cancelListResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            cancellationList = it.data
        })

        orderListViewModel.errorOrderList.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        btn_back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        btn_cancel.setOnClickListener {
            dialogConfirm(addOrderResponse!!.order_summary.order_id)
        }

        btn_continue.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }
    private fun dialogConfirm(order_id: String) {
        dialog.setContentView(R.layout.cancel_dialog_confirm)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.tvMesg.text = resources.getString(R.string.are_you_sure_you_want_to_cancel_thisorder)
        dialog.no?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.yes?.setOnClickListener {
            dialog.dismiss()
            showCancellationListDialog(order_id)
        }
        dialog.show()
    }
    private fun showCancellationListDialog(order_id: String) {
        var cancelItemId = ""
        cancelListDialog.setContentView(R.layout.cancellation_list_dialog)
        cancelListDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelListDialog.setCancelable(true)
        cancelListDialog.setCanceledOnTouchOutside(true)
        cancelListDialog.cancellationList?.adapter = CancelListAdapter(
            this,
            cancellationList,
            object : CancelListAdapter.onClickListener {
                override fun onClick(cancelData: CancelListResponse.CancelModel, type: String) {
                    if (type == "cancel") {
                        cancelItemId = cancelData.id
                    }
                }
            })
        cancelListDialog.submit?.setOnClickListener {
            if (cancelItemId != "") {
                cancelListDialog.dismiss()
                ProgressDialogUtils.getInstance()
                    .showProgress(cancelListDialog.context, false)
                orderListViewModel.cancelOrder(
                    sharedPreference.accessToken, cancelItemId
                    , order_id
                )
                cancelListDialog.dismiss()
            } else {
                showToast(this, resources.getString(R.string.select_a_reason_for_ancellation))
            }
        }
        cancelListDialog.show()
    }
    private fun cancelSuccessDialog() {
        cancelSuccessDialog.setContentView(R.layout.cancel_success_dialog)
        cancelSuccessDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cancelSuccessDialog.setCancelable(false)
        cancelSuccessDialog.done?.setOnClickListener {
            cancelSuccessDialog.dismiss()
            right_icon.background = ContextCompat.getDrawable(this,R.drawable.circle_bg_red)
            order_placed.text = resources.getString(R.string.order_cancelled)
            linearPay.visibility = View.GONE
            ivOrderStatus.setImageResource(R.drawable.white_check_icon)
        }
        cancelSuccessDialog.show()
    }
    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

}