package com.hypermarket_android.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.SubCategoryAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductListActivity
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.GridSpacingItemDecoration


class CategeoryListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categeory_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val listOfSubcategory =
           // intent.getParcelableArrayListExtra<CategoriesDataModel.SubCategoryModel>("listOfSubcategory")
        val listOfSubcategory = arguments?.getParcelableArrayList<CategoriesDataModel.SubCategoryModel>("listOfSubcategory")
        val ivBack = view.findViewById<ImageView>(R.id.iv_actionbar_back)
        val rvSubcategory = view.findViewById<RecyclerView>(R.id.rv_sub_category)
        val svSearch = view.findViewById<SearchView>(R.id.sv_search)
        val tvToolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)
        rvSubcategory.layoutManager == GridLayoutManager(activity, 3)
        rvSubcategory.addItemDecoration(GridSpacingItemDecoration(10))
        val categoryAdapter = SubCategoryAdapter(listOfSubcategory!!, activity!!)
        tvToolbarTitle.text = arguments?.getString("name")
        categoryAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                val bundle = Bundle()
                val productListFragment = ProductListFragment()
                bundle.putString("categoryId", categoryAdapter.getItem(position).id.toString())
                bundle.putString("name",categoryAdapter.getItem(position).name)
                productListFragment.arguments = bundle
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, productListFragment)
                transaction?.addToBackStack("prod")
                transaction?.commit()
                /*startActivity(
                    Intent(
                        activity,
                        ProductListActivity::class.java
                    ).putExtra("categoryId", categoryAdapter.getItem(position).id.toString()).putExtra(
                        "name",
                        categoryAdapter.getItem(position).name
                    )
                ) */

            }

        })
        rvSubcategory.adapter = categoryAdapter
        ivBack.setOnClickListener {
            //onBackPressed()
            (activity as HomeActivity).setFragmentView(HomeFragment())
            //activity!!.supportFragmentManager.popBackStack()
        }
        svSearch.setOnSearchClickListener {
            tvToolbarTitle.visibility = View.GONE
            svSearch.background = resources.getDrawable(R.drawable.border_rectangle)
        }
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // do something on text submit
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                categoryAdapter.filter.filter(newText)
                // do something when text changes
                return false
            }
        })
        svSearch.setOnCloseListener {
            tvToolbarTitle.visibility = View.VISIBLE
            svSearch.background = resources.getDrawable(R.drawable.custom_btn_drawable)

            false
        }

    }


}