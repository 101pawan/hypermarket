package com.hypermarket_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.TrendingViewAllItemAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductDetailActivity
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.dataModel.TrendingDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import com.hypermarket_android.viewModel.TrendingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrendingProductListFragment : Fragment() {
    private lateinit var trendingViewModel: TrendingViewModel
    private lateinit var trendingViewAllItemAdapter: TrendingViewAllItemAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharedPreferences: SharedPreferenceUtil
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private var listOfTrendingProduct: ArrayList<TrendingDataModel.ProductListDataModel.ProductData> =
        ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvTrending = view.findViewById<RecyclerView>(R.id.rv_trendings)

        val svSearch = view.findViewById<SearchView>(R.id.sv_search)
        val tvToolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)
        val ivActionbarback = view.findViewById<ImageView>(R.id.iv_actionbar_back)
        progressBar = view.findViewById(R.id.progressbar)
        tvReload = view.findViewById(R.id.tv_reload)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        sharedPreferences= SharedPreferenceUtil.getInstance(activity!!)
        trendingViewModel= ViewModelProvider(this).get(TrendingViewModel::class.java)
        productViewModel= ViewModelProvider(this).get(ProductViewModel::class.java)

        rvTrending.layoutManager = LinearLayoutManager(activity)
        rvTrending.itemAnimator = DefaultItemAnimator()

        trendingViewAllItemAdapter = TrendingViewAllItemAdapter(activity, listOfTrendingProduct)

        trendingViewAllItemAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (id==R.id.ll_parent_layout){
                    val bundle = Bundle()
                    val productDetFragment = ProductDetailsFragment()
                    bundle.putInt("product_id",
                        trendingViewAllItemAdapter.getItem(position).id!!
                    )
                    bundle.putString("act","trendList")
                    productDetFragment.arguments = bundle
                    val transaction = activity!!.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, productDetFragment)
                    transaction?.disallowAddToBackStack()
                    transaction?.commit()
                   /* startActivityForResult(
                        Intent(
                            activity,
                            ProductDetailActivity::class.java
                        ).putExtra("product_id", trendingViewAllItemAdapter.getItem(position).id),1001

                    )*/

                }else if (id==R.id.iv_heart){


                    if(Helper.isNetworkAvailable(activity!!)) {
                        if (trendingViewAllItemAdapter.getItem(position).is_wishlist == 0) {
                            postAddToWishList(trendingViewAllItemAdapter.getItem(position).id!!)
                            trendingViewAllItemAdapter.getItem(position).is_wishlist = 1
                            trendingViewAllItemAdapter.notifyDataSetChanged()
                            Toast.makeText(activity,resources.getString(R.string.product_added_to_wishlist_successfully),
                                Toast.LENGTH_SHORT).show()
                        }else{
                            postAddToWishList(trendingViewAllItemAdapter.getItem(position).id!!)
                            trendingViewAllItemAdapter.getItem(position).is_wishlist = 0
                            trendingViewAllItemAdapter.notifyDataSetChanged()
                            Toast.makeText(activity,resources.getString(R.string.product_remove_to_wishlist_successfully),
                                Toast.LENGTH_SHORT).show()

                        }

                    }else{
                        Toast.makeText(activity,resources.getString(R.string.message_no_internet_connection),
                            Toast.LENGTH_SHORT).show()
                    }
                }

            }
        })
        rvTrending.adapter = trendingViewAllItemAdapter



        ivActionbarback.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = activity!!.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, homeFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
            //onBackPressed()
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
                trendingViewAllItemAdapter.filter.filter(newText)
                // do something when text changes
                return false
            }
        })


        svSearch.setOnCloseListener {
            tvToolbarTitle.visibility = View.VISIBLE
            svSearch.background = resources.getDrawable(R.drawable.custom_btn_drawable)

            false
        }


        if (Helper.isNetworkAvailable(activity!!)) {
            getTrendingProduct("1")
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                getTrendingProduct("1")
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun getTrendingProduct(page: String) {
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE

        trendingViewModel.getTrendingProducts(
            sharedPreferences.accessToken,
            sharedPreferences.storeId.toString(),
            page,
            object : Callback<TrendingDataModel> {
                override fun onFailure(call: Call<TrendingDataModel>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<TrendingDataModel>,
                    response: Response<TrendingDataModel>
                ) {

                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.GONE
                        listOfTrendingProduct.clear()
                        listOfTrendingProduct.addAll(response.body()!!.product_list.data)

                        Log.d("size==", listOfTrendingProduct.size.toString())
                        trendingViewAllItemAdapter.notifyDataSetChanged()

                    } else {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.VISIBLE
                    }
                }

            })
    }

    fun postAddToWishList(productId: Int) {
        productViewModel.postAddToWishList(
            sharedPreferences.accessToken,
            productId,0,
            object : Callback<Any> {

                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.product_added_to_wishlist_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==1001){
            val data1=data!!.getParcelableExtra<ProductDetailDataModel.ProductData>("productdata")

            for (productData in listOfTrendingProduct){
                if (productData.id== data1!!.id){
                    productData.is_added=data1.is_added
                    productData.is_wishlist=data1.is_wishlist

                    trendingViewAllItemAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}