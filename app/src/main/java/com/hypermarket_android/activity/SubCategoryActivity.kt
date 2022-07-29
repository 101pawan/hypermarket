package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.SubCategoryAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CategoriesDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.GridSpacingItemDecoration

class SubCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        val listOfSubcategory =
            intent.getParcelableArrayListExtra<CategoriesDataModel.SubCategoryModel>("listOfSubcategory")


        val ivBack = findViewById<ImageView>(R.id.iv_actionbar_back)
        val rvSubcategory = findViewById<RecyclerView>(R.id.rv_sub_category)
        val svSearch = findViewById<SearchView>(R.id.sv_search)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)

        rvSubcategory.layoutManager == GridLayoutManager(baseContext, 3)
        rvSubcategory.addItemDecoration(GridSpacingItemDecoration(10))
        val categoryAdapter = SubCategoryAdapter(listOfSubcategory!!, this)
        tvToolbarTitle.text = intent.getStringExtra("name")
        categoryAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {

              //  Toast.makeText(baseContext,"Under Development..",Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(
                        baseContext,
                        ProductListActivity::class.java
                    ).putExtra("categoryId", categoryAdapter.getItem(position).id.toString()).putExtra(
                        "name",
                        categoryAdapter.getItem(position).name
                    )
                )

            }

        })
        rvSubcategory.adapter = categoryAdapter

        ivBack.setOnClickListener {
            onBackPressed()
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

       statusBackGroundColor()
    }
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
}
