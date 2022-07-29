package com.hypermarket_android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.WishListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.WishListDataModel
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.eventPojos.WishListUpdateEventPojo
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.GridSpacingItemDecoration
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.activity_my_wishlist.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWishlistActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferenceUtil
    private var wishListAdapter: WishListAdapter? = null
    private lateinit var productViewModel: ProductViewModel
    private var listOfProduct: ArrayList<WishListDataModel.WishListData> =
        ArrayList()

    private lateinit var rvWhishList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wishlist)
        progressBar=findViewById(R.id.progressbar)
        tvReload=findViewById(R.id.tv_reload)
        rvWhishList=findViewById(R.id.rv_wish_list)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);
        iv_actionbar_back.setOnClickListener {
            onBackPressed()
        }
        sharedPreferences= SharedPreferenceUtil.getInstance(baseContext)
        productViewModel=ViewModelProvider(this).get(ProductViewModel::class.java)
        rvWhishList.layoutManager = (GridLayoutManager(baseContext, 2))
        rvWhishList.addItemDecoration(GridSpacingItemDecoration(8))
        wishListAdapter= WishListAdapter(this,listOfProduct)
        wishListAdapter!!.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (id==R.id.rl_parent_view){
                    startActivity(
                        Intent(
                            baseContext,
                            ProductDetailActivity::class.java
                        ).putExtra(
                            "product_id",
                            wishListAdapter!!.getItem(position).product_id
                        )
                    )
                }else if (id==R.id.tv_cart){
                    if (Helper.getCurrentLocation(this@MyWishlistActivity).equals(sharedPreferences.country)) {
                        if (Helper.isNetworkAvailable(this@MyWishlistActivity)) {
                            if (wishListAdapter!!.getItem(position).is_added == 0) {
                                postMoveToCartFromWishList(
                                    position,
                                    wishListAdapter!!.getItem(position).wishlist_id!!
                                )
                            }/*else{
                            postAddToCart(wishListAdapter!!.getItem(position).product_id!!)

                        }*/
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()
                    }
                }else if (id==R.id.tv_remove){
                    if (Helper.getCurrentLocation(this@MyWishlistActivity).equals(sharedPreferences.country)) {
                        if (Helper.isNetworkAvailable(this@MyWishlistActivity)) {
                            postRemoveToWishList(position, wishListAdapter!!.getItem(position).id!!)
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()
                    }
                }else if (id==R.id.iv_heart){
                    if (Helper.getCurrentLocation(this@MyWishlistActivity).equals(sharedPreferences.country)) {
                        if (Helper.isNetworkAvailable(this@MyWishlistActivity)) {
                            wishListAdapter!!.getItem(position).is_wishlist == 0
                            postRemoveToWishList(position, wishListAdapter!!.getItem(position).id!!)
                            wishListAdapter!!.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()

                    }
                }
            }
        })
        rvWhishList.adapter=wishListAdapter
        getWishList()
        tvReload.setOnClickListener {
            if(Helper.isNetworkAvailable(this)){
                getWishList()
            }else {
                Toast.makeText(baseContext,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
            }
        }
        statusBackGroundColor()
    }
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    fun postRemoveToWishList(position:Int,wishId:Int){
        progressBar.visibility = View.VISIBLE
        productViewModel.postRemoveToWishList(sharedPreferences.accessToken,wishId,object :
            Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                progressBar.visibility=View.GONE
                if (response.isSuccessful){
                    listOfProduct.removeAt(position)
                    wishListAdapter!!.notifyDataSetChanged()
                    if(listOfProduct.size==0){
                        tv_empty_message.visibility = View.VISIBLE
                    }else{
                        tv_empty_message.visibility = View.GONE
                    }
                }
            }

        })
    }
    private fun getWishList(){
        progressBar.visibility=View.VISIBLE
        tvReload.visibility=View.GONE
        productViewModel.getWishList(sharedPreferences.accessToken,object :Callback<WishListDataModel>{
            override fun onFailure(call: Call<WishListDataModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                tvReload.visibility = View.VISIBLE
            }
            override fun onResponse(
                call: Call<WishListDataModel>,
                response: Response<WishListDataModel>
            ) {
                if (response.isSuccessful){
                    progressBar.visibility=View.GONE
                    tvReload.visibility=View.GONE
                    listOfProduct.clear()
                    listOfProduct.addAll(response.body()!!.wish_list)
                    wishListAdapter!!.notifyDataSetChanged()
                    if (listOfProduct.size==0){
                        tv_empty_message.visibility=View.VISIBLE
                    }else{
                        tv_empty_message.visibility=View.GONE
                    }
                }else{
                    progressBar.visibility=View.GONE
                    tvReload.visibility=View.VISIBLE
                }
            }

        })
    }
    fun postMoveToCartFromWishList(position:Int,wishListId: Int) {
        productViewModel.postMoveToCartFromWishList(
            sharedPreferences.accessToken,
            wishListId,
            object :
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                }
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))
                        listOfProduct.removeAt(position)
                        wishListAdapter!!.notifyDataSetChanged()
                        if (listOfProduct.size==0){
                            tv_empty_message.visibility=View.VISIBLE
                        }else{
                            tv_empty_message.visibility=View.GONE
                        }
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.added_to_cart),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WishListUpdateEventPojo) {
        if (event.isWishlistUpdate()!!) {
            getWishList()
            EventBus.getDefault().postSticky(WishListUpdateEventPojo(false))
        }
    }
    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
    public override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}
