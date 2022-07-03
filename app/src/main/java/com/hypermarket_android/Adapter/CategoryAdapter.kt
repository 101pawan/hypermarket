package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.Fragment.CategeoryListFragment
import com.hypermarket_android.Fragment.ProductListFragment
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductListActivity
import com.hypermarket_android.activity.SubCategoryActivity
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.makeramen.roundedimageview.RoundedImageView


class CategoryAdapter(
    private var listOfCategory: List<CategoriesDataModel.categoryModel>,
    private val context: FragmentActivity?
) :
    RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>(), Filterable {
    private var mFilteredList: List<CategoriesDataModel.categoryModel>? = null


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<CardView>(R.id.parent_layout)
        var ivPhoto = itemView.findViewById<RoundedImageView>(R.id.iv_category_photo)
        var tvCategoryName = itemView.findViewById<TextView>(R.id.tv_category_name)


        init {
            parentLayout.setOnClickListener { v ->
                if (listOfCategory[adapterPosition].subCategory!!.size==0){
                    val bundle = Bundle()
                    val productListFragment = ProductListFragment()
                    bundle.putString("categoryId", getItem(adapterPosition).id.toString())
                    bundle.putString("name",getItem(adapterPosition).name)
                    productListFragment.arguments = bundle
                    val transaction = context!!.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, productListFragment)
                    transaction?.addToBackStack("prod")
                    transaction?.commit()
                    /*activity.startActivity(Intent(
                        activity,
                        ProductListActivity::class.java
                    ).putExtra("categoryId", getItem(adapterPosition).id.toString()).putExtra(
                        "name",
                        getItem(adapterPosition).name
                    )
                    )*/
                }else{
                    val bundle = Bundle()
                    val listOfSubcategory: ArrayList<CategoriesDataModel.SubCategoryModel>? = listOfCategory.get(adapterPosition).subCategory
                    val subCategeoryFragment = CategeoryListFragment()

                    bundle.putParcelableArrayList("listOfSubcategory", listOfSubcategory)
                    bundle.putString("name",listOfCategory.get(adapterPosition).name)
                    subCategeoryFragment.arguments = bundle
                    val transaction = context?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, subCategeoryFragment)
                    transaction?.addToBackStack("cat")
                    transaction?.commit()
                    /*activity.startActivity(
                        Intent(
                            activity,
                            SubCategoryActivity::class.java
                        ).putParcelableArrayListExtra(
                            "listOfSubcategory",
                            listOfCategory.get(adapterPosition).subCategory
                        ).putExtra("name",listOfCategory.get(adapterPosition).name)
                    )*/
                }

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

        Glide.with(context!!).load(getItem(position).image).placeholder(R.drawable.no_image)
            .into(holder.ivPhoto)


    }


    override fun getItemCount(): Int {
        return listOfCategory.size
    }

    fun getItem(position: Int): CategoriesDataModel.categoryModel {
        return listOfCategory.get(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<CategoriesDataModel.categoryModel>()

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
                    results.values as ArrayList<CategoriesDataModel.categoryModel>
                notifyDataSetChanged()
            }
        }
    }

}