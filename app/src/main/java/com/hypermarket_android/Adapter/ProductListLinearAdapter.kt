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
import com.hypermarket_android.dataModel.TrendingDataModel
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.wang.avi.AVLoadingIndicatorView

class ProductListLinearAdapter(
    private val context: Activity,
    private var trendingList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>
) :
    RecyclerView.Adapter< RecyclerView.ViewHolder>() {
    private val TYPE_FOOTER = 1
    private val TYPE_ITEM = 2
    private var isBottomError: Boolean = false
    private var isBottomLoading: Boolean = false
    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private lateinit var recyclerViewClickListener: RecyclerViewClickListener



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<LinearLayout>(R.id.rl_parent_view)
        var ivProductPhoto = itemView.findViewById<ImageView>(R.id.iv_product_image)
        var tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var tvStock = itemView.findViewById<TextView>(R.id.tv_stock)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var tvAddToWishList = itemView.findViewById<TextView>(R.id.tv_add_to_wish_list)
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


            tvAddToWishList.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.tv_add_to_wishlist,
                    adapterPosition
                )

            }

            parentLayout.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.rl_parent_view,
                    adapterPosition
                )
            }

            /*     //parentLayout.setOnClickListener{

                 ivHeart.setOnClickListener {
                     if (trendingList.get(adapterPosition).is_wishlist == 1) {
                         trendingList.get(adapterPosition).is_wishlist = 0
                         ivHeart.setImageResource(R.drawable.ic_heart_un_fill)
                     } else {
                         trendingList.get(adapterPosition).is_wishlist = 1
                         ivHeart.setImageResource(R.drawable.ic_heart_fill)

                     }
                     notifyDataSetChanged()
                 }*/

        }
        //     }

    }


    private inner class FooterViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var avlBottomLoadingDots: AVLoadingIndicatorView
        var ivBottomReload: ImageView

        init {
            avlBottomLoadingDots = view.findViewById(R.id.avl_loading)
            ivBottomReload = view.findViewById(R.id.iv_bottom_reload)

            ivBottomReload.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_bottom_reload,
                    adapterPosition
                )
            }
        }
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = null
        if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_product_list_vertical_layout, parent, false)
            return MyViewHolder(itemView)
        } else if (viewType == TYPE_FOOTER) {
            val footerView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            return FooterViewHolder(footerView)
        }
        return MyViewHolder(view!!)
    }

//    //Inflate view for recycler
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view =
//            LayoutInflater.from(context).inflate(R.layout.item_product_list_layout, parent, false)
//        return MyViewHolder(view)
//    }

    //Return size
    override fun getItemCount(): Int {
        return trendingList.size+1
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MyViewHolder) {
//            holder.run {
//
//            }
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

            if (getItem(position).is_wishlist==1){
                holder.tvAddToWishList.text=  holder.tvAddToWishList.context.resources.getString(R.string.remove_from_wishlist)
                    holder.tvAddToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_fill, 0, 0, 0);
                }else{
                holder.tvAddToWishList.text=holder.tvAddToWishList.context.resources.getString(R.string.add_to_wishlist)
                holder.tvAddToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_un_fill_black, 0, 0, 0);
            }


            if (getItem(position).is_added==1){
                holder.tvBtnAddToCart.text=holder.tvBtnAddToCart.context.resources.getString(R.string.remove_from_cart)
                holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cart_green, 0, 0, 0);
            }else{
                holder.tvBtnAddToCart.text=holder.tvBtnAddToCart.context.resources.getString(R.string.add_to_cart)
                holder.tvBtnAddToCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cart_grey, 0, 0, 0);
            }

            if (position == trendingList.size - 1 && !isBottomError && !isBottomLoading) {
                onBottomReachedListener.onBottomReached(position)
            }
        }else if (holder is FooterViewHolder) {

            if (isBottomError) {
                holder.avlBottomLoadingDots.visibility = View.GONE
                holder.ivBottomReload.visibility = View.VISIBLE
            } else if (isBottomLoading) {
                holder.avlBottomLoadingDots.visibility = View.VISIBLE
                holder.ivBottomReload.visibility = View.GONE
            } else {
                holder.avlBottomLoadingDots.visibility = View.GONE
                holder.ivBottomReload.visibility = View.GONE
            }
        }


    }





    fun getItem(postion: Int): TrendingDataModel.ProductListDataModel.ProductData {
        return trendingList[postion]
    }


    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }
    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

    fun setList(list: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>) {
        this.trendingList = list
        notifyDataSetChanged()
    }


    fun footerVisibility(isBottomError: Boolean, isBottomLoading: Boolean) {
        this.isBottomError = isBottomError
        this.isBottomLoading = isBottomLoading
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == trendingList.size) {
            TYPE_FOOTER
        } else TYPE_ITEM

    }

}
