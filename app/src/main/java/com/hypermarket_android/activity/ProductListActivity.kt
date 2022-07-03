package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.ProductListGridAdapter
import com.app.pharmadawa.ui.notification.ProductListLinearAdapter
import com.hypermarket_android.Fragment.ProductFilterDialogFragment
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.dataModel.TrendingDataModel
import com.hypermarket_android.eventPojos.ProductShortEventPojo
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.eventPojos.SelectCategoryEventPojo
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.GridSpacingItemDecoration
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_store_info.progressbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferenceUtil
    private var productListGridAdapter: ProductListGridAdapter? = null
    private var productListLinearAdapter: ProductListLinearAdapter? = null
    private lateinit var productViewModel: ProductViewModel
    private var categoryId: String? = null
    private var listOfProduct: ArrayList<TrendingDataModel.ProductListDataModel.ProductData> =
        ArrayList()

    private var isBottomError: Boolean = false
    private lateinit var rvProductListGrid: RecyclerView
    private lateinit var rvProductListLinear: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private var sort: String = ""
    private var isLinear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        categoryId = intent.getStringExtra("categoryId")
        val name = intent.getStringExtra("name")
        sharedPreferences = SharedPreferenceUtil.getInstance(baseContext)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
//        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        rvProductListGrid = findViewById(R.id.rv_product)
        rvProductListLinear = findViewById(R.id.rv_product_linear)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        tvReload = findViewById(R.id.tv_reload)
        progressBar = findViewById(R.id.progressbar)
        tvToolbarTitle.text = name

        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        rvProductListGrid.layoutManager = (GridLayoutManager(baseContext, 2));
        rvProductListGrid.addItemDecoration(GridSpacingItemDecoration(8))

        rvProductListLinear.layoutManager = LinearLayoutManager(baseContext)
        rvProductListLinear.itemAnimator = DefaultItemAnimator()

        productViewModel.getProductList(
            sharedPreferences.accessToken,
            categoryId!!,
            sharedPreferences.storeId.toString(),
            "1",
            sort
        )



        productViewModel.isNetworkError.observe(this, object : Observer<Int> {
            override fun onChanged(@Nullable value: Int?) {
                if (value == 0) {
                    isBottomError = false
                    progressbar!!.visibility = View.GONE
                    tvReload.visibility = View.GONE
                    //GridAdapter
                    productListGridAdapter!!.footerVisibility(false, false)
                    productListGridAdapter!!.notifyDataSetChanged()
                    //LinearAdapter
                    productListLinearAdapter!!.footerVisibility(false, false)
                    productListLinearAdapter!!.notifyDataSetChanged()
                } else if (value == 1) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                } else if (value == 2) {
                    isBottomError = true
                    //GridAdapter
                    productListGridAdapter!!.footerVisibility(true, false)
                    productListGridAdapter!!.notifyDataSetChanged()
                    //LinearAdapter
                    productListLinearAdapter!!.footerVisibility(true, false)
                    productListLinearAdapter!!.notifyDataSetChanged()
                }

            }
        })


        productViewModel.getProductListFromModel().observe(this,
            Observer<ArrayList<TrendingDataModel.ProductListDataModel.ProductData>> {
                if (productListGridAdapter == null) {
                    listOfProduct.addAll(it)
                    productListGridAdapter =
                        ProductListGridAdapter(this@ProductListActivity, listOfProduct)
                    productListLinearAdapter =
                        ProductListLinearAdapter(this@ProductListActivity, listOfProduct)
                    //Liner Bottom: Next Data
                    LinearLayoutManager()
                    //Bottom: Next Data
                    productListGridAdapter!!.setOnBottomReachedListener(object :
                        OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if ((!productViewModel.isDataEnd.value!!) && (!isBottomError) == true) {
                                Log.d("bottom==", "bottom1")
                                productListGridAdapter!!.footerVisibility(false, true)
                                productViewModel.getProductList(
                                    sharedPreferences.accessToken,
                                    categoryId!!,
                                    sharedPreferences.storeId.toString(),
                                    productListGridAdapter!!.getItem(0).currentPage!! + 1,
                                    sort
                                )
                            }
                        }
                    })


                    productListGridAdapter!!.setRecyclerViewClickListener(object :
                        RecyclerViewClickListener {
                        override fun onClick(
                            id: Int,
                            position: Int
                        ) {
                            if (id == R.id.iv_bottom_reload) {

                                if (Helper.isNetworkAvailable(this@ProductListActivity)) {
                                    isBottomError = false
                                    productListGridAdapter!!.footerVisibility(false, true)
                                    productListGridAdapter!!.notifyDataSetChanged()
                                    productViewModel.getProductList(
                                        sharedPreferences.accessToken,
                                        categoryId!!,
                                        sharedPreferences.storeId.toString(),
                                        productListGridAdapter!!.getItem(0).currentPage!! + 1,
                                        sort
                                    )
                                } else {
                                    Toast.makeText(
                                        baseContext,
                                        resources.getString(R.string.message_no_internet_connection),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else if (id == R.id.tv_cart) {
                                if (Helper.getCurrentLocation(this@ProductListActivity).equals(sharedPreferences.country)) {
                                    if (productListGridAdapter!!.getItem(position).is_added == 0) {
                                        postAddToCart(
                                            position,
                                            productListGridAdapter!!.getItem(position).id!!,
                                            ""
                                        )
                                    } else {
                                        postRemoveFromCart(productListGridAdapter!!.getItem(position).cart_id!!)
                                        productListGridAdapter!!.getItem(position).is_added = 0
                                        productListGridAdapter!!.notifyDataSetChanged()
                                    }
                                }else{
                                    Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()
                                }
                            } else if (id == R.id.iv_heart) {
                                if (Helper.getCurrentLocation(this@ProductListActivity).equals(sharedPreferences.country)) {
                                    if (Helper.isNetworkAvailable(this@ProductListActivity)) {
                                    if (productListGridAdapter!!.getItem(position).is_wishlist == 0) {
                                        postAddToWishList(productListGridAdapter!!.getItem(position).id!!)
                                        productListGridAdapter!!.getItem(position).is_wishlist = 1
                                        productListGridAdapter!!.notifyDataSetChanged()
                                        Toast.makeText(
                                            baseContext,
                                            resources.getString(R.string.product_added_to_wishlist_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        postAddToWishList(productListGridAdapter!!.getItem(position).id!!)
                                        productListGridAdapter!!.getItem(position).is_wishlist = 0
                                        productListGridAdapter!!.notifyDataSetChanged()
                                        Toast.makeText(
                                            baseContext,
                                            resources.getString(R.string.product_remove_to_wishlist_successfully),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
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

                            } else if (id == R.id.rl_parent_view) {
                                startActivityForResult(
                                    Intent(
                                        baseContext,
                                        ProductDetailActivity::class.java
                                    ).putExtra(
                                        "product_id",
                                        productListGridAdapter!!.getItem(position).id
                                    ), 1001
                                )
                            }
                        }
                    })
                    rvProductListGrid.adapter = productListGridAdapter
                    rvProductListLinear.adapter = productListLinearAdapter
                } else {
                    listOfProduct = it
                    productListGridAdapter!!.setList(listOfProduct);
                    productListGridAdapter!!.footerVisibility(false, false)
                    productListLinearAdapter!!.setList(listOfProduct);
                    productListLinearAdapter!!.footerVisibility(false, false);
                }

            }
        )


        tvReload.setOnClickListener {

            if (Helper.isNetworkAvailable(this)) {
                productViewModel.getProductList(
                    sharedPreferences.accessToken,
                    categoryId!!,
                    sharedPreferences.storeId.toString(),
                    "1",
                    ""
                )
            } else {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        iv_category.setOnClickListener {

            EventBus.getDefault().postSticky(SelectCategoryEventPojo(true))
            val intent = Intent(baseContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        iv_filter.setOnClickListener {

            val bottomSheet = ProductFilterDialogFragment()
            bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
        }

        iv_view_type.setOnClickListener {

            if (isLinear) {
                isLinear = false
                iv_view_type.setImageResource(R.drawable.ic_header_grid_view)
                rvProductListGrid.visibility = View.VISIBLE
                rvProductListLinear.visibility = View.GONE
            } else {
                isLinear = true
                iv_view_type.setImageResource(R.drawable.ic_header_listview)
                rvProductListGrid.visibility = View.GONE
                rvProductListLinear.visibility = View.VISIBLE
            }

        }


        iv_actionbar_back.setOnClickListener {
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

    //Event Notification Count
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ProductShortEventPojo) {

        if (event.isShort()!!) {
            Log.d("event==", "calll")
            listOfProduct.clear()
            productListGridAdapter!!.notifyDataSetChanged()
            progressBar.visibility = View.VISIBLE
            sort = event.getSort()
            productViewModel.getProductList(
                sharedPreferences.accessToken,
                categoryId!!,
                sharedPreferences.storeId.toString(),
                "1",
                sort
            )
            // EventBus.getDefault().postSticky(SelectCartEventPojo(false))

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


    fun LinearLayoutManager() {

        //Bottom: Next Data
        productListLinearAdapter!!.setOnBottomReachedListener(object :
            OnBottomReachedListener {
            override fun onBottomReached(position: Int) {

                if ((!productViewModel.isDataEnd.value!!) && (!isBottomError) == true) {
                    Log.d("bottom==", "bottom1")
                    productListLinearAdapter!!.footerVisibility(false, true)

                    productViewModel.getProductList(
                        sharedPreferences.accessToken,
                        categoryId!!,
                        sharedPreferences.storeId.toString(),
                        productListGridAdapter!!.getItem(0).currentPage!! + 1,
                        sort
                    )
                }
            }
        })


        productListLinearAdapter!!.setRecyclerViewClickListener(object :
            RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (id == R.id.iv_bottom_reload) {

                    if (Helper.isNetworkAvailable(this@ProductListActivity)) {
                        isBottomError = false
                        productListLinearAdapter!!.footerVisibility(false, true)
                        productListLinearAdapter!!.notifyDataSetChanged()

                        productViewModel.getProductList(
                            sharedPreferences.accessToken,
                            categoryId!!,
                            sharedPreferences.storeId.toString(),
                            productListGridAdapter!!.getItem(0).currentPage!! + 1,
                            sort
                        )
                    } else {
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.message_no_internet_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (id == R.id.tv_cart) {

                    if (Helper.getCurrentLocation(this@ProductListActivity).equals(sharedPreferences.country)) {

                        if (productListLinearAdapter!!.getItem(position).is_added == 0) {
                            postAddToCart(
                                position,
                                productListLinearAdapter!!.getItem(position).id!!,
                                ""
                            )

                        } else {
                            postRemoveFromCart(productListLinearAdapter!!.getItem(position).cart_id!!)
                            productListLinearAdapter!!.getItem(position).is_added = 0
                            productListLinearAdapter!!.notifyDataSetChanged()
                        }
                    }else{
                        Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()

                    }
                } else if (id == R.id.tv_add_to_wishlist) {
                    if (Helper.getCurrentLocation(this@ProductListActivity).equals(sharedPreferences.country)) {

                        if (Helper.isNetworkAvailable(this@ProductListActivity)) {
                            if (productListLinearAdapter!!.getItem(position).is_wishlist == 0) {
                                postAddToWishList(productListLinearAdapter!!.getItem(position).id!!)
                                productListLinearAdapter!!.getItem(position).is_wishlist = 1
                                productListLinearAdapter!!.notifyDataSetChanged()
                                Toast.makeText(
                                    baseContext,
                                    resources.getString(R.string.product_added_to_wishlist_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                postAddToWishList(productListLinearAdapter!!.getItem(position).id!!)
                                productListLinearAdapter!!.getItem(position).is_wishlist = 0
                                productListLinearAdapter!!.notifyDataSetChanged()
                                Toast.makeText(
                                    baseContext,
                                    resources.getString(R.string.product_remove_to_wishlist_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

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
                } else if (id == R.id.rl_parent_view) {

                    startActivityForResult(
                        Intent(
                            baseContext,
                            ProductDetailActivity::class.java
                        ).putExtra(
                            "product_id",
                            productListLinearAdapter!!.getItem(position).id
                        ), 1001
                    )

                }
            }
        })

    }


    fun postAddToCart(position: Int, productId: Int,barcodeId: String) {
        productViewModel.postAddToCart(
            sharedPreferences.accessToken,
            productId,
            sharedPreferences.storeId,
            1,
            barcodeId,
            object :
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        val jsonObject = Gson().toJson(response.body())

                        Log.d("response==", jsonObject)
                        try {
                            val jsonObject1 = JSONObject(jsonObject)

                            val cartId = jsonObject1.getInt("cart_id")
                            productListGridAdapter!!.getItem(position).cart_id = cartId
                            productListLinearAdapter!!.getItem(position).cart_id = cartId

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        productListGridAdapter!!.getItem(position).is_added = 1
                        productListGridAdapter!!.notifyDataSetChanged()

                        productListLinearAdapter!!.getItem(position).is_added = 1
                        productListLinearAdapter!!.notifyDataSetChanged()

                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))

                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.added_to_cart),
                            Toast.LENGTH_SHORT
                        ).show()
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

                }

            })

    }


    fun postRemoveFromCart(cartId: Int) {
        productViewModel.postRemoveCart(
            sharedPreferences.accessToken,
            cartId,
            object : Callback<Any> {

                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))

                    }
                }

            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1001) {
            val data1 = data!!.getParcelableExtra<ProductDetailDataModel.ProductData>("productdata")

            for (productData in listOfProduct) {
                if (productData.id == data1!!.id) {
                    productData.is_added = data1.is_added
                    productData.is_wishlist = data1.is_wishlist
                    productData.cart_id = data1.cart_id


                    productListGridAdapter!!.notifyDataSetChanged()
                    productListLinearAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

}
