package com.app.pharmadawa.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.Fragment.CategeoryListFragment
import com.hypermarket_android.Fragment.HomeFragment
import com.hypermarket_android.Fragment.ProductListFragment
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductListActivity
import com.hypermarket_android.activity.SubCategoryActivity
import com.hypermarket_android.dataModel.CategoriesDataModel


class CategoriesRecyclerAdapter(
    private val context: FragmentActivity?,
    private var categoryList: ArrayList<CategoriesDataModel.categoryModel>
) :
    RecyclerView.Adapter<CategoriesRecyclerAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<LinearLayout>(R.id.ll_parent_layout)
        var ivPhoto = itemView.findViewById<ImageView>(R.id.img_Category)
        var tvCategoryName = itemView.findViewById<TextView>(R.id.tvCategoryName)


        init {
            parentLayout.setOnClickListener { v ->
                if (categoryList[adapterPosition].subCategory!!.size == 0) {
                    val bundle = Bundle()
                    val productListFragment = ProductListFragment()
                    bundle.putString("categoryId", categoryList.get(adapterPosition).id.toString())
                    bundle.putString("name",categoryList.get(adapterPosition).name)
                    productListFragment.arguments = bundle
                    val transaction = context!!.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, productListFragment)
                    transaction?.addToBackStack("prod")
                    transaction?.commit()
                    /*context!!.startActivity(
                        Intent(
                            context,
                            ProductListActivity::class.java
                        ).putExtra("categoryId", categoryList.get(adapterPosition).id.toString()).putExtra(
                            "name",
                            categoryList.get(adapterPosition).name
                        )
                    )*/
                    //Toast.makeText(context,categoryList.get(adapterPosition).id.toString(),Toast.LENGTH_SHORT).show()
                } else {
                    val bundle = Bundle()
                    val listOfSubcategory: ArrayList<CategoriesDataModel.SubCategoryModel>? = categoryList.get(adapterPosition).subCategory
                    val subCategeoryFragment = CategeoryListFragment()
                    bundle.putParcelableArrayList("listOfSubcategory", listOfSubcategory)
                    bundle.putString("name",categoryList.get(adapterPosition).name)
                    subCategeoryFragment.arguments = bundle
                    val transaction = context?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, subCategeoryFragment)
                    transaction?.addToBackStack("cat")
                    transaction?.commit()
                    /*context!!.startActivity(
                        Intent(
                            context,
                            SubCategoryActivity::class.java
                        ).putParcelableArrayListExtra(
                            "listOfSubcategory",
                            categoryList.get(adapterPosition).subCategory
                        ).putExtra("name",categoryList.get(adapterPosition).name)
                    )*/
                }
            }
        }

    }


    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.categories_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return categoryList.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvCategoryName.text = categoryList.get(position).name
        Glide.with(context!!).load(categoryList.get(position).image).placeholder(R.drawable.no_image).into(holder.ivPhoto)
    }
}