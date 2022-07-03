package com.hypermarket_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.DealsProductListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.DealsProductDataModel
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DealsListActivity : AppCompatActivity() {
    private lateinit var tvReload:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharedPreference: SharedPreferenceUtil
    private var listOfDeals: ArrayList<DealsProductDataModel.ProductData> =
        ArrayList()
    private lateinit var dealsAdapter:DealsProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deals_list)

        val dealId=intent.getIntExtra("id",0);
        val name=intent.getStringExtra("name")
        val rvDeal=findViewById<RecyclerView>(R.id.rv_deal_list)
        tvReload=findViewById(R.id.tv_reload)
        progressBar=findViewById(R.id.progressbar)

        val ivActionBarBack=findViewById<ImageView>(R.id.iv_actionbar_back)
        val tvToolbarTitle=findViewById<TextView>(R.id.tv_toolbar_title)
        tvToolbarTitle.setText(name)
        productViewModel=ViewModelProvider(this).get(ProductViewModel::class.java)
        sharedPreference= SharedPreferenceUtil.getInstance(baseContext)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);
        rvDeal.layoutManager=LinearLayoutManager(baseContext)
        rvDeal.itemAnimator=DefaultItemAnimator()
        dealsAdapter= DealsProductListAdapter(this,listOfDeals)
        rvDeal.adapter=dealsAdapter
        getProductList(dealId)
        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(this)){
                getProductList(dealId)
            }else{
                Toast.makeText(baseContext,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
            }
        }
        ivActionBarBack.setOnClickListener {
            onBackPressed()
        }
        statusBackGroundColor()
    }
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    fun getProductList(id:Int){
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE
        productViewModel.getDealsProduct(id,object : Callback<DealsProductDataModel>{
            override fun onFailure(call: Call<DealsProductDataModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                tvReload.visibility = View.VISIBLE
            }
            override fun onResponse(
                call: Call<DealsProductDataModel>,
                response: Response<DealsProductDataModel>
            )
            {
                if (response.isSuccessful){
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.GONE
                    listOfDeals.clear()
                    listOfDeals.addAll(response.body()!!.product_list)
                    dealsAdapter.notifyDataSetChanged()
                }else{
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }
            }

        })
    }
}
