package com.hypermarket_android.ui.downloadInvoice

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import kotlinx.android.synthetic.main.downloaded_invoice_item.view.*

class DownloadInvoiceAdapter (
    private var invoiceList: List<Data>,val invoiceClickListner: itemInvoiceClickListner
) :
    RecyclerView.Adapter<DownloadInvoiceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.downloaded_invoice_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = invoiceList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val view =  holder.itemView




        Glide.with(view.iv_download_invoice.context).load(invoiceList[position].user_name.image)
            .fitCenter().centerCrop().placeholder(
                R.drawable.login_logo).into(view.iv_download_invoice)


        view.tv_invoice_name.setText(invoiceList[position].order_id)
        view.tv_view_invoice.setOnClickListener {

           /* val i  = Intent(view.context, ProductDetailActivity::class.java)
            i.putExtra("pro_id", this!!.userAdvertismentList!![position]._id)
            i.putExtra("pro_img", this!!.userAdvertismentList!![position].mainvideoimage)
            view.context.startActivity(i)*/
            invoiceClickListner.onviewInvoiceClick(holder.adapterPosition)
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    interface itemInvoiceClickListner{
        fun onviewInvoiceClick(position: Int)
    }
}
