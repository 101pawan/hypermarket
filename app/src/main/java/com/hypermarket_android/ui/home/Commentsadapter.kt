package com.hypermarket_android.ui.home

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.HomeSearchResponse
import com.hypermarket_android.dataModel.ProductList
import com.hypermarket_android.dataModel.UserData
import kotlinx.android.synthetic.main.home_search_layout_item.view.*

import java.text.SimpleDateFormat
import java.util.*

class Commentsadapter(
    val list: HomeSearchResponse,
    val commentList: List<UserData>, private val commentdeleteClickListner: commentsdeleteClickListner
) :
    RecyclerView.Adapter<Commentsadapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.home_search_layout_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = commentList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val view =  holder.itemView


        Glide.with(view.img_product.context).load(commentList[position].main_image).
        placeholder(R.drawable.login_logo).into(view.img_product)

        view.textViewName.setText(commentList[position].name)



            view.textViewName.setOnClickListener {
                val review_txt =  commentList[position].id.toString()
                commentdeleteClickListner.oncommentsupdateClick(holder.adapterPosition,review_txt)
                notifyItemChanged(holder.adapterPosition)
            }




    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }



    interface commentsdeleteClickListner{
        fun oncommentsdeleteClick(position: Int)
        fun oncommentsupdateClick(position: Int, review_txt: String)
    }


}
