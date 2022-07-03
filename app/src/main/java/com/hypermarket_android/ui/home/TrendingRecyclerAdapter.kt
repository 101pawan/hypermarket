package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.Fragment.ProductDetailsFragment
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductDetailActivity
import com.hypermarket_android.dataModel.TrendingDataModel

class TrendingRecyclerAdapter(
    private val context: FragmentActivity?,
    private var trendingList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>
) :
    RecyclerView.Adapter<TrendingRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivTrendingPhoto = itemView.findViewById<ImageView>(R.id.trending_image)
        var tvTrendingItemName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvPrice = itemView.findViewById<TextView>(R.id.tv_trending_price)

        init {
            parentLayout.setOnClickListener {
                val bundle = Bundle()
                val productDetFragment = ProductDetailsFragment()
                bundle.putInt("product_id",
                    getItem(adapterPosition).id!!
                )
                bundle.putString("act","trend")
                productDetFragment.arguments = bundle
                val transaction = context!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, productDetFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
               /* context!!.startActivity(
                    Intent(
                        context,
                        ProductDetailActivity::class.java
                    ).putExtra("product_id", getItem(adapterPosition).id)

                ) */
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.trending_categories_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return trendingList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context!!).load(getItem(position).main_image).placeholder(R.drawable.no_image).thumbnail(0.1f).into(holder.ivTrendingPhoto)
        holder.tvTrendingItemName.text = getItem(position).name
        holder.tvPrice.text = getItem(position).selling_price+" "+getItem(position).currency


    }


    fun getItem(postion:Int): TrendingDataModel.ProductListDataModel.ProductData{
        return trendingList[postion]
    }
}