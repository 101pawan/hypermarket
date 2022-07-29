package com.hypermarket_android.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.TrendingDataModel

class ProductListAdapters(
    private val context: Activity,
    private var trendingList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>
) : RecyclerView.Adapter<ProductListAdapters.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_product_list_layout, parent, false)
        return MyViewHolder(view)    }

    override fun getItemCount(): Int {

       return trendingList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//          val parentLayout = itemView.findViewById<RelativeLayout>(R.id.rl_parent_view)
//        var ivProductPhoto = itemView.findViewById<ImageView>(R.id.iv_product_image)
//        var tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
//        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
//        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
//        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
//        var tvStock = itemView.findViewById<TextView>(R.id.tv_stock)
//        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
//        var ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
//        var tvBtnAddToCart = itemView.findViewById<TextView>(R.id.tv_cart)


        init {

            //  tvMainPrice.paintFlags=tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            //parentLayout.setOnClickListener{
            Toast.makeText(context, context.resources.getString(R.string.under_development)+".... ", Toast.LENGTH_SHORT).show()
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
}