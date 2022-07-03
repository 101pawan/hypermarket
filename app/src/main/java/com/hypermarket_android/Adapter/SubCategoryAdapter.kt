package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.makeramen.roundedimageview.RoundedImageView


class SubCategoryAdapter(
    private var listOfCategory: List<CategoriesDataModel.SubCategoryModel>,
    var activity: Activity
) :
    RecyclerView.Adapter<SubCategoryAdapter.ItemViewHolder>(), Filterable {

    private var mFilteredList: List<CategoriesDataModel.SubCategoryModel>? = null
    private var recyclerViewClickListener: RecyclerViewClickListener? = null


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<CardView>(R.id.parent_layout)
        var ivPhoto = itemView.findViewById<RoundedImageView>(R.id.iv_category_photo)
        var tvCategoryName = itemView.findViewById<TextView>(R.id.tv_category_name)


        init {
            parentLayout.setOnClickListener { v ->

                recyclerViewClickListener!!.onClick(
                    R.id.parent_layout,
                    adapterPosition
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)

        return ItemViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.tvCategoryName.setText(getItem(position).name)
        //  holder.tvStoreAddress.setText(getItem(position).formatted_address)

        Glide.with(activity).load(getItem(position).image).placeholder(R.drawable.no_image)
            .into(holder.ivPhoto)


    }


    override fun getItemCount(): Int {
        return listOfCategory.size
    }

    fun getItem(position: Int): CategoriesDataModel.SubCategoryModel {
        return listOfCategory.get(position)
    }


    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<CategoriesDataModel.SubCategoryModel>()

                if (mFilteredList == null)
                    mFilteredList = listOfCategory
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
                    oReturn.count = listOfCategory.size
                    oReturn.values = listOfCategory
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listOfCategory =
                    results.values as ArrayList<CategoriesDataModel.SubCategoryModel>
                notifyDataSetChanged()
            }
        }
    }
}