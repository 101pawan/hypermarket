package com.hypermarket_android.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.ProductDetailPhotoListAdapter
import com.app.pharmadawa.ui.notification.RelatedProductAdapter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.hypermarket_android.Adapter.ProductColorListAdapter
import com.hypermarket_android.Adapter.ProductPagerAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.FullscreenImageActivity
import com.hypermarket_android.activity.SelectAddressActivity
import com.hypermarket_android.activity.SelectPaymentModeActivity
import com.hypermarket_android.customView.WrapContentHeightViewPager
import com.hypermarket_android.dataModel.CartDataModel
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.eventPojos.WishListUpdateEventPojo
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.BottomMenuHelper
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class ProductDetailsFragment : Fragment() {
    private var sellingPrice: String = ""
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
    private lateinit var rvColorList: RecyclerView
    private lateinit var rvSizeList: RecyclerView
    private lateinit var tvReload: TextView
    private lateinit var tvRelatedProduct: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sizeLinerLayout: LinearLayout
    private lateinit var colorLinerLayout: LinearLayout
    private lateinit var tvColorName: TextView
    private lateinit var tvSizeName: TextView
    private var viewForPhoto: View? = null
    private var categoryId: String? = null
    private var catName: String? = null
    private var isColorSelected: Boolean = false
    private var colorPosition: Int = 0
    private var sizePosition: Int = 0
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharedPreference: SharedPreferenceUtil
    private var productPagerAdapter: ProductPagerAdapter? = null
    private var productColorListAdapter: ProductColorListAdapter? = null
    private var TAG_CURRENT_LOCATION_TESTING = "tag_current_location_testing"
    var currentBarCodeId = ""
    var productColor = ""
    var homeActivity: HomeActivity? = null
    var productDetailData: ProductDetailDataModel.ProductData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivity = activity as HomeActivity?
        parentLayout = view.findViewById(R.id.constraint_layout_prent)
        llButtonParent = view.findViewById(R.id.ll_btn_layout)
        tvToolbarTitle = view.findViewById(R.id.tv_toolbar_title)
        vpProductPhoto = view.findViewById(R.id.vp_product_photo)
        indicatorTab = view.findViewById(R.id.tab_layout)
        ivBtnLeft = view.findViewById(R.id.iv_btn_left)
        ivBtnRight = view.findViewById(R.id.iv_btn_right)
        sizeLinerLayout = view.findViewById(R.id.ll_size_text)
        colorLinerLayout = view.findViewById(R.id.ll_color_text)
        tvSizeName = view.findViewById(R.id.tv_size_name)
        tvColorName = view.findViewById(R.id.tv_color_name)
        ivFrontPhoto = view.findViewById(R.id.iv_front_photo)
        ivSidePhoto = view.findViewById(R.id.iv_side_photo)
        ivBackPhoto = view.findViewById(R.id.iv_back_photo)
        tvBtnStock = view.findViewById(R.id.tv_btn_stock)
        tvProductName = view.findViewById(R.id.tv_product_name)
        tvBrand = view.findViewById(R.id.tv_brand)
        tvMainPrice = view.findViewById(R.id.tv_main_price)
        tvSellingPrice = view.findViewById(R.id.tv_selling_price)
        tvDiscount = view.findViewById(R.id.tv_discount)
        tvRating = view.findViewById(R.id.tv_rating)
        ratingBar = view.findViewById(R.id.simpleRatingBar)
        tvSizeXS = view.findViewById(R.id.tv_size_xs)
        tvSizeS = view.findViewById(R.id.tv_size_s)
        tvSizeM = view.findViewById(R.id.tv_size_m)
        tvSizeL = view.findViewById(R.id.tv_size_l)
        tvSizeXL = view.findViewById(R.id.tv_size_xl)
        tvColorRoyalBlue = view.findViewById(R.id.tv_royal_blue)
        tvColorOrange = view.findViewById(R.id.tv_orange)
        tvColorPurple = view.findViewById(R.id.tv_purple)
        tvColorRed = view.findViewById(R.id.tv_red)
        tvColorSkyBlue = view.findViewById(R.id.tv_sky_blue)
        tvColorGreen = view.findViewById(R.id.tv_green)
        tvDealTiming = view.findViewById(R.id.tv_deals_timing)
        tvRemaining = view.findViewById(R.id.tv_remaining_text)
        tvDealText = view.findViewById(R.id.tv_deal_text)
        tvDescription = view.findViewById(R.id.tv_description)
        rvRelatedProduct = view.findViewById(R.id.rv_related_product)
        rvColorList = view.findViewById(R.id.rv_color_list)
        rvSizeList = view.findViewById(R.id.rv_size_list)
        tvReload = view.findViewById(R.id.tv_reload)
        progressBar = view.findViewById(R.id.progressbar)
        tvRelatedProduct = view.findViewById(R.id.tv_related_product_text)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);
        tvMainPrice.paintFlags = tvMainPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        ivBtnLeft.setImageResource(R.drawable.ic_arrow_left)
        ivBtnRight.setImageResource(R.drawable.ic_arrow_left)
        ratingBar.setIsIndicator(true)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        sharedPreference = SharedPreferenceUtil.getInstance(activity!!)

        Helper.getCurrentLocation(activity!!)
        val act = arguments?.getString("act")
        if (act.equals("trend") || act.equals("prdList")) {
            categoryId = arguments?.getString("catId")
            catName = arguments?.getString("catName")
        } else {
            categoryId = ""
            catName = ""
        }
        val productId = arguments?.getInt("product_id", 0)
        val dealId = arguments?.getInt("dealId", 0)


        getProductDetails(productId!!, dealId!!)
//        getViewBarcodeProduct(productId!!, dealId!!)
//        getRelatedProduct(productId!!, dealId!!)

        getCartForCarCount()

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                getProductDetails(productId!!, dealId!!)
                getViewBarcodeProduct(productId!!, dealId!!)
                getRelatedProduct(productId!!, dealId!!)
                getCartForCarCount()

            } else {
                Toast.makeText(
                    activity,
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
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity!!.finish()
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
            Log.e("getcurrentBarCodeId", currentBarCodeId);
            if (currentBarCodeId.isNullOrEmpty()) {
                showDialog()
            } else {
                SelectPaymentModeActivity.totalAmount = tvSellingPrice.text.toString().replace(
                    productDetailData?.currency.toString(), ""
                ).trim()
                SelectPaymentModeActivity.product_id = productId.toString()
                SelectPaymentModeActivity.quantity = "1"
                SelectPaymentModeActivity.barCodeId = currentBarCodeId
                startActivity(Intent(activity, SelectAddressActivity::class.java))
            }
            //finish()
        }

        iv_actionbar_back.setOnClickListener {
            if (act.equals("trend")) {
                Log.e("Screen Name", "Trend")
                val homeFragment = HomeFragment()
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, homeFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            } else if (act.equals("trendList")) {
                Log.e("Screen Name", "TrendList")
                val trendFragment = TrendingProductListFragment()
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, trendFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            } else if (act.equals("home")) {
                Log.e("Screen Name", "home")
                val trendFragment = HomeFragment()
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, trendFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            } else if (act.equals("Cart")) {
                val cartFragment = CartFragment()
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, cartFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            } else {
                Log.e("Screen Name", "Product List")
//                val bundle = Bundle()
//                val productListFragment = ProductListFragment()
//                bundle.putString("categoryId", categoryId)
//                bundle.putString("name", catName)
//                Log.e("Screen Name1", categoryId!!)
//                Log.e("Screen Name2", catName!!)
//                productListFragment.arguments = bundle
//                val transaction = activity!!.supportFragmentManager?.beginTransaction()
//                transaction?.replace(R.id.container, productListFragment)
//                transaction?.disallowAddToBackStack()
//                transaction?.commit()
                activity!!.supportFragmentManager.popBackStack()
            }

        }
        rl_cart_layout.setOnClickListener {
            EventBus.getDefault().postSticky(SelectCartEventPojo(true, true))
            val cartFragment = CartFragment()
            val transaction = activity!!.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, cartFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
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
        // statusBackGroundColor()
        indicatorTab.setupWithViewPager(vpProductPhoto)
    }

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

    fun getProductDetails(productId: Int, isDeal: Int) {
//        llButtonParent.visibility = View.GONE
        parentLayout.visibility = View.GONE
        tvReload.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        productViewModel.getViewProductsDetails(
            sharedPreference.accessToken,
            productId,
            isDeal,
            object : Callback<ProductDetailDataModel> {
                override fun onFailure(call: Call<ProductDetailDataModel>, t: Throwable) {
                    tvReload.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.e("ProductBarCode8", "failure")
                    t.printStackTrace()
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResponse(
                    call: Call<ProductDetailDataModel>,
                    response: Response<ProductDetailDataModel>
                ) {
                    if (response.isSuccessful) {
                        getViewBarcodeProduct(productId!!, isDeal)
                        getRelatedProduct(productId!!, isDeal)
                        productDetailData = response.body()!!.product_details
                        llButtonParent.visibility = View.VISIBLE
                        parentLayout.visibility = View.VISIBLE
                        tvReload.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        setProductDetailData(
                            response.body()!!.product_details, isDeal
                        )
                        Log.e("ProductBarCode1", response.body()!!.product_details.id.toString())
                        Log.e("ProductBarCode2", response.body()!!.product_details.toString())
                        Log.e(
                            "ProductBarCode",
                            response.body()!!.product_details.measurement_unit?.size.toString()
                        )
                    } else {
                        Log.e("ProductBarCode6", response.body()!!.toString())
                        progressBar.visibility = View.GONE
                    }
                }

            })
    }

    fun getViewBarcodeProduct(productId: Int, isDeal: Int) {
//        llButtonParent.visibility = View.GONE
//        parentLayout.visibility = View.GONE
//        tvReload.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
        productViewModel.getViewBarcodeProduct(
            sharedPreference.accessToken,
            productId,
            isDeal,
            object : Callback<ProductDetailDataModel.BarcodeProductDataModel> {
                override fun onFailure(
                    call: Call<ProductDetailDataModel.BarcodeProductDataModel>,
                    t: Throwable
                ) {
                    tvReload.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.e("ProductBarCode8", "failure")
                    t.printStackTrace()
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResponse(
                    call: Call<ProductDetailDataModel.BarcodeProductDataModel>,
                    response: Response<ProductDetailDataModel.BarcodeProductDataModel>
                ) {
                    if (response.isSuccessful) {
//                        llButtonParent.visibility = View.VISIBLE
//                        parentLayout.visibility = View.VISIBLE
//                        tvReload.visibility = View.GONE
//                        progressBar.visibility = View.GONE
                        setBarcodeProducts(
                            response.body()!!.barcode_products
                        )
                    } else {
                        Log.e("ProductBarCode6", response.body()!!.toString())
                        progressBar.visibility = View.GONE
                    }
                }

            })
    }


    fun getRelatedProduct(productId: Int, isDeal: Int) {
//        llButtonParent.visibility = View.GONE
//        parentLayout.visibility = View.GONE
//        tvReload.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
        productViewModel.getRelatedProduct(
            sharedPreference.accessToken,
            productId,
            isDeal,
            object : Callback<ProductDetailDataModel> {
                override fun onFailure(call: Call<ProductDetailDataModel>, t: Throwable) {
                    tvReload.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.e("ProductBarCode8", "failure")
                    t.printStackTrace()
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResponse(
                    call: Call<ProductDetailDataModel>,
                    response: Response<ProductDetailDataModel>
                ) {
                    if (response.isSuccessful) {
//                        llButtonParent.visibility = View.VISIBLE
//                        parentLayout.visibility = View.VISIBLE
//                        tvReload.visibility = View.GONE
//                        progressBar.visibility = View.GONE
                        setRelatedProducts(
                            response.body()!!.related_product
                        )
                    } else {
                        Log.e("ProductBarCode6", response.body()!!.toString())
                        progressBar.visibility = View.GONE
                    }
                }

            })
    }

    private fun setRelatedProducts(relatedProduct: ArrayList<ProductDetailDataModel.ProductData>?) {
//Related ProductAdapter
        rvRelatedProduct.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rvRelatedProduct.itemAnimator = DefaultItemAnimator()
        relatedProductAdapter = relatedProduct?.let { RelatedProductAdapter(activity, it) }!!
        relatedProductAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (id == R.id.rl_parent_view) {
                    getProductDetails(relatedProductAdapter.getItem(position).id!!, 0)
                    getViewBarcodeProduct(relatedProductAdapter.getItem(position).id!!, 0)
                    getRelatedProduct(relatedProductAdapter.getItem(position).id!!, 0)
                    getCartForCarCount()
                    /*startActivity(
                        Intent(
                            activity,
                            ProductDetailActivity::class.java
                        ).putExtra("product_id", relatedProductAdapter.getItem(position).id)
                    )*/
                } else if (id == R.id.tv_cart) {
                    if (Helper.getCurrentLocation(activity!!).equals(sharedPreference.country)) {
                        if (relatedProductAdapter.getItem(position).is_added == 0) {
                            if (currentBarCodeId.isNullOrEmpty()) {
                                showDialog()
                            } else {
                                productDetailData?.let {
                                    postAddToCart(
                                        position,
                                        relatedProductAdapter.getItem(position).id!!,
                                        currentBarCodeId,
                                        it
                                    )
                                }
                            }

                        } else {
                            postRemoveFromCart(relatedProductAdapter.getItem(position).cart_id!!)
                            relatedProductAdapter.getItem(position).is_added = 0
                            relatedProductAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "Please Select your native country",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (id == R.id.iv_heart) {
                    if (Helper.getCurrentLocation(activity!!).equals(sharedPreference.country)) {
                        if (Helper.isNetworkAvailable(activity!!)) {
                            if (relatedProductAdapter.getItem(position).is_wishlist == 0) {
                                postAddToWishList(relatedProductAdapter.getItem(position).id!!)
                                relatedProductAdapter.getItem(position).is_wishlist = 1
                                relatedProductAdapter.notifyDataSetChanged()
                                Toast.makeText(
                                    activity,
                                    resources.getString(R.string.product_added_to_wishlist_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //Log.e("WishListData","Clicked")

                            } else {
                                postAddToWishList(relatedProductAdapter.getItem(position).id!!)
                                relatedProductAdapter.getItem(position).is_wishlist = 0
                                relatedProductAdapter.notifyDataSetChanged()
                                Toast.makeText(
                                    activity,
                                    resources.getString(R.string.product_remove_to_wishlist_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //Log.e("WishListData","Clicked1")

                            }

                        } else {
                            Toast.makeText(
                                activity,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "Please Select your native country",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        })
        if (relatedProduct.isEmpty()) {
            tvRelatedProduct.visibility = View.GONE
        } else {
            tvRelatedProduct.visibility = View.VISIBLE
        }
        rvRelatedProduct.adapter = relatedProductAdapter
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    @SuppressLint("SetTextI18n")
//    fun setProductDetailData(
//        relatedProduct: ArrayList<ProductDetailDataModel.ProductData>,
//        productData: ProductDetailDataModel.ProductData,
//        dealId: Int
//    ) {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    fun setProductDetailData(
        productData: ProductDetailDataModel.ProductData,
        dealId: Int
    ) {
        productDetailData = productData
        try {
            if (productData.is_added == 1) {
//                btn_add_to_cart.visibility = View.GONE
//                btn_buy_now.visibility = View.GONE
//                btn_home.visibility=View.VISIBLE
            } else {
                btn_add_to_cart.visibility = View.VISIBLE
                btn_buy_now.visibility = View.VISIBLE
                btn_home.visibility = View.GONE
            }
            if (productData.is_wishlist == 1) {
                iv_heart.setImageResource(R.drawable.ic_heart_fill)
            } else {
                iv_heart.setImageResource(R.drawable.ic_heart_un_fill_black)
            }
            if (dealId != 0) {
                ll_deal_parent.visibility = View.VISIBLE
                tvDealTiming.text = productData.deal_remaining + " Days"
                tv_deal_text.text = productData.deal_desc
            } else {
                ll_deal_parent.visibility = View.GONE
            }
        } catch (e: Exception) {

        }

        // Calculating discount
        try {
            val discount =
                productData.main_price!!.toDouble() - productData.selling_price!!.toDouble()
            val disPercent = (discount / productData.main_price!!.toDouble()) * 100;
            val disc = Math.round(disPercent);
            tvDiscount.text = disc.toString() + "%" + " Off"

        } catch (e: Exception) {

        }
//Fetching ImagePagerAdapter
        productPagerAdapter = ProductPagerAdapter(activity!!, productData.images!!, productData)
        vpProductPhoto.adapter = productPagerAdapter
        vpProductPhoto.currentItem = 0
        //vpProductPhoto.
        //Fetching ProductDetailPhotoListAdapter
        rvColorList.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rvColorList.itemAnimator = DefaultItemAnimator()
        rvSizeList.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rvSizeList.itemAnimator = DefaultItemAnimator()
        productData.images!![0].isSelectedImage = true
        //
        //removed
        tvProductName.text = productData.name
        //
        //removed
        ratingBar.rating = productData.rating!!.toFloat()
        tvRating.text = productData.rating.toString()
        tvDescription.text = productData.description!!
        tvToolbarTitle.text = productData.name
        tvToolbarTitle.visibility = View.VISIBLE



        btn_add_to_cart.setOnClickListener {

            /*     if(isCheckAvailable){
                     if(isConfirmCheckAvailable){

                         val currentLocation = Helper.getCurrentLocation(this)
                         if (currentLocation!=null && currentLocation.isNotEmpty() && currentLocation.equals(sharedPreference.country,true)) {
                             if (Helper.isNetworkAvailable(this)) {
                                 postAddToCart(9999, productData.id!!, productData)
                                 llButtonParent.visibility = View.GONE
                                 btn_home.visibility = View.VISIBLE
                             } else {

                                 Toast.makeText(
                                     baseContext,
                                     resources.getString(R.string.message_no_internet_connection),
                                     Toast.LENGTH_SHORT
                                 ).show()
                             }
                         }else{
                             Toast.makeText(baseContext,"Please Select your native country",Toast.LENGTH_SHORT).show()
                         }
                     }else{
                         showToast(isConfirmCheckAvailableError)
                     }

                 }else{
                     showToast("Please check first available for this product")
                 }*/

            val currentLocation = Helper.getCurrentLocation(activity!!)
            if (currentLocation != null && currentLocation.isNotEmpty() && currentLocation.equals(
                    sharedPreference.country,
                    true
                )
            ) {
                if (Helper.isNetworkAvailable(activity!!)) {
                    if (currentBarCodeId.isNullOrEmpty()) {
                        showDialog()
                    } else {
                        postAddToCart(9999, productData.id!!, currentBarCodeId, productData)
                    }

//                    llButtonParent.visibility = View.GONE
//                    btn_home.visibility = View.VISIBLE
                    Log.e("CartAdd", "AddedCalled")
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.message_no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        iv_heart.setOnClickListener {
            if (Helper.getCurrentLocation(activity!!).equals(sharedPreference.country)) {
                if (Helper.isNetworkAvailable(activity!!)) {
                    if (productData.is_wishlist == 0) {
                        postAddToWishList(productData.id!!)
                        productData.is_wishlist = 1
                        iv_heart.setImageResource(R.drawable.ic_heart_fill)
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.product_added_to_wishlist_successfully),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        postAddToWishList(productData.id!!)
                        productData.is_wishlist = 0
                        iv_heart.setImageResource(R.drawable.ic_heart_un_fill_black)

                        Toast.makeText(
                            activity,
                            resources.getString(R.string.product_remove_to_wishlist_successfully),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    //val intent = Intent();
                    //intent.putExtra("productdata", productData);
                    //setResult(1001, intent)
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.message_no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        iv_share.setOnClickListener {

            if (!Helper.isReadExternalStoragePermissionAllowed(activity!!)) {


                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            } else {
                val view =
                    vpProductPhoto.findViewWithTag<View>("myview" + vpProductPhoto.getCurrentItem());

                shareBitMapToOtherApps(view)
            }

        }
    }

    fun setBarcodeProducts(barcodeProducts: ArrayList<ProductDetailDataModel.ProductData.MesurementUnit>?) {
        try {
            Log.e("setBarcodeProducts", productDetailData.toString())
            if (barcodeProducts?.size!! > 0) {
                barcodeProducts[0].isSelected = true
                if (barcodeProducts[0].color_info!!.size > 0) {
                    barcodeProducts[0].color_info!![0].isSelected = true
//                    Log.e("getquantoty",barcodeProducts[0].color_info!![0].quantity!!.toString())
                    try {
                        if (barcodeProducts[0].color_info!![0].quantity!! >= 1) {
                            currentBarCodeId =
                                barcodeProducts[0].color_info!![0].id.toString()
                        } else {
                            tv_btn_stock.setText(getString(R.string.out_of_stock))
                        }
                    } catch (e: java.lang.Exception) {
                        showDialog()
                        tv_btn_stock.setText(getString(R.string.out_of_stock))
                    }


                    productColorListAdapter = ProductColorListAdapter(
                        activity,
                        barcodeProducts[0].color_info!!
                    )
                    tvColorName.text = barcodeProducts[0].color_info!![0].color_name
                    isColorSelected = true
                    colorPosition = 0
                    rvColorList.adapter = productColorListAdapter
                    productPagerAdapter!!.updateListItem(barcodeProducts[sizePosition].color_info!![colorPosition].images!!)
                    productColorListAdapter!!.setRecyclerViewClickListener(object :
                        RecyclerViewClickListener {
                        override fun onClick(
                            id: Int,
                            position: Int,
                        ) {
                            try {
                                if (barcodeProducts[sizePosition].color_info!![colorPosition].quantity!! < 1) {
                                    showDialog()
                                    tv_btn_stock.setText(getString(R.string.out_of_stock))
                                } else {
                                    colorPosition = position
                                    tvColorName.text =
                                        barcodeProducts[sizePosition].color_info!![colorPosition].color_name
                                    Log.e("photosList", "updateListItem")
                                    productPagerAdapter!!.updateListItem(barcodeProducts[sizePosition].color_info!![colorPosition].images!!)
                                    currentBarCodeId =
                                        barcodeProducts[sizePosition].color_info!![colorPosition].id.toString()

                                }
                            } catch (e: java.lang.Exception) {
                                showDialog()
                                tv_btn_stock.setText(getString(R.string.out_of_stock))
                            }

                        }
                    })
                    colorLinerLayout.visibility = View.VISIBLE
                } else {
                    colorLinerLayout.visibility = View.GONE
                }

            }
        } catch (e: Exception) {
        }
        val productDetailPhotoListAdapter =
            barcodeProducts?.let { ProductDetailPhotoListAdapter(activity, it) }
        sizePosition = 0
        if (barcodeProducts?.size!! > 0) {
            sizeLinerLayout.visibility = View.VISIBLE
        } else {
            sizeLinerLayout.visibility = View.GONE
        }
        productDetailPhotoListAdapter?.setRecyclerViewClickListener(object :
            RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int,
            ) {
                //productData.barcodes!![position].isSelected = true
                // productDetailPhotoListAdapter.notifyDataSetChanged()
                sizePosition = position
                productColorListAdapter?.updateListItems(barcodeProducts?.get(position)?.color_info!!)
                if (barcodeProducts?.get(position)?.color_info!!.size > 0) {
                    colorLinerLayout.visibility = View.VISIBLE
                    tvSellingPrice.text =
                        barcodeProducts[sizePosition].color_info!![0].selling_price.toString() + " " + productDetailData?.currency
                    Log.e(
                        "getquantoty2",
                        barcodeProducts[sizePosition].color_info!![0].quantity!!.toString()
                    )

                    if (barcodeProducts[sizePosition].color_info!![0].quantity!! >= 1) {
                        currentBarCodeId =
                            barcodeProducts[sizePosition].color_info!![0].id.toString()
                    }


                } else {
                    colorLinerLayout.visibility = View.GONE
                }
                //tvMainPrice.text = productData.barcodes!![sizePosition].price.toString() + " " + productData.currency
                if (barcodeProducts[sizePosition].color_info!!.size > colorPosition) {
                    tvSellingPrice.text =
                        barcodeProducts[sizePosition].color_info!![colorPosition].selling_price.toString() + " " + productDetailData?.currency
                    Log.e(
                        "getquantoty3",
                        barcodeProducts[sizePosition].color_info!![colorPosition].quantity!!.toString()
                    )

                    if (barcodeProducts[sizePosition].color_info!![colorPosition].quantity!! >= 1) {
                        currentBarCodeId =
                            barcodeProducts[sizePosition].color_info!![colorPosition].id.toString()
                    }


                } else {

//                    tvSellingPrice.text = barcodeProducts[sizePosition].color_info?.get(0)?.price +" AED"
                    tvSellingPrice.text =
                        productDetailData?.main_price.toString() + " " + productDetailData?.currency
                }
                vpProductPhoto.currentItem = position;
            }
        })
        if (productPagerAdapter != null) {
            productPagerAdapter!!.setRecyclerViewClickListener(object : RecyclerViewClickListener {
                override fun onClick(id: Int, position: Int) {
                    if (id == R.id.imgDisplay) {
                        if (isColorSelected) {
                            startActivity(
                                Intent(
                                    activity,
                                    FullscreenImageActivity::class.java
                                ).putParcelableArrayListExtra(
                                    "image",
                                    barcodeProducts?.get(sizePosition)?.color_info!![colorPosition].images
                                )
                            )
                        } else {
                            startActivity(
                                Intent(
                                    activity,
                                    FullscreenImageActivity::class.java
                                ).putParcelableArrayListExtra(
                                    "image", productDetailData?.images
                                )
                            )
                        }

                    }
                }

            })
        }

        ivBtnLeft.setOnClickListener {
            if (vpProductPhoto.currentItem != 0) {
                vpProductPhoto.currentItem = vpProductPhoto.currentItem - 1
                if (productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)?.isSelected == true) {
                    productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)
                        ?.isSelected = false

                } else {
                    for (image in barcodeProducts!!) {
                        image.isSelected = false
                    }
                    productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)
                        ?.isSelected = true
                }
                productDetailPhotoListAdapter?.notifyDataSetChanged()

            }
        }

        ivBtnRight.setOnClickListener {
            if (vpProductPhoto.currentItem < vpProductPhoto.adapter!!.count)
                vpProductPhoto.currentItem = vpProductPhoto.currentItem + 1
            if (productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)?.isSelected == true) {


                if (vpProductPhoto.currentItem != productDetailData?.images!!.size - 1) {
                    productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)
                        .isSelected = false

                }


            } else {
                for (image in productDetailData?.images!!) {
                    image.isSelectedImage = false
                }
                productDetailPhotoListAdapter?.getItem(vpProductPhoto.currentItem)
                    ?.isSelected = true
            }
            productDetailPhotoListAdapter?.notifyDataSetChanged()
        }

        if (barcodeProducts?.size!! > 0) {
            if (barcodeProducts[0]?.color_info?.size!! > 0) {
                if (barcodeProducts[0]?.color_info!![0].price?.toDouble() == barcodeProducts[0].color_info!![0].selling_price?.toDouble()) {
                    tvMainPrice.visibility = View.GONE
                    tvDiscount.visibility = View.GONE
                } else {
                    tvMainPrice.visibility = View.VISIBLE
                    tvDiscount.visibility = View.VISIBLE
                }
                tvMainPrice.text =
                    barcodeProducts.get(0).color_info?.get(0)?.price + " " + productDetailData?.currency
                Log.e(
                    "testcurrency",
                    barcodeProducts.get(0).color_info?.get(0)?.selling_price + " " + productDetailData?.currency + " == " + productDetailData?.main_price.toString()
                )

                tvSellingPrice.text =
                    barcodeProducts.get(0).color_info?.get(0)?.selling_price + " " + productDetailData?.currency
            }

        } else {
            if (productDetailData?.main_price?.toDouble() == productDetailData?.selling_price?.toDouble()) {
                tvMainPrice.visibility = View.GONE
                tvDiscount.visibility = View.GONE
            } else {
                tvMainPrice.visibility = View.VISIBLE
                tvDiscount.visibility = View.VISIBLE
            }
            tvMainPrice.text =
                productDetailData?.main_price.toString() + " " + productDetailData?.currency

            tvSellingPrice.text =
                productDetailData?.selling_price.toString() + " " + productDetailData?.currency
        }
        rvSizeList.adapter = productDetailPhotoListAdapter
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.not_available))
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }
        builder.show()
    }


    fun postAddToCart(
        position: Int,
        productId: Int,
        barcodeId: String,
        ductData: ProductDetailDataModel.ProductData
    ) {
        Log.e("CartAdd1", "AddedCalled")

        productViewModel.postAddToCart(
            sharedPreference.accessToken,
            productId,
            sharedPreference.storeId,
            1,
            barcodeId,
            object :
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.e("CartAdd2", "AddedCalled")
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {

                        Log.e("CartAdd3", "AddedCalled")
                        val jsonObject = Gson().toJson(response.body())

                        try {
                            val jsonObject1 = JSONObject(jsonObject)

                            val cartId = jsonObject1.getInt("cart_id")
                            if (position != 9999) {
                                relatedProductAdapter.getItem(position).cart_id = cartId
                                relatedProductAdapter.getItem(position).is_added = 1
                                relatedProductAdapter.notifyDataSetChanged()
                            } /*else {
                                val intent = Intent();
                                productData.is_added = 1
                               productData.cart_id = cartId
                                intent.putExtra("productdata", productData);
                                setResult(1001, intent)
                            } */

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Log.e("CartAdd4", "AddedCalled")
                        EventBus.getDefault().postSticky(SelectCartEventPojo(false, true))
                        getCartForCarCount()
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.added_to_cart),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("CartAdd5", "AddedCalled")
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
        val imageUri = getImageUri(activity!!, getBitmapFromView(view))
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
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
                    try {
                        if (response.isSuccessful) {
                            if (response.body()!!.cart_list!!.size == 0) {
                                tv_cart_count.visibility = View.GONE
                            } else {
                                tv_cart_count.visibility = View.VISIBLE
                                var cartCount = 0
                                for (cart in response.body()!!.cart_list!!) {
                                    cartCount += cart.quantity!!
                                }
                                tv_cart_count.text = cartCount.toString()
                                BottomMenuHelper.showBadge(
                                    activity,
                                    homeActivity?.navigation, R.id.nav_cart, cartCount.toString()
                                )

                            }
                        }
                    } catch (e: Exception) {

                    }

                }

            })
    }


}