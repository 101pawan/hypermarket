package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CouponListResponse
import kotlinx.android.synthetic.main.coupon_list_item.view.*

class CouponListAdapter(
    private val context: Activity?,
    private var couponList: List<CouponListResponse.CouponModel>,
    val clickListener: onClickListener
) :
    RecyclerView.Adapter<CouponListAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.coupon_list_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return couponList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = couponList[position]

        holder.itemView.coupon_code.text = couponList[position].coupon_code
        holder.itemView.couponcode_title.text = couponList[position].coupon_text
        holder.itemView.couponcode_desciption.text = couponList[position].description
        holder.itemView.months_Duration.text = holder.itemView.months_Duration.context.resources.getString(R.string.duration)+ couponList[position].coupon_valid_to
        holder.itemView.expiry_coupon.text = holder.itemView.expiry_coupon.context.resources.getString(R.string.expiry) + couponList[position].coupon_valid_to
        holder.itemView.get_Code.setOnClickListener {
            clickListener.onClick(couponList[position])
        }

    }


    fun getItem(postion: Int): CouponListResponse.CouponModel {
        return couponList[postion]
    }

    interface onClickListener {
        fun onClick(
            couponModel: CouponListResponse.CouponModel
        )
    }

}