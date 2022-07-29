package com.hypermarket_android.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.pharmadawa.ui.notification.ProductDetailPhotoListAdapter
import com.app.pharmadawa.ui.notification.RelatedProductAdapter
import com.google.android.material.tabs.TabLayout
import com.hypermarket_android.Adapter.ProductPagerAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.CartDataModel
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.eventPojos.WishListUpdateEventPojo
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import com.google.gson.Gson
import com.hypermarket_android.customView.WrapContentHeightViewPager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ProductDetailActivity : AppCompatActivity() {
    private var sellingPrice: String=""
    private lateinit var parentLayout: ConstraintLayout
    private lateinit var llButtonParent: LinearLayout
    private lateinit var vpProductPhoto: WrapContentHeightViewPager
    private lateinit var indicatorTab: TabLayout
    private lateinit var ivBtnLeft: ImageView
    private lateinit var ivBtnRight: ImageView
    private lateinit var ivFrontPhoto: ImageView
    private lateinit var ivSidePhoto: ImageView
    private lateinit var ivBackPhoto: ImageView
    private lateinit var tvBtnStock: TextView
    private lateinit var tvProductName: TextView
    private lateinit var tvBrand: TextView
    private lateinit var tvMainPrice: TextView
    private lateinit var tvSellingPrice: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvRating: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvSizeXS: TextView
    private lateinit var tvSizeS: TextView
    private lateinit var tvSizeM: TextView
    private lateinit var tvSizeL: TextView
    private lateinit var tvSizeXL: TextView
    private lateinit var tvColorRoyalBlue: TextView
    private lateinit var tvColorOrange: TextView
    private lateinit var tvColorPurple: TextView
    private lateinit var tvColorRed: TextView
    private lateinit var tvColorSkyBlue: TextView
    private lateinit var tvColorGreen: TextView
    private lateinit var tvDealTiming: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var tvDealText: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvToolbarTitle: TextView
    private lateinit var rvRelatedProduct: RecyclerView
    private lateinit var rvPhotoList: RecyclerView
    private lateinit var tvReload: TextView
    private lateinit var tvRelatedProduct: TextView
    private lateinit var progressBar: ProgressBar
    private var viewForPhoto: View? = null

    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharedPreference: SharedPreferenceUtil
    private var productPagerAdapter: ProductPagerAdapter? = null
    private var TAG_CURRENT_LOCATION_TESTING = "tag_current_location_testing"


    /*var isCheckAvailable=false
    var isConfirmCheckAvailable=false
    var isConfirmCheckAvailableError=""
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        parentLayout = findViewById(R.id.constraint_layout_prent)
        llButtonParent = findViewById(R.id.ll_btn_layout)
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title)
        vpProductPhoto = findViewById(R.id.vp_product_photo)
        indicatorTab = findViewById(R.id.tab_layout)
        ivBtnLeft = findViewById(R.id.iv_btn_left)
        ivBtnRight = findViewById(R.id.iv_btn_right)
        ivFrontPhoto = findViewById(R.id.iv_front_photo)
        ivSidePhoto = findViewById(R.id.iv_side_photo)
        ivBackPhoto = findViewById(R.id.iv_back_photo)
        tvBtnStock = findViewById(R.id.tv_btn_stock)
        tvProductName = findViewById(R.id.tv_product_name)
        tvBrand = findViewById(R.id.tv_brand)
        tvMainPrice = findViewById(R.id.tv_main_price)
        tvSellingPrice = findViewById(R.id.tv_selling_price)
        tvDiscount = findViewById(R.id.tv_discount)
        tvRating = findViewById(R.id.tv_rating)
        ratingBar = findViewById(R.id.simpleRatingBar)
        tvSizeXS = findViewById(R.id.tv_size_xs)
        tvSizeS = findViewById(R.id.tv_size_s)
        tvSizeM = findViewById(R.id.tv_size_m)
        tvSizeL = findViewById(R.id.tv_size_l)
        tvSizeXL = findViewById(R.id.tv_size_xl)
        tvColorRoyalBlue = findViewById(R.id.tv_royal_blue)
        tvColorOrange = findViewById(R.id.tv_orange)
        tvColorPurple = findViewById(R.id.tv_purple)
        tvColorRed = findViewById(R.id.tv_red)
        tvColorSkyBlue = findViewById(R.id.tv_sky_blue)
        tvColorGreen = findViewById(R.id.tv_green)
        tvDealTiming = findViewById(R.id.tv_deals_timing)
        tvRemaining = findViewById(R.id.tv_remaining_text)
        tvDealText = findViewById(R.id.tv_deal_text)
        tvDescription = findViewById(R.id.tv_description)
        rvRelatedProduct = findViewById(R.id.rv_related_product)
        rvPhotoList = findViewById(R.id.rv_photo_list)
        tvReload = findViewById(R.id.tv_reload)
        progressBar = findViewById(R.id.progressbar)
        tvRelatedProduct = findViewById(R.id.tv_related_product_text)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);
        tvMainPrice.paintFlags = tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        ivBtnLeft.setImageResource(R.drawable.ic_arrow_left)
        ivBtnRight.setImageResource(R.drawable.ic_arrow_left)
        ratingBar.setIsIndicator(true)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        sharedPreference= SharedPreferenceUtil.getInstance(this)
        Helper.getCurrentLocation(this)
        val productId = intent.getIntExtra("product_id", 0)
        val dealId = intent.getIntExtra("dealId", 0)
        getProductDetails(productId, dealId)
        getCartForCarCount()
        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(this)) {
                getProductDetails(productId, dealId)
                getCartForCarCount()
            } else {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        /*   ivFrontPhoto.setOnClickListener {
               vpProductPhoto.setCurrentItem(0);

           }

           ivSidePhoto.setOnClickListener {
               vpProductPhoto.setCurrentItem(1);

           }
           ivBackPhoto.setOnClickListener {
               vpProductPhoto.setCurrentItem(2);
           }
   */


        btn_home.setOnClickListener {
            val intent = Intent(baseContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            //  EventBus.getDefault().postSticky(SelectCartEventPojo(true, false))

        }

        btn_buy_now.setOnClickListener {
           //Toast.makeText(baseContext,"Under Development",Toast.LENGTH_SHORT).show()
           /* if(isCheckAvailable){
                if(isConfirmCheckAvailable){
                    SelectPaymentModeActivity.totalAmount = sellingPrice.toString()
                    startActivity(Intent(baseContext, SelectAddressActivity::class.java))
                    finish()
                }else{
                    showToast(isConfirmCheckAvailableError)
                }
            }else{
                showToast("Please check first available for this product")
            }*/

            SelectPaymentModeActivity.totalAmount = sellingPrice.toString()
            SelectPaymentModeActivity.product_id = productId.toString()
            startActivity(Intent(baseContext, SelectAddressActivity::class.java))
            finish()
        }

        iv_actionbar_back.setOnClickListener {
            onBackPressed()
        }

        rl_cart_layout.setOnClickListener {
            EventBus.getDefault().postSticky(SelectCartEventPojo(true, true))
            val intent = Intent(baseContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // TODO
      /*  tv_btn_check.setOnClickListener {
                isCheckAvailable=true
            if(et_enter_zip_code.text.toString().isNotEmpty()){
                if(et_enter_zip_code.text.toString().length==6 || et_enter_zip_code.text.toString().length==5){
                    progressBar.visibility = View.VISIBLE
                    productViewModel.productServiceCharge(accessToken=sharedPreference.accessToken,zipcode = et_enter_zip_code.text.toString())
                }else{
                    showToast("Please enter valid zip code.")
                }
            }else{
                showToast("Please enter zip code.")
            }
        }*/

        observer()
        statusBackGroundColor()
        indicatorTab.setupWithViewPager(vpProductPhoto)
    }

    var is_available="0"

    private fun observer() {
      /*  productViewModel.checkAvailablityResponse.observe(this, Observer {
            progressBar.visibility = View.GONE
            is_available=it.is_available!!
            when(is_available){
                "1"->{
                    showToast(it.message!!)
                    isConfirmCheckAvailable =true
                }
                "0"->{
                    isConfirmCheckAvailable =false
                    isConfirmCheckAvailableError=it.message!!
                    showToast(it.message)
                }
            }
        })*/
    }
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    fun getProductDetails(productId: Int, isDeal: Int) {
//        llButtonParent.visibility = View.GONE
        parentLayout.visibility = View.GONE
        tvReload.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        productViewModel.getProductsDetails(
            sharedPreference.accessToken,
            productId,
            isDeal,
            object : Callback<ProductDetailDataModel> {
                override fun onFailure(call: Call<ProductDetailDataModel>, t: Throwable) {
                    tvReload.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResponse(
                    call: Call<ProductDetailDataModel>,
                    response: Response<ProductDetailDataModel>
                ) {
                    if (response.isSuccessful) {
                        llButtonParent.visibility = View.VISIBLE
                        parentLayout.visibility = View.VISIBLE
                        tvReload.visibility = View.GONE
                        progressBar.visibility = View.GONE
//                        setProductDetailData(
//                            response.body()!!.related_product!!,
//                            response.body()!!.product_details, isDeal
//                        )
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }

            })
    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    @SuppressLint("SetTextI18n")
//    fun setProductDetailData(
//        relatedProduct: ArrayList<ProductDetailDataModel.ProductData>,
//        productData: ProductDetailDataModel.ProductData,
//        dealId: Int
//    ) {
////        if (productData.is_added == 1) {
////            btn_add_to_cart.visibility = View.GONE
////            btn_buy_now.visibility = View.GONE
////            btn_home.visibility=View.VISIBLE
////        }
//        if (productData.is_wishlist == 1) {
//            iv_heart.setImageResource(R.drawable.ic_heart_fill)
//        } else {
//            iv_heart.setImageResource(R.drawable.ic_heart_un_fill_black)
//        }
//        if (dealId != 0) {
//            ll_deal_parent.visibility = View.VISIBLE
//            tvDealTiming.text = productData.deal_remaining + " Days"
//            tv_deal_text.text = productData.deal_desc
//        } else {
//            ll_deal_parent.visibility = View.GONE
//        }
//
//
//        // Calculating discount
//        try{
//            val discount = productData.main_price!!.toDouble() - productData.selling_price!!.toDouble()
//            val disPercent = (discount / productData.main_price!!.toDouble()) * 100;
//
//            val disc = Math.round(disPercent);
//            tvDiscount.setText(disc.toString() + "%" + " Off")
//        }catch (e:Exception){
//
//        }
//
//
////Fetching ImagePagerAdapter
//        productPagerAdapter = ProductPagerAdapter(baseContext, productData.images!!, productData)
//        vpProductPhoto.adapter = productPagerAdapter
//        vpProductPhoto.setCurrentItem(0)
//        //vpProductPhoto.
//
//        //Fetching ProductDetailPhotoListAdapter
//        rvPhotoList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        rvPhotoList.itemAnimator = DefaultItemAnimator()
//
//        productData.images!![0].isSelectedImage = true
//
//        val productDetailPhotoListAdapter =
//            ProductDetailPhotoListAdapter(this, productData.measurementUnit!!)
//
//        productDetailPhotoListAdapter.setRecyclerViewClickListener(object :
//            RecyclerViewClickListener {
//            override fun onClick(
//                id: Int,
//                position: Int,
//            ) {
//                vpProductPhoto.setCurrentItem(position);
//
//
//            }
//        })
//        productPagerAdapter!!.setRecyclerViewClickListener(object:RecyclerViewClickListener{
//            override fun onClick(id: Int, position: Int) {
//                if (id == R.id.imgDisplay)  {
//                    startActivity(
//                        Intent(
//                            this@ProductDetailActivity,
//                            FullscreenImageActivity::class.java
//                        ).putParcelableArrayListExtra(
//                            "image",
//                            productData.images)
//                    )
//                }
//            }
//
//        })
//        rvPhotoList.adapter = productDetailPhotoListAdapter
//
//        //Related ProductAdapter
//        rvRelatedProduct.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        rvRelatedProduct.itemAnimator = DefaultItemAnimator()
//
//        relatedProductAdapter = RelatedProductAdapter(this, relatedProduct)
//
//        relatedProductAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
//            override fun onClick(
//                id: Int,
//                position: Int
//            ) {
//
//                if (id == R.id.rl_parent_view) {
//
//                    startActivity(
//                        Intent(
//                            this@ProductDetailActivity,
//                            ProductDetailActivity::class.java
//                        ).putExtra("product_id", relatedProductAdapter.getItem(position).id)
//                    )
//                } else if (id == R.id.tv_cart) {
//                    if (Helper.getCurrentLocation(this@ProductDetailActivity).equals(sharedPreference.country)) {
//                        if (relatedProductAdapter.getItem(position).is_added == 0) {
//                            postAddToCart(
//                                position,
//                                relatedProductAdapter.getItem(position).id!!,
//                                "",
//                                productData
//                            )
//                        } else {
//                            postRemoveFromCart(relatedProductAdapter.getItem(position).cart_id!!)
//                            relatedProductAdapter.getItem(position).is_added = 0
//                            relatedProductAdapter.notifyDataSetChanged()
//                        }
//                    }else{
//                        Toast.makeText(baseContext,"Please Select your native country",Toast.LENGTH_SHORT).show()
//                    }
//                } else if (id == R.id.iv_heart) {
//                    if (Helper.getCurrentLocation(this@ProductDetailActivity).equals(sharedPreference.country)) {
//
//                        if (Helper.isNetworkAvailable(this@ProductDetailActivity)) {
//                            if (relatedProductAdapter.getItem(position).is_wishlist == 0) {
//                                postAddToWishList(relatedProductAdapter.getItem(position).id!!)
//                                relatedProductAdapter.getItem(position).is_wishlist = 1
//                                relatedProductAdapter.notifyDataSetChanged()
//                                Toast.makeText(
//                                    baseContext,
//                                    resources.getString(R.string.product_added_to_wishlist_successfully),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                //Log.e("WishListData","Clicked")
//
//                            } else {
//                                postAddToWishList(relatedProductAdapter.getItem(position).id!!)
//                                relatedProductAdapter.getItem(position).is_wishlist = 0
//                                relatedProductAdapter.notifyDataSetChanged()
//                                Toast.makeText(
//                                    baseContext,
//                                    resources.getString(R.string.product_remove_to_wishlist_successfully),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                //Log.e("WishListData","Clicked1")
//
//                            }
//
//                        } else {
//                            Toast.makeText(
//                                baseContext,
//                                resources.getString(R.string.message_no_internet_connection),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }else{
//                        Toast.makeText(baseContext,"Please Select your native country",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//        if (relatedProduct.isEmpty()){
//            tvRelatedProduct.visibility = View.GONE
//        }else{
//            tvRelatedProduct.visibility  = View.VISIBLE
//        }
//        rvRelatedProduct.adapter = relatedProductAdapter
//
//        if (productData.selling_price==productData.main_price){
//            tvMainPrice.visibility=View.GONE
//            tvDiscount.visibility=View.GONE
//        }else{
//            tvMainPrice.visibility=View.VISIBLE
//            tvDiscount.visibility=View.VISIBLE
//
//
//        }
//       sellingPrice= productData.selling_price.toString()
//        tvProductName.text = productData.name
//        tvMainPrice.text = productData.main_price.toString() + " " + productData.currency
//        tvSellingPrice.text = productData.selling_price.toString() + " " + productData.currency
//        ratingBar.rating = productData.rating!!.toFloat()
//        tvRating.text = productData.rating.toString()
//        tvDescription.text = productData.description!!
//        tvToolbarTitle.text = productData.name
//        tvToolbarTitle.visibility = View.VISIBLE
//
//
//
//        btn_add_to_cart.setOnClickListener {
//
//       /*     if(isCheckAvailable){
//                if(isConfirmCheckAvailable){
//
//                    val currentLocation = Helper.getCurrentLocation(this)
//                    if (currentLocation!=null && currentLocation.isNotEmpty() && currentLocation.equals(sharedPreference.country,true)) {
//                        if (Helper.isNetworkAvailable(this)) {
//                            postAddToCart(9999, productData.id!!, productData)
//                            llButtonParent.visibility = View.GONE
//                            btn_home.visibility = View.VISIBLE
//                        } else {
//
//                            Toast.makeText(
//                                baseContext,
//                                resources.getString(R.string.message_no_internet_connection),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }else{
//                        Toast.makeText(baseContext,"Please Select your native country",Toast.LENGTH_SHORT).show()
//                    }
//                }else{
//                    showToast(isConfirmCheckAvailableError)
//                }
//
//            }else{
//                showToast("Please check first available for this product")
//            }*/
//
//            val currentLocation = Helper.getCurrentLocation(this)
//            if (currentLocation!=null && currentLocation.isNotEmpty() && currentLocation.equals(sharedPreference.country,true)) {
//                if (Helper.isNetworkAvailable(this)) {
//                    postAddToCart(9999, productData.id!!, "", productData)
////                    llButtonParent.visibility = View.GONE
////                    btn_home.visibility = View.VISIBLE
//                } else {
//                    Toast.makeText(
//                        baseContext,
//                        resources.getString(R.string.message_no_internet_connection),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }else{
//                Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//        iv_heart.setOnClickListener {
//            if (Helper.getCurrentLocation(this).equals(sharedPreference.country)) {
//                if (Helper.isNetworkAvailable(this@ProductDetailActivity)) {
//                    if (productData.is_wishlist == 0) {
//                        postAddToWishList(productData.id!!)
//                        productData.is_wishlist = 1
//                        iv_heart.setImageResource(R.drawable.ic_heart_fill)
//                        Toast.makeText(
//                            baseContext,
//                            resources.getString(R.string.product_added_to_wishlist_successfully),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    } else {
//                        postAddToWishList(productData.id!!)
//                        productData.is_wishlist = 0
//                        iv_heart.setImageResource(R.drawable.ic_heart_un_fill_black)
//
//                        Toast.makeText(
//                            baseContext,
//                            resources.getString(R.string.product_remove_to_wishlist_successfully),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }
//
//                    val intent = Intent();
//                    intent.putExtra("productdata", productData);
//                    setResult(1001, intent)
//                } else {
//                    Toast.makeText(
//                        baseContext,
//                        resources.getString(R.string.message_no_internet_connection),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }else{
//                Toast.makeText(baseContext,resources.getString(R.string.please_select_country),Toast.LENGTH_SHORT).show()
//
//            }
//        }
//
//        iv_share.setOnClickListener {
//
//            if (!Helper.isReadExternalStoragePermissionAllowed(this@ProductDetailActivity)) {
//
//
//                requestPermissions(
//                    arrayOf(
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ), 1
//                )
//            } else {
//                val view =
//                    vpProductPhoto.findViewWithTag<View>("myview" + vpProductPhoto.getCurrentItem());
//
//                shareBitMapToOtherApps(view)
//            }
//
//        }
//
//
//        ivBtnLeft.setOnClickListener {
//            if (vpProductPhoto.getCurrentItem() != 0) {
//                vpProductPhoto.setCurrentItem(vpProductPhoto.getCurrentItem() - 1)
//                if (productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem()).isSelected) {
//                    productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem())
//                        .isSelected = false
//
//                } else {
//                    for (image in productData.measurementUnit!!) {
//                        image.isSelected = false
//                    }
//                    productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem())
//                        .isSelected = true
//                }
//                productDetailPhotoListAdapter.notifyDataSetChanged()
//
//            }
//        }
//
//        ivBtnRight.setOnClickListener {
//            if (vpProductPhoto.getCurrentItem() < vpProductPhoto.getAdapter()!!.getCount())
//                vpProductPhoto.setCurrentItem(vpProductPhoto.getCurrentItem() + 1)
//            if (productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem()).isSelected) {
//
//
//                if (vpProductPhoto.getCurrentItem() != productData.images!!.size - 1) {
//                    productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem())
//                        .isSelected = false
//
//                }
//
//
//            } else {
//                for (image in productData.images!!) {
//                    image.isSelectedImage = false
//                }
//                productDetailPhotoListAdapter.getItem(vpProductPhoto.getCurrentItem())
//                    .isSelected = true
//            }
//            productDetailPhotoListAdapter.notifyDataSetChanged()
//        }
//    }


    fun postAddToCart(
        position: Int,
        productId: Int,
        barcodeId: String,
        productData: ProductDetailDataModel.ProductData
    ) {
        productViewModel.postAddToCart(
            sharedPreference.accessToken,
            productId,
            sharedPreference.storeId,
            1,
            barcodeId,
            object :
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {


                        val jsonObject = Gson().toJson(response.body())

                        try {
                            val jsonObject1 = JSONObject(jsonObject)

                            val cartId = jsonObject1.getInt("cart_id")
                            if (position != 9999) {
                                relatedProductAdapter.getItem(position).cart_id = cartId
                                relatedProductAdapter.getItem(position).is_added = 1
                                relatedProductAdapter.notifyDataSetChanged()
                            } else {

                                val intent = Intent();
                                productData.is_added = 1
                                productData.cart_id = cartId
                                intent.putExtra("productdata", productData);
                                setResult(1001, intent)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))
                        getCartForCarCount()
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
            sharedPreference.accessToken,
            productId, 0,
            object : Callback<Any> {

                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        EventBus.getDefault().postSticky(WishListUpdateEventPojo(true))
                    }
                }

            })

    }

    fun postRemoveFromCart(cartId: Int) {
        productViewModel.postRemoveCart(
            sharedPreference.accessToken,
            cartId,
            object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                }
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))
                        getCartForCarCount()
                    }
                }
            })
    }


    private fun shareBitMapToOtherApps(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/*")
        intent.putExtra(
            Intent.EXTRA_TEXT, ""
        )
        val imageUri = getImageUri(getBaseContext(), getBitmapFromView(view))
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)

        //   Log.d("UriCreated", imageUri.toString())
        try {
            startActivity(Intent.createChooser(intent, "Photo"))
        } catch (ex: android.content.ActivityNotFoundException) {
            ex.printStackTrace()
        }


    }

    //Get Image URI
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    //Generate Bitmap from view
    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val view =
                    vpProductPhoto.findViewWithTag<View>("myview" + vpProductPhoto.getCurrentItem())
                shareBitMapToOtherApps(view)
            }
        }
    }


    fun getCartForCarCount() {
        productViewModel.getCartList(sharedPreference.accessToken,
            object : Callback<CartDataModel> {
                override fun onFailure(call: Call<CartDataModel>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<CartDataModel>,
                    response: Response<CartDataModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.cart_list!!.size == 0) {
                            tv_cart_count.visibility = View.GONE
                        } else {
                            tv_cart_count.visibility = View.VISIBLE
                            tv_cart_count.text = response.body()!!.cart_list!!.size.toString()
                        }
                    }
                }

            })
    }
}
