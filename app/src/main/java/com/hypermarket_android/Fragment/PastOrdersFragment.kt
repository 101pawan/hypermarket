package com.hypermarket_android.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.PastOrderAdapter

import com.hypermarket_android.R
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.OrderInvoiceResponse
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.viewModel.OrderListViewModel
import kotlinx.android.synthetic.main.fragment_past_orders.*
import kotlinx.android.synthetic.main.invoice_dialog_detail.*

class PastOrdersFragment : BaseFragment() {
    lateinit var inoviceDialog: Dialog
    private lateinit var orderListViewModel: OrderListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_past_orders, container, false)

        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)


        initViews()
        initControl()
        return view
    }

    override fun initViews() {
        ProgressDialogUtils.getInstance().showProgress(this.requireActivity(), false)
        orderListViewModel.getOrdersPast(
            sharedPreferenceUtil.accessToken,
            "2",
            sharedPreferenceUtil.userId
        )
    }

    override fun initControl() {
        orderListViewModel.pastorderListResponse.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            rv_past_orders.layoutManager = LinearLayoutManager(this.requireActivity())
            rv_past_orders.adapter = PastOrderAdapter(
                this.requireActivity(),
                it.data,
                object : PastOrderAdapter.OnclickListener {
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
        })

        orderListViewModel.errorOrderList.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this.requireActivity(), it)

        })

        orderListViewModel.orderInvoiceResponse.observe(this.requireActivity(), Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showInvoiceDetailsDialog(it)
        })

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
