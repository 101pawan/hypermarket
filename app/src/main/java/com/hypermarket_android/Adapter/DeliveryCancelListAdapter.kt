package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.DeliverOrderStatusResponseList
import com.hypermarket_android.dataModel.DeliveryOrderStatusReasonResponseList
import kotlinx.android.synthetic.main.cancel_list_item.view.*

class DeliveryCancelListAdapter(
private val context: Activity?,
private var reasonList: ArrayList<DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData>,
private val clickListener: OnClickListener
) :
RecyclerView.Adapter<DeliveryCancelListAdapter.MyViewHolder>() {

    var rowIndex = -1

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cancel_list_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return reasonList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item =reasonList[position]


        holder.itemView.radiobutton.text = item.cancel_reason

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(reasonList[position], "cancel")

        }

        holder.itemView.radiobutton.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(reasonList[position], "cancel")

        }

        holder.itemView.radiobutton.isChecked = rowIndex == position


    }


    fun getItem(position: Int): DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData {
        return reasonList[position]
    }

    interface OnClickListener {
        fun onClick(
            cancelData: DeliveryOrderStatusReasonResponseList.DeliveryOrderStatusReasonData,
            type: String
        )
    }

}