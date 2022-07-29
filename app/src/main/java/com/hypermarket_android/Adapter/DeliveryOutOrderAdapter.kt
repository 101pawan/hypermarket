package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.activity.OrderTrackActivity
import com.hypermarket_android.activity.UpdateOrderStatusActivity
import com.hypermarket_android.dataModel.DeliveryOrderListResponse
import com.hypermarket_android.listener.RecyclerViewClickListener
import kotlinx.android.synthetic.main.item_ongoing_delivery_list_item.view.*

class DeliveryOutOrderAdapter (
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<DeliveryOrderListResponse.DeliveryOrderData>,
    private val listener: OnclickListener
) :
    RecyclerView.Adapter<DeliveryOutOrderAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener

    interface OnclickListener {
        fun onClick(orderData: DeliveryOrderListResponse.DeliveryOrderData)
        fun onClick(position: Int)
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_ongoing_delivery_list_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listOfOrder[position].status == "Replacement" || listOfOrder[position].status == "Refund"){
            context?.resources?.let { holder.itemView.replacementLayout.setBackgroundColor(it?.getColor(R.color.red)) }
            when (listOfOrder[position].status){
                "Replacement" -> holder.itemView.type.setText("Replacement")
                "Refund" -> holder.itemView.type.setText("Refund")
                else -> {
                    holder.itemView.type.setText("")
                }
            }
        }else{
            context?.resources?.let { holder.itemView.replacementLayout.setBackgroundColor(it.getColor(R.color.white)) }
        }
        holder.itemView.tv_delivery_order_id.text = "#" + listOfOrder[position].order_id
        holder.itemView.tv_delivery_address.text = listOfOrder[position].address

        holder.itemView.tv_update_status.setOnClickListener {
            UpdateOrderStatusActivity.orderId = listOfOrder[position].order_id
            context?.startActivity(Intent(context, UpdateOrderStatusActivity::class.java))
        }
        if (listOfOrder[position].payment_mode == 0){
            holder.itemView.tv_payment_mode.text = context?.resources?.getString(R.string.cash)
        }else{
            holder.itemView.tv_payment_mode.text = context?.resources?.getString(R.string.online)
        }
        holder.itemView.tv_payble_amount.text = listOfOrder[position].total_payable_amount + " " +
                context?.resources?.getString(R.string.aed)
        val aed = listOfOrder[position].total_payable_amount?:"0"
        holder.itemView.tv_payble_amount.text =
            aed + " " +
                    context?.resources?.getString(R.string.aed)
        val m_no = listOfOrder[position].mobile_number?:""
        val alt_mobile_number = listOfOrder[position].alt_mobile_number?:""
        holder.itemView.contact.text = "$m_no $alt_mobile_number"
//        holder.itemView.tv_update_status.visibility = View.GONE

    }


    fun getItem(position: Int): DeliveryOrderListResponse.DeliveryOrderData {
        return listOfOrder[position]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }


}