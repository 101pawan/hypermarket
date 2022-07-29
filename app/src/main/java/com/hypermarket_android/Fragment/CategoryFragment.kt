package com.hypermarket_android.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.CategoryAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.util.GridSpacingItemDecoration

import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.CategoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment() : Fragment() {


    private var listOfCategory: ArrayList<CategoriesDataModel.categoryModel> = ArrayList()

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var sharedPreferences: SharedPreferenceUtil
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_category, container, false)

        progressBar = view.findViewById(R.id.progressbar)
        tvReload = view.findViewById(R.id.tv_reload)
        val rvCategory = view.findViewById<RecyclerView>(R.id.rv_category)
        val svSearch = view.findViewById<SearchView>(R.id.sv_search)
        val tvToolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)

        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        sharedPreferences = SharedPreferenceUtil.getInstance(context!!)


        rvCategory.layoutManager == GridLayoutManager(context, 3)
        rvCategory.addItemDecoration(GridSpacingItemDecoration(10))
        categoryAdapter = CategoryAdapter(listOfCategory, activity!!)
        rvCategory.adapter = categoryAdapter

        if (Helper.isNetworkAvailable(activity!!)) {
            getCategory()
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                getCategory()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        svSearch.setOnSearchClickListener {
            tvToolbarTitle.visibility = View.GONE
            svSearch.background = resources.getDrawable(R.drawable.border_rectangle)

        }
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                categoryAdapter.filter.filter(newText)
                return false
            }
        })


        svSearch.setOnCloseListener {
            tvToolbarTitle.visibility = View.VISIBLE
            svSearch.background = resources.getDrawable(R.drawable.custom_btn_drawable)

            false
        }

        return view
    }


    fun getCategory() {
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE

        categoryViewModel.getCategory(
            sharedPreferences.accessToken,
            sharedPreferences.storeId,
            object : Callback<CategoriesDataModel> {
                override fun onFailure(call: Call<CategoriesDataModel>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<CategoriesDataModel>,
                    response: Response<CategoriesDataModel>
                ) {

                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.GONE
                        listOfCategory.clear()
                        listOfCategory.addAll(response.body()!!.categories!!)
                        categoryAdapter.notifyDataSetChanged()

                    } else {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.VISIBLE
                    }
                }

            })
    }

}
