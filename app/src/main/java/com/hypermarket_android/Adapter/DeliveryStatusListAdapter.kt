package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.hypermarket_android.R

import com.hypermarket_android.dataModel.DeliverOrderStatusResponseList

import kotlinx.android.synthetic.main.cancel_list_item.view.*

class DeliveryStatusListAdapter(
    private val context: Activity?,
    private var statusList: ArrayList<DeliverOrderStatusResponseList.DeliveryOrderStatusData>,
    private val clickListener: OnClickListener
) :
    RecyclerView.Adapter<DeliveryStatusListAdapter.MyViewHolder>() {

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
        return statusList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item =statusList[position]


        holder.itemView.radiobutton.text = item.order_status

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(statusList[position], "status")

        }

        holder.itemView.radiobutton.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(statusList[position], "status")

        }

        holder.itemView.radiobutton.isChecked = rowIndex == position


    }


    fun getItem(position: Int): DeliverOrderStatusResponseList.DeliveryOrderStatusData {
        return statusList[position]
    }

    interface OnClickListener {
        fun onClick(
            cancelData: DeliverOrderStatusResponseList.DeliveryOrderStatusData,
            type: String
        )
    }

}