package com.hypermarket_android.nestedrecycler.adapter

import android.R
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.hypermarket_android.Adapter.HomeCategotyAdapter
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.dataModel.TrendingDataModel
import com.hypermarket_android.nestedrecycler.model.ParentItem
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import java.util.ArrayList


class ParentItemAdapter(private val context: FragmentActivity?, private val itemParentList: List<ParentItem>?,var productViewModel: ProductViewModel,var sharedPreferences: SharedPreferenceUtil) :
    RecyclerView.Adapter<ParentItemAdapter.MyViewHolder>() {
    private val viewPool = RecycledViewPool()
    private var itemList: ArrayList<CategoriesDataModel.SubCategoryModel>? = null
    private var ChildItemList: ArrayList<TrendingDataModel.ProductListDataModel.ProductData>? = null

    fun setdata(){
        itemList =  itemParentList?.let {
            it.get(0).ParentItemTitle
        }
        ChildItemList = itemParentList?.let { it.get(0)?.ChildItemList}


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ChildItemTitle = itemView.findViewById<TextView>(com.hypermarket_android.R.id.parent_item_title)
        var childRecycler = itemView.findViewById<RecyclerView>(com.hypermarket_android.R.id.child_recyclerview)

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):  MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(com.hypermarket_android.R.layout.parent_item, p0, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Create an instance of the ParentItem
        // class for the given position
        val parentItem = itemList!![position]

        // For the created instance,
        // get the title and set it
        // as the text for the TextView
        holder.ChildItemTitle.text = parentItem.name

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.

        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        val layoutManager = LinearLayoutManager(
            holder.childRecycler
                .context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager.initialPrefetchItemCount = ChildItemList
            ?.size ?: 0

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
//        productViewModel.getProductList(
//            sharedPreferences.accessToken,
//            parentItem.id,
//            sharedPreferences.storeId.toString(),
//            1.toString(),
//            sort
//        )
        val childItemAdapter = ChildItemList?.let {
                context?.let { it1 ->
                    HomeCategotyAdapter(
                        it1,
                        it
                    )
                }
            }
        holder.childRecycler.layoutManager = layoutManager
        holder.childRecycler.adapter = childItemAdapter
        holder.childRecycler
            .setRecycledViewPool(viewPool)
    }

    // This method returns the number
    // of items we have added in the
    // ParentItemList i.e. the number
    // of instances we have created
    // of the ParentItemList
    override fun getItemCount(): Int {
        if (itemList != null){
            return itemList!!.size
        }else{
            return 0
        }

    }

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView

}