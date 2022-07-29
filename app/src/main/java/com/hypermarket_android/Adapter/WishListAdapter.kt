package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.WishListDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener

class WishListAdapter(
    private val context: Activity,
    private var trendingList: ArrayList<WishListDataModel.WishListData>
) : RecyclerView.Adapter<WishListAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_wish_list_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {

        return trendingList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (getItem(position).main_price==getItem(position).selling_price){
            holder.tvMainPrice.visibility=View.GONE
        }else{
            holder.tvMainPrice.visibility=View.VISIBLE

        }
        Glide.with(context).load(getItem(position).main_image).placeholder(R.drawable.no_image).thumbnail(0.1f)
            .into(holder.ivProductPhoto)
        holder.tvProductName.text = getItem(position).name
        holder.tvMainPrice.text = getItem(position).main_price + " " + getItem(position).currency
        holder.tvSellingPrice.text =
            getItem(position).selling_price + " " + getItem(position).currency
        holder.tvRating.text = getItem(position).rating.toString()
        holder.ratingBar.rating = getItem(position).rating!!.toFloat()
        holder.ivHeart.setImageResource(R.drawable.ic_heart_fill)


        if (getItem(position).is_wishlist==1){
            holder.ivHeart.setImageResource(R.drawable.ic_heart_fill)

        }else{
            holder.ivHeart.setImageResource(R.drawable.ic_heart_un_fill_black)

        }
        if (getItem(position).is_added == 1) {
            holder.tvBtnAddToCart.visibility = View.GONE
            holder.tvBtnAddToCart.text = holder.tvBtnAddToCart.context.resources.getString(R.string.remove_from_cart)
            /* holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                 R.drawable.ic_cart_green,
                 0,
                 0,
                 0
             );*/
        } else {
            holder.tvBtnAddToCart.visibility = View.VISIBLE
            holder.tvBtnAddToCart.text = holder.tvBtnAddToCart.context.resources.getString(R.string.move_to_cart)
            /*     holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                     R.drawable.ic_cart_grey,
                     0,
                     0,
                     0
                 );*/
        }


    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.rl_parent_view)
        var ivProductPhoto = itemView.findViewById<ImageView>(R.id.iv_product_image)
        var tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
        var tvBtnAddToCart = itemView.findViewById<TextView>(R.id.tv_cart)
        var tvBtnRemove = itemView.findViewById<TextView>(R.id.tv_remove)


        init {

            tvMainPrice.paintFlags = tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


            tvBtnAddToCart.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_cart,
                    adapterPosition
                )
            }


            parentLayout.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.rl_parent_view,
                    adapterPosition
                )
            }

            tvBtnRemove.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_remove,
                    adapterPosition
                )

            }

            ivHeart.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_heart,
                    adapterPosition
                )

            }


            //parentLayout.setOnClickListener{
        }

        /*         ivHeart.setOnClickListener {
                     if (trendingList.get(adapterPosition).is_wishlist==1) {
                         trendingList.get(adapterPosition).is_wishlist=0
                         ivHeart.setImageResource(R.drawable.ic_heart_un_fill)
                     } else {
                         trendingList.get(adapterPosition).is_wishlist=1
                         ivHeart.setImageResource(R.drawable.ic_heart_fill)

                     }
                     notifyDataSetChanged()
                 }*/
        //     }

    }


    fun getItem(postion: Int): WishListDataModel.WishListData {
        return trendingList[postion]
    }


    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

}