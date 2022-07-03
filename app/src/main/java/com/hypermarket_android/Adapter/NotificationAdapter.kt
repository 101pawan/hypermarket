package com.hypermarket_android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.NotificationResponse
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mManageAddressList: MutableList<NotificationResponse.Result> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null
        holder =
            ContentViewHolder(
                inflater.inflate(
                    R.layout.item_notification,
                    parent,
                    false
                )
            )
        return holder!!
    }



    override fun getItemCount(): Int {
        return mManageAddressList.size;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            val contentViewHolder = holder as ContentViewHolder
           contentViewHolder.notificationTV.text = mManageAddressList.get(contentViewHolder.adapterPosition).notification
           contentViewHolder.notificationTitleTV.text = mManageAddressList.get(contentViewHolder.adapterPosition).title
//           contentViewHolder.dateTimeTV.text = mManageAddressList.get(position).date
            contentViewHolder.dateTimeTV.text = getDateFromTimeInMillis(mManageAddressList.get(contentViewHolder.adapterPosition).notificationDate)
        }
    }

    private fun getDateFromTimeInMillis(notificationDate: Long): String {
        val dateFormat=  SimpleDateFormat("dd/MM/yyyy");
        System.out.println(notificationDate);
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(notificationDate*1000);
        System.out.println(dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime())
    }


    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationIV = itemView.findViewById<ImageView>(R.id.iv_notification)
        var notificationTV = itemView.findViewById<TextView>(R.id.tv_notification)
        var dateTimeTV = itemView.findViewById<TextView>(R.id.tv_time_date)
        var notificationTitleTV = itemView.findViewById<TextView>(R.id.tv_notification_title)

    }



    fun setData(result: List<NotificationResponse.Result>) {
        mManageAddressList.clear()
        mManageAddressList.addAll(result)
        notifyDataSetChanged()
    }


}


