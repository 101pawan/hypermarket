package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CancelListResponse
import kotlinx.android.synthetic.main.cancel_list_item.view.*

class CancelListAdapter(
    private val context: Activity?,
    private var cancelList: ArrayList<CancelListResponse.CancelModel>,
    private val clickListener: onClickListener
) :
    RecyclerView.Adapter<CancelListAdapter.MyViewHolder>() {

    var rowIndex = -1

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cancel_list_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return cancelList.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = cancelList[position]


        holder.itemView.radiobutton.text = item.cancel_reason

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(cancelList[position], "cancel")

        }

        holder.itemView.radiobutton.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(cancelList[position], "cancel")

        }

        if (rowIndex == position) {
            holder.itemView.radiobutton.isChecked = true
        } else {
            holder.itemView.radiobutton.isChecked = false

        }


    }


    fun getItem(postion: Int): CancelListResponse.CancelModel {
        return cancelList[postion]
    }

    interface onClickListener {
        fun onClick(
            cancelData: CancelListResponse.CancelModel,
            type: String
        )
    }

}