package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.OngoingOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.OrderTrackActivity
import com.hypermarket_android.activity.UpdateOrderStatusActivity
import com.hypermarket_android.dataModel.DeliveryOrderListResponse
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.RecyclerViewClickListener
import kotlinx.android.synthetic.main.item_ongoing_delivery_list_item.view.*
import java.lang.Exception

class OngoingDeliveryOrderAdapter (
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<DeliveryOrderListResponse.DeliveryOrderData>,
    private val statusId: Int?,
    private val listOfOnGoingOrder: ArrayList<DeliveryOrderListResponse.DeliveryOrderData>?,
    private val listener: OnclickListener
) :
    RecyclerView.Adapter<OngoingDeliveryOrderAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener


    interface OnclickListener {
        fun onClick(orderData: DeliveryOrderListResponse.DeliveryOrderData)
        fun onClick(position: Int,orderId: String?)
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

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        val data = listOfOrder.get(position)
        Log.e("checkstatusid",data.status_id.toString())
        if (statusId == 11){
            //accept delivery
            holder.itemView.tv_update_status.setText(context?.getString(R.string.accept))
        }
        Log.e("getorderstatus",data.status.toString())
        if (data.status == "Replacement" || data.status == "Refund"){
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
        val checkData = listOfOnGoingOrder?.any{ it -> it.order_id == data.order_id }
        Log.e("checkdata",checkData.toString())
        checkData?.let {
            if (it == true){
                if (statusId == 11) {
                    holder.itemView.tv_update_status.visibility = android.view.View.GONE
                }
            }
        }

//
        holder.itemView.tv_delivery_order_id.text = "#" + data.order_id
        holder.itemView.tv_delivery_address.text = data.address

        holder.itemView.tv_update_status.setOnClickListener {
            try {
                Log.e("testongoing",data.status_id.toString())
                if (statusId == 11){
                    //accept delivery
                    listener.onClick(position,data.order_id)
                }else{
                    UpdateOrderStatusActivity.orderId = data.order_id
                    context?.startActivity(Intent(context, UpdateOrderStatusActivity::class.java))
                }


            }catch (e:Exception){

            }
        }
        if (data.payment_mode == 0){
            holder.itemView.tv_payment_mode.text = context?.resources?.getString(R.string.cash)
        }else{
            holder.itemView.tv_payment_mode.text = context?.resources?.getString(R.string.online)
        }
        holder.itemView.tv_payble_amount.text = data.total_payable_amount + " " +
                context?.resources?.getString(R.string.aed)

        val aed = data.total_payable_amount?:"0"
        holder.itemView.tv_payble_amount.text =
            aed + " " +
                    context?.resources?.getString(R.string.aed)
        val m_no = data.mobile_number?:""
        val alt_mobile_number = data.alt_mobile_number?:""
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