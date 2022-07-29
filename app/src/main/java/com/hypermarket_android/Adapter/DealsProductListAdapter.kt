package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductDetailActivity
import com.hypermarket_android.dataModel.DealsProductDataModel

class DealsProductListAdapter(
    private val context: FragmentActivity?,
    private var listOfCart: ArrayList<DealsProductDataModel.ProductData>
) :
    RecyclerView.Adapter<DealsProductListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivDealsPhoto = itemView.findViewById<ImageView>(R.id.deals_image)
        var tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
        val tvDiscount = itemView.findViewById<AppCompatTextView>(R.id.tv_discounts)
        val tvDealsName = itemView.findViewById<TextView>(R.id.tv_deal_name)
        val tvStartingDate = itemView.findViewById<TextView>(R.id.tv_starting_date)
        val tvRemainingTime = itemView.findViewById<TextView>(R.id.tv_remaining_time)


        init {

            tvMainPrice.paintFlags = tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            parentLayout.setOnClickListener {

                context!!.startActivity(
                    Intent(context, ProductDetailActivity::class.java).putExtra(
                        "product_id",
                        getItem(adapterPosition).id
                    ).putExtra("dealId", getItem(adapterPosition).deal_id)
                )
            }

        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_deals_detail_list_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfCart.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context!!).load(getItem(position).main_image).placeholder(R.drawable.no_image).thumbnail(0.1f)
            .into(holder.ivDealsPhoto)
        holder.tvDealsName.text = getItem(position).deal_name
        holder.tvProductName.text = getItem(position).name
        holder.tvSellingPrice.text =
            getItem(position).selling_price + " " + getItem(position).currency
        holder.tvMainPrice.text = getItem(position).main_price + " " + getItem(position).currency
        holder.tvDiscount.text = getItem(position).discount + holder.tvDiscount.context.resources.getString(R.string.offer)
        holder.tvStartingDate.text = getItem(position).deal_start
        holder.tvRemainingTime.text = getItem(position).deal_remain+holder.tvRemainingTime.context.resources.getString(R.string.days)

    }


    fun getItem(position: Int): DealsProductDataModel.ProductData {
        return listOfCart[position]
    }


}