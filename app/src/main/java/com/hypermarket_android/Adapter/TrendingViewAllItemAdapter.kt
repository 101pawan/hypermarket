package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.TrendingDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener

class TrendingViewAllItemAdapter(
    private val context: FragmentActivity?,
    private var trendingList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>
) :
    RecyclerView.Adapter<TrendingViewAllItemAdapter.MyViewHolder>(), Filterable {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener

    private var mFilteredList: List<TrendingDataModel.ProductListDataModel.ProductData>? = null
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

          val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivTrendingPhoto = itemView.findViewById<ImageView>(R.id.trending_image)
        var tvTrendingItemName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvMainPrice = itemView.findViewById<TextView>(R.id.tv_main_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)


        init {

            tvMainPrice.paintFlags=tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            parentLayout.setOnClickListener{
            recyclerViewClickListener.onClick(
                R.id.ll_parent_layout,
                adapterPosition
            )
            }

            ivHeart.setOnClickListener {
                recyclerViewClickListener.onClick(
                    R.id.iv_heart,
                    adapterPosition
                )

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
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.trending_view_all_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return trendingList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context!!).load(getItem(position).main_image).placeholder(R.drawable.no_image).thumbnail(0.1f)
            .into(holder.ivTrendingPhoto)
        holder.tvTrendingItemName.text = getItem(position).name
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


    }


    fun getItem(postion: Int): TrendingDataModel.ProductListDataModel.ProductData {
        return trendingList[postion]
    }


    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<TrendingDataModel.ProductListDataModel.ProductData>()

                if (mFilteredList == null)
                    mFilteredList = trendingList
                if (constraint != null) {
                    if (mFilteredList != null && mFilteredList!!.size > 0) {
                        for (itemDetailDataModel in mFilteredList!!) {
                            if (itemDetailDataModel.name!!.toLowerCase().contains(
                                    constraint.toString().toLowerCase()
                                )
                            )
                                results.add(itemDetailDataModel)
                        }
                    }
                    oReturn.count = results.size
                    oReturn.values = results
                } else {
                    oReturn.count = trendingList.size
                    oReturn.values = trendingList
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                trendingList =
                    results.values as ArrayList<TrendingDataModel.ProductListDataModel.ProductData>
                notifyDataSetChanged()
            }
        }
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

}