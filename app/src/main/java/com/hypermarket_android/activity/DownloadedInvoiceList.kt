package com.hypermarket_android.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.downloadInvoice.Data
import com.hypermarket_android.ui.downloadInvoice.DownloadInvoiceAdapter
import com.hypermarket_android.ui.downloadInvoice.DownloadInvoiceViewModel
import com.hypermarket_android.util.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_downloaded_invoice_list.*
import kotlinx.android.synthetic.main.invoice_dialog_detail.*

class DownloadedInvoiceList : BaseActivity() {
    private lateinit var downloadInvoiceViewModel: DownloadInvoiceViewModel
    lateinit var inoviceDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloaded_invoice_list)
        downloadInvoiceViewModel = ViewModelProvider(this).get(DownloadInvoiceViewModel::class.java)
        btn_back.setOnClickListener {
            finish()
        }

        invoice_list()

        downloadInvoiceObservable()
    }

    override fun initViews() {

    }

    override fun initControl() {

    }

    fun invoice_list(){
        if (Helper.isNetworkAvailable(this!!)) {
            downloadInvoiceViewModel.hitDownloadInvoiceApi(
                sharedPreference.accessToken,
               sharedPreference.userId
            )
        }
        else {
            Toast.makeText(
                this,
                resources.getString(R.string.message_no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun downloadInvoiceObservable() {
        downloadInvoiceViewModel.downloadInvoiceResponse.observe(this, Observer {
            showToast(it.message)
            val adp1 = DownloadInvoiceAdapter(it.data,object :DownloadInvoiceAdapter.itemInvoiceClickListner{
                override fun onviewInvoiceClick(position: Int) {
                    //editdeleteitem(it[0].advertisementlist[position]._id)
                    showInvoiceDetailsDialog(it.data[position])
                }
            })
            rv_download_invoice.setHasFixedSize(true)
            rv_download_invoice.adapter = adp1
        })
        downloadInvoiceViewModel.errorDownloadInvoice.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
//            showToast(it.message?:"")
            ErrorUtil.handlerGeneralError(this, it)

        })

        downloadInvoiceViewModel.mProgessDownloadInvoice.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }


    fun showInvoiceDetailsDialog(it: Data) {
        inoviceDialog = this.let { Dialog(it) }
        inoviceDialog?.setContentView(R.layout.invoice_dialog_detail)
        inoviceDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        inoviceDialog?.setCancelable(true)
        inoviceDialog?.setCanceledOnTouchOutside(true)
        inoviceDialog?.order_id.text = "#" + it.order_id ?: ""
        inoviceDialog?.created_At.text = convertDate(it.created_at)
        inoviceDialog?.total_amount_pay.text = it.total_payable_amount + " AED"
        inoviceDialog?.net_payable_amount.text = it.total_payable_amount + " AED"
        inoviceDialog?.done.setOnClickListener {
            inoviceDialog.dismiss()
        }

        inoviceDialog?.show()

    }
}