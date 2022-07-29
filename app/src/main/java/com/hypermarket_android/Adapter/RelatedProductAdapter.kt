package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener

class RelatedProductAdapter(
    private val context: Activity?,
    private var listOfRelatedProduct: ArrayList<ProductDetailDataModel.ProductData>
) :
    RecyclerView.Adapter<RelatedProductAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.rl_parent_view)
        var ivProductPhoto = itemView.findViewById<ImageView>(R.id.iv_product_image)
        var tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var tvStock = itemView.findViewById<TextView>(R.id.tv_stock)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
        var tvBtnAddToCart = itemView.findViewById<TextView>(R.id.tv_cart)


        init {


            tvMainPrice.paintFlags = tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cart_grey,0, 0, 0);

            tvBtnAddToCart.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_cart,
                    adapterPosition
                )
            }

            ivHeart.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_heart,
                    adapterPosition
                )
            }

            parentLayout.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.rl_parent_view,
                    adapterPosition
                )
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_related_product, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfRelatedProduct.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (getItem(position).selling_price==getItem(position).main_price){
            holder.tvMainPrice.visibility=View.GONE
        }else{
            holder.tvMainPrice.visibility=View.VISIBLE
        }
        Glide.with(context!!).load(getItem(position).main_image).thumbnail(0.1f).placeholder(R.drawable.no_image)
            .into(holder.ivProductPhoto)
        holder.tvProductName.text = getItem(position).name
        holder.tvMainPrice.text = getItem(position).main_price + " " + getItem(position).currency
        holder.tvSellingPrice.text =
            getItem(position).selling_price + " " + getItem(position).currency
        holder.tvRating.text = getItem(position).rating.toString()
        holder.ratingBar.rating = getItem(position).rating!!.toFloat()
        if (getItem(position).is_wishlist == 1) {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_fill)
        } else {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_un_fill_black)
        }
        if (getItem(position).is_added == 1) {
            holder.tvBtnAddToCart.text = holder.tvBtnAddToCart.context.resources.getString(R.string.remove_from_cart)
            holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_cart_green,
                0,
                0,
                0
            );
        } else {
            holder.tvBtnAddToCart.text = holder.tvBtnAddToCart.context.resources.getString(R.string.add_to_cart)
            holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_cart_grey,
                0,
                0,
                0
            );
        }
    }
    fun getItem(postion: Int): ProductDetailDataModel.ProductData{
        return listOfRelatedProduct[postion]
    }
    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

}