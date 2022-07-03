package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.SaverLaterDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.makeramen.roundedimageview.RoundedImageView

class SaveForLaterAdapter(
    private val context: FragmentActivity?,
    private var listOfCart: ArrayList<SaverLaterDataModel.SaverLaterData>
) :
    RecyclerView.Adapter<SaveForLaterAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

       // val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivProductPhoto = itemView.findViewById<RoundedImageView>(R.id.iv_product_photo)
        var tvItemName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvSellingPrice = itemView.findViewById<TextView>(R.id.tv_selling_price)
        var tvRating = itemView.findViewById<TextView>(R.id.tv_rating)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)
        var tvRemove = itemView.findViewById<TextView>(R.id.tv_remove)
        var tvMoveTocart = itemView.findViewById<TextView>(R.id.tv_move_to_cart)


        init {



            tvRemove.setOnClickListener {

                recyclerViewClickListener.onClick(
                    R.id.tv_remove,
                    adapterPosition
                )
            }

            tvMoveTocart.setOnClickListener {

                recyclerViewClickListener.onClick(
                    R.id.tv_move_to_cart,
                    adapterPosition
                )
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_save_for_later_list, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfCart.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context!!).load(getItem(position).main_image).thumbnail(0.1f)
            .placeholder(R.drawable.no_image)
            .into(holder.ivProductPhoto)
        holder.tvItemName.text = getItem(position).name
        holder.tvSellingPrice.text = getItem(position).price + " " + getItem(position).currency
        holder.tvRating.text = getItem(position).rating.toString()
        holder.ratingBar.rating = getItem(position).rating!!.toFloat()


    }


    fun getItem(postion: Int): SaverLaterDataModel.SaverLaterData{
        return listOfCart[postion]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }



}