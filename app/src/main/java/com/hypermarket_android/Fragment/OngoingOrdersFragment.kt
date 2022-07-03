package com.hypermarket_android.Fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.CancelListAdapter
import com.app.pharmadawa.ui.notification.OngoingOrderAdapter

import com.hypermarket_android.R
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.CancelListResponse
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.cancel_dialog_confirm.*
import kotlinx.android.synthetic.main.cancel_dialog_confirm.tvMesg
import kotlinx.android.synthetic.main.cancel_success_dialog.*
import kotlinx.android.synthetic.main.cancellation_list_dialog.*
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*

/**
 * A simple [Fragment] subclass.
 */
class OngoingOrdersFragment : BaseFragment() {
    private lateinit var orderListViewModel: OrderListViewModel
    private var cancellationList: ArrayList<CancelListResponse.CancelModel> = ArrayList()
    lateinit var dialog: Dialog
    lateinit var cancelListDialog: Dialog
    lateinit var cancelSuccessDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ongoing_orders, container, false)
        dialog = Dialog(this.requireActivity())
        cancelListDialog = Dialog(this.requireActivity())
        cancelSuccessDialog = Dialog(this.requireActivity())
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        initViews()
        initControl()
        return view
    }
    override fun initViews() {
        orderListViewModel.getCancellationList(sharedPreferenceUtil.accessToken)
        orderListViewModel.getOrders(
            sharedPreferenceUtil.accessToken,
            "1",
            sharedPreferenceUtil.userId
        )
    }
    override fun initControl() {
        orderListViewModel.orderListResponse.observe(this.requireActivity(), Observer {
            rv_ongoing_orders.layoutManager = LinearLayoutManager(this.requireActivity())
            rv_ongoing_orders.adapter = OngoingOrderAdapter(
                activity!!,
                it.data,
                object : OngoingOrderAdapter.OnclickListener {
                    override fun onClick(orderData: OrderListResponse.OrderData) {
                        dialogConfirm(orderData.order_id)
                    }

                })
        })
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
            orderListViewModel.getOrders(
                sharedPreferenceUtil.accessToken,
                "1",
                sharedPreferenceUtil.userId
            )
        }
        cancelSuccessDialog?.show()

    }
}
