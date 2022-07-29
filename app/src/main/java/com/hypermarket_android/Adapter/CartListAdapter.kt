package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CartDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.makeramen.roundedimageview.RoundedImageView

class CartListAdapter(
    private val context: FragmentActivity?,
    private var listOfCart: ArrayList<CartDataModel.CartData>
) :
    RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {
    private lateinit var recyclerViewClickListener: RecyclerViewClickListener
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivProductPhoto = itemView.findViewById<RoundedImageView>(R.id.iv_product_photo)
        var tvItemName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var tvQuantity = itemView.findViewById<TextView>(R.id.tv_quantity)
        var ivMinus = itemView.findViewById<ImageView>(R.id.iv_minus)
        var ivPlus = itemView.findViewById<ImageView>(R.id.iv_plus)
        var tvRemove = itemView.findViewById<TextView>(R.id.tv_remove)
        var tvAddToWishlist = itemView.findViewById<TextView>(R.id.tv_add_to_wishlist)
        init {
                parentLayout.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.ll_parent_layout,
                    adapterPosition
                )
            }
            ivMinus.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_minus,
                    adapterPosition
                )
            }
            ivPlus.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_plus,
                    adapterPosition
                )
            }
            tvRemove.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_remove,
                    adapterPosition
                )
            }
            tvAddToWishlist.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_add_to_wishlist,
                    adapterPosition
                )
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_cart_list, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfCart.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context!!).load(getItem(position).image).placeholder(R.drawable.no_image).thumbnail(0.1f)
            .into(holder.ivProductPhoto)
        //Log.e("PdPhoto", getItem(position).image.toString())
        holder.tvItemName.text = getItem(position).name
        //Log.e("PdName", getItem(position).name.toString())

        holder.tvRating.text = getItem(position).rating.toString()
        holder.ratingBar.rating = getItem(position).rating!!.toFloat()
        holder.tvQuantity.text= getItem(position).quantity!!.toString()

        if (getItem(position).is_wishlist==1){
            holder.tvAddToWishlist.visibility=View.GONE
        }else{
            holder.tvAddToWishlist.visibility=View.VISIBLE

        }
      //  getItem(position).available_qty=getItem(position).available_qty!! - getItem(position).quantity!!
        Log.d("availbaleqty==", getItem(position).available_qty.toString())

      //  val totalPrice: Int = getItem(position).price!!.toInt()* getItem(position).quantity!!
        //  getItem(position).price=totalPrice.toString()
        holder.tvSellingPrice.text =getItem(position).price+ " " + getItem(position).currency
    }
    fun getItem(position: Int): CartDataModel.CartData{
        return listOfCart[position]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

}