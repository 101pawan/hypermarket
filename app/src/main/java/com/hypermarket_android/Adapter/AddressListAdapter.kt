package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ManageAddressResponse
import kotlinx.android.synthetic.main.item_address.view.*

class AddressListAdapter(
    private val context: Activity?,
    private var listOfAddress: List<ManageAddressResponse.UserAddres>,
    private val clickListener: onClickListener
) :
    RecyclerView.Adapter<AddressListAdapter.MyViewHolder>() {

    var rowIndex = -1

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_address, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfAddress.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listOfAddress[position]

        holder.apply {
            holder.itemView.tv_name.text = item.name
            holder.itemView.tv_address.text =
                " ${item?.house_number ?: ""}, ${item?.building_tower ?: ""}, ${item?.society_locality ?: ""}"
        }

        holder.itemView.iv_delete.setOnClickListener {
            clickListener.onClick(listOfAddress[position], "delete")
        }

        holder.itemView.iv_edit.setOnClickListener {
            clickListener.onClick(listOfAddress[position], "edit")
        }

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(listOfAddress[position], "select")

        }

        holder.itemView.rb_select_address.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
            clickListener.onClick(listOfAddress[position], "select")

        }

        if (rowIndex == position) {
            holder.itemView.rb_select_address.isChecked = true
        } else {
            holder.itemView.rb_select_address.isChecked = false

        }


    }


    fun getItem(postion: Int): ManageAddressResponse.UserAddres {
        return listOfAddress[postion]
    }

    interface onClickListener {
        fun onClick(
            userAddres: ManageAddressResponse.UserAddres,
            type: String
        )
    }

}