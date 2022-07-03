package com.hypermarket_android.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CategoriesRecyclerAdapter
import com.app.pharmadawa.ui.notification.DealsRecyclerAdapter
import com.app.pharmadawa.ui.notification.StoresRecyclerAdapter
import com.app.pharmadawa.ui.notification.TrendingRecyclerAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hypermarket_android.Adapter.HomeCategotyAdapter
import com.hypermarket_android.Adapter.RandomDataRecyclerAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.NotificationYasMartActivity
import com.hypermarket_android.activity.ProductListActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.*
import com.hypermarket_android.eventPojos.HomePageReloadEventPojo
import com.hypermarket_android.eventPojos.SelectCategoryEventPojo
import com.hypermarket_android.eventPojos.SelectDealsEventPojo
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.nestedrecycler.adapter.ParentItemAdapter
import com.hypermarket_android.nestedrecycler.model.ChildItem
import com.hypermarket_android.nestedrecycler.model.ParentItem
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.ui.home.AutoSuggestAdapter
import com.hypermarket_android.ui.home.MainSliderAdapter
import com.hypermarket_android.ui.home.SearchAdapter
import com.hypermarket_android.ui.home.SpinnerModel
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.CategoryViewModel
import com.hypermarket_android.viewModel.HomeSearchViewModel
import com.hypermarket_android.viewModel.ProductViewModel
import com.hypermarket_android.viewModel.TrendingViewModel
import kotlinx.android.synthetic.main.activity_store_info.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment() : BaseFragment() {
    private val TRIGGER_AUTO_COMPLETE = 100
    private val AUTO_COMPLETE_DELAY: Long = 300
    private var handler: Handler? = null
    private var autoSuggestAdapter: AutoSuggestAdapter? = null
    private lateinit var searchAdapter: SearchAdapter
    private var listOfCategory: ArrayList<CategoriesDataModel.categoryModel> = ArrayList()
    private var listOfTrendingProduct: ArrayList<TrendingDataModel.ProductListDataModel.ProductData> = ArrayList()
    private var listOfRandomProduct: ArrayList<RandomProductDataModel.ProductData> = ArrayList()
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var trendingViewModel: TrendingViewModel
    private lateinit var homeSearchViewModel: HomeSearchViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private var mainSliderAdapter: MainSliderAdapter? = null
    private lateinit var prductViewModel: ProductViewModel
    private var trendingRecyclerAdapter: TrendingRecyclerAdapter? = null
    private var randomproductsRecyclerAdapter: RandomDataRecyclerAdapter? = null
    private var dealsRecyclerAdapter: DealsRecyclerAdapter? = null
    private var categoriesRecyclerAdapter: CategoriesRecyclerAdapter? = null
    private var productListGridAdapter: HomeCategotyAdapter? = null
    private var parentItemAdapter: ParentItemAdapter? = null
    private var categoriesRecyclerAdapter1: CategoriesRecyclerAdapter? = null
    private var categoriesRecyclerAdapter2: CategoriesRecyclerAdapter? = null
    private var storesRecyclerAdapter: StoresRecyclerAdapter? = null
    private val selectProductList = ArrayList<SpinnerModel>()
    lateinit var homeActivity: HomeActivity
    private var categoryId: String? = null
    private var sort: String = ""
    //  private var deals: ArrayList<Deals> = ArrayList()



    private var listOfDeals: ArrayList<DealsDataModel.DealsData> = ArrayList()
    private lateinit var rvTrending: RecyclerView
    private lateinit var rvRandomProduct: RecyclerView
    private lateinit var rvCategory: RecyclerView
    private lateinit var rvSubCategory: RecyclerView
    private lateinit var rvSubCategory2: RecyclerView
    private lateinit var rvDeal:RecyclerView
    private lateinit var tvStoreName: TextView
    private lateinit var tv_search: TextView
    private lateinit var banner_img: AppCompatImageView
    private lateinit var edt_search: AppCompatAutoCompleteTextView
    private lateinit var ivCountryFlag: AppCompatImageView
    private lateinit var sharedPreferences: SharedPreferenceUtil
    private lateinit var productViewModel: ProductViewModel
    private var isBottomError: Boolean = false
    private var listOfProduct: ArrayList<TrendingDataModel.ProductListDataModel.ProductData> =
        ArrayList()

    private  var isCartWhislist=false
    //    var category: ArrayList<Category> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        rvDeal = view.findViewById<RecyclerView>(R.id.rv_deals)
        rvTrending = view.findViewById(R.id.rv_trending)
        rvRandomProduct = view.findViewById(R.id.rv_random_product)
        rvCategory = view.findViewById(R.id.rv_categories)
        rvSubCategory2 = view.findViewById(R.id.rv_sub_categories2)
        rvSubCategory = view.findViewById(R.id.rv_sub_categories)
        homeActivity = (activity as HomeActivity)
        //  val ivSelectStore = view.findViewById<ImageView>(R.id.iv_select_store)
        tvStoreName = view.findViewById(R.id.tv_store_name)
        tv_search = view.findViewById(R.id.tv_search)
        edt_search = view.findViewById(R.id.edt_search)
        val ivProfilePhoto = view.findViewById<ImageView>(R.id.iv_profile_photo)
        ivCountryFlag = view.findViewById(R.id.iv_country_flag)
        val tvCategoryViewAll = view.findViewById<TextView>(R.id._tv_category_view_all)
        val tvTrendingViewAll = view.findViewById<TextView>(R.id.tv_trending_all)
        val tvDealViewAll = view.findViewById<TextView>(R.id.tv_deals_all)
        banner_img = view.findViewById(R.id.banner_img)
        mainSliderAdapter = MainSliderAdapter(activity)
        sharedPreferences = SharedPreferenceUtil.getInstance(context!!)
        if (sharedPreferences.city == ""){
          tvStoreName.text = sharedPreferences.full_name + " - " + sharedPreferences.country
        }else{
            tvStoreName.text = sharedPreferences.full_name + " - " + sharedPreferences.city
        }
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        Glide.with(this).load(sharedPreferences.profile_image).placeholder(R.drawable.profile_dummy_img).thumbnail(0.1f).into(ivProfilePhoto)

//        if (sharedPreferences.country == "Oman") {
//            Log.d("country==", "Oman")
//            sharedPreferences.country_code = "+968"
//            ivCountryFlag.setImageResource(R.drawable.ic_oman)
//        } else
            if (sharedPreferences.country == "United Arab Emirates") {
            Log.d("country==", "dubai")
            sharedPreferences.country_code = "+971"
            ivCountryFlag.setImageResource(R.drawable.ic_united_arab_emirates)
            //sharedPreferences.
        }
        banner_img.setOnClickListener(View.OnClickListener {
            startActivity( Intent(activity, ProductListActivity::class.java)
                .putExtra("categoryId","127").putExtra("name","New Arrival"))
        })


        view.iv_notification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationYasMartActivity::class.java))
        }

        //setDeals
        setDealsData()
        //setTrendingProductData
        initializeViewModel()
        setTrendingProductData()
        setRandomProductData()
        //setCategoryData
        setCategoryData()
        setSubCategoryData()
        ivCountryFlag.setOnClickListener {
            countryDialog()
        }
        tvTrendingViewAll.setOnClickListener {
            //val bundle = Bundle()
            val trendingListFragment = TrendingProductListFragment()
            //bundle.putInt("product_id",
            //    autoSuggestAdapter!!.getProductId(position)
           // )
            //bundle.putString("act","trend")
            //productDetFragment.arguments = bundle
            val transaction = activity!!.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, trendingListFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
            /*startActivity(
                Intent(
                    activity!!,
                    TrendingProductactivity::class.java
                ).putParcelableArrayListExtra("listOfTrending", listOfTrendingProduct)
            ) */
        }
        tvCategoryViewAll.setOnClickListener {
            EventBus.getDefault().postSticky(SelectCategoryEventPojo(true))
        }
        tvDealViewAll.setOnClickListener {
            EventBus.getDefault().postSticky(SelectDealsEventPojo(true))
        }
        initViews()
        setUpSubCategory()


        return view
    }

    private fun setUpNestedRecycler() {

        // Initialise the Linear layout manager

        // Initialise the Linear layout manager
        val layoutManager = LinearLayoutManager(activity)

        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()

        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()

          parentItemAdapter = ParentItemAdapter(
            activity!!,
            ParentItemList(),
              productViewModel,
              sharedPreferences
        )

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview
        rvSubCategory2.adapter = parentItemAdapter
        rvSubCategory2.layoutManager = layoutManager
    }

    fun setUpSubCategory(){
        categoryId = "16"
        val name = "Mens Clothing"
        Log.e("Product ListData", categoryId!!)
//        Log.e("Product ListData", name!!)
        sharedPreferences = SharedPreferenceUtil.getInstance(activity!!)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        productViewModel.getProductList(
            sharedPreferences.accessToken,
            categoryId!!,
            sharedPreferences.storeId.toString(),
            1.toString(),
            sort
        )
        productViewModel.isNetworkError.observe(
            activity!!,
            { value ->
                try{
                    if (value == 0) {
                        isBottomError = false
                        progressbar!!.visibility = View.GONE
                        //GridAdapter
                        productListGridAdapter!!.footerVisibility(false, false)
                        productListGridAdapter!!.notifyDataSetChanged()
                    } else if (value == 1) {
//                        progressBar.visibility = View.GONE
//                        tvReload.visibility = View.VISIBLE
                    } else if (value == 2) {
                        isBottomError = true
                        //GridAdapter
                        productListGridAdapter!!.footerVisibility(true, false)
                        productListGridAdapter!!.notifyDataSetChanged()
                    }
                }catch (e:Exception){

                }

            })

        productViewModel.getProductListFromModel().observe(activity!!, androidx.lifecycle.Observer {

        })
        productViewModel.getProductListFromModel().observe(activity!!, androidx.lifecycle.Observer {

        if (productListGridAdapter == null) {
                    setUpNestedRecycler()
                    listOfProduct.addAll(it)
                    productListGridAdapter =
                        HomeCategotyAdapter(activity!!, listOfProduct)


                    //Bottom: Next Data
                    productListGridAdapter!!.setOnBottomReachedListener(object :
                        OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            Log.e("onBottomReached",position.toString())
                            if ((!productViewModel.isDataEnd.value!!) && (!isBottomError) == true) {
                                Log.e("bottom==", "bottom1")
                                productListGridAdapter!!.footerVisibility(false, true)

                                productViewModel.getProductList(
                                    sharedPreferences.accessToken,
                                    categoryId!!,
                                    sharedPreferences.storeId.toString(),
                                    (productListGridAdapter!!.getItem(0).currentPage!!.toInt() + 1).toString(),
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
                                val bundle = Bundle()
                                val productDetFragment = ProductDetailsFragment()
                                bundle.putInt("product_id",
                                    productListGridAdapter!!.getItem(position).id!!
                                )
                                bundle.putString("catId", categoryId)
                                bundle.putString("catName",name)
                                bundle.putString("act","home")
                                productDetFragment.arguments = bundle
                                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                                transaction?.replace(R.id.container, productDetFragment)
                                transaction?.disallowAddToBackStack()
                                transaction?.commit()

                        }
                    })
                    rvSubCategory.adapter = productListGridAdapter
                } else {
                    listOfProduct = it
                    productListGridAdapter!!.setList(listOfProduct)
                    productListGridAdapter!!.footerVisibility(false, false)
                }

            }
        )

//        tvReload.setOnClickListener {
//            if (Helper.isNetworkAvailable(activity!!)) {
//                productViewModel.getProductList(
//                    sharedPreferences.accessToken,
//                    categoryId!!,
//                    sharedPreferences.storeId.toString(),
//                    "1",
//                    ""
//                )
//            } else {
//                Toast.makeText(
//                    activity,
//                    resources.getString(R.string.message_no_internet_connection),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        iv_category.setOnClickListener {
//            EventBus.getDefault().postSticky(SelectCategoryEventPojo(true))
//            val intent = Intent(activity, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            activity!!.finish()
//        }
//        iv_filter.setOnClickListener {
//            val bottomSheet = ProductFilterDialogFragment()
//            bottomSheet.show(activity!!.supportFragmentManager, "ModalBottomSheet")
//        }
//        iv_view_type.setOnClickListener {
//            if (isLinear) {
//                isLinear = false
//                iv_view_type.setImageResource(R.drawable.ic_header_grid_view)
//                rvProductListGrid.visibility = View.VISIBLE
//                rvProductListLinear.visibility = View.GONE
//            } else {
//                isLinear = true
//                iv_view_type.setImageResource(R.drawable.ic_header_listview)
//                rvProductListGrid.visibility = View.GONE
//                rvProductListLinear.visibility = View.VISIBLE
//            }
//
//        }
//        iv_actionbar_back.setOnClickListener {
//            //onBackPressed()
//            activity!!.supportFragmentManager.popBackStack()
//        }
    }
    override fun initViews() {

        //Setting up the adapter for AutoSuggest
        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = AutoSuggestAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line
        )
        edt_search.setThreshold(2)
        edt_search.setAdapter(autoSuggestAdapter)
        edt_search.setOnItemClickListener(
            OnItemClickListener { parent, view, position, id ->
                Log.e("SearchPrdId",autoSuggestAdapter!!.getProductId(position).toString())
                val bundle = Bundle()
                val productDetFragment = ProductDetailsFragment()
                autoSuggestAdapter!!.getProductId(position)?.let {
                    bundle.putInt("product_id",
                        it
                    )
                }
                bundle.putString("act","trend")
                productDetFragment.arguments = bundle
                val transaction = activity!!.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container, productDetFragment)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
                tv_search.setText(
                    autoSuggestAdapter!!.getObject(position)
                )
            })

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                handler?.removeMessages(TRIGGER_AUTO_COMPLETE)
                handler?.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        handler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(edt_search.getText())) {
                    getSearchProduct(sharedPreferences.accessToken, sharedPreferences.storeId.toString(), "1",edt_search.getText().toString())

                }
            }
            false
        }

    }

    override fun initControl() {

    }

    fun initializeViewModel(){
        trendingViewModel = ViewModelProvider(this).get(TrendingViewModel::class.java)
        homeSearchViewModel = ViewModelProvider(this).get(HomeSearchViewModel::class.java)
    }

    fun setTrendingProductData() {
        rvTrending.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        trendingRecyclerAdapter = TrendingRecyclerAdapter(activity, listOfTrendingProduct)
        rvTrending.adapter = trendingRecyclerAdapter
//        getTrendingProduct(sharedPreferences.accessToken, sharedPreferences.storeId.toString(), "1")
        getAllTrendingProduct(sharedPreferences.accessToken)

    }

    fun setRandomProductData() {
        rvRandomProduct.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        randomproductsRecyclerAdapter = RandomDataRecyclerAdapter(activity, listOfRandomProduct)
        rvRandomProduct.adapter = randomproductsRecyclerAdapter
//        getTrendingProduct(sharedPreferences.accessToken, sharedPreferences.storeId.toString(), "1")
        getAllRandomProduct(sharedPreferences.accessToken)

    }

    fun setDealsData() {
        rvDeal.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        dealsRecyclerAdapter = DealsRecyclerAdapter(activity, listOfDeals)
        rvDeal.adapter = dealsRecyclerAdapter
        prductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        getDeals()

    }

    fun setCategoryData() {

        rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        //rvCategory.addItemDecoration(GridSpacingItemDecoration(10))
        categoriesRecyclerAdapter = CategoriesRecyclerAdapter(activity, listOfCategory)
        rvCategory.adapter = categoriesRecyclerAdapter


        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        getCategory(sharedPreferences.storeId)

    }

    fun setSubCategoryData() {

        rvSubCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        //rvCategory.addItemDecoration(GridSpacingItemDecoration(10))
//        categoriesRecyclerAdapter1 = CategoriesRecyclerAdapter(activity, listOfCategory)
//        rvSubCategory.adapter = categoriesRecyclerAdapter1


        rvSubCategory2.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        //rvCategory.addItemDecoration(GridSpacingItemDecoration(10))
//        categoriesRecyclerAdapter2 = CategoriesRecyclerAdapter(activity, listOfCategory)
//        rvSubCategory2.adapter = categoriesRecyclerAdapter2

    }


    fun getCategory(storeId: Int) {
//        progressBar.visibility = View.VISIBLE
        //tvReload.visibility = View.GONE

        categoryViewModel.getCategory(sharedPreferences.accessToken,storeId, object : Callback<CategoriesDataModel> {
            override fun onFailure(call: Call<CategoriesDataModel>, t: Throwable) {
                //progressBar.visibility = View.GONE
                //tvReload.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<CategoriesDataModel>,
                response: Response<CategoriesDataModel>
            ) {

                if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.GONE
                    listOfCategory.clear()
                    listOfCategory.addAll(response.body()!!.categories!!)
                    categoriesRecyclerAdapter!!.notifyDataSetChanged()
//                    categoriesRecyclerAdapter1!!.notifyDataSetChanged()
//                    categoriesRecyclerAdapter2!!.notifyDataSetChanged()

                }
            }

        })

    }


    fun getTrendingProduct(accessToken: String, storeId: String, page: String) {
//        progressBar.visibility = View.VISIBLE
        //tvReload.visibility = View.GONE

        trendingViewModel.getTrendingProducts(
            accessToken,
            storeId,
            page,
            object : Callback<TrendingDataModel> {
                override fun onFailure(call: Call<TrendingDataModel>, t: Throwable) {
                    //progressBar.visibility = View.GONE
                    //tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<TrendingDataModel>,
                    response: Response<TrendingDataModel>
                ) {

                    if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.GONE
                        listOfTrendingProduct.clear()
                        listOfTrendingProduct.addAll(response.body()!!.product_list.data)

                        Log.d("size==", listOfTrendingProduct.size.toString())
                        trendingRecyclerAdapter!!.notifyDataSetChanged()

                    } else {
                       /* if(response.code()==401){
                            LoginManager.getInstance().logOut()
                            if (prefs.loginguest == true) {
                                prefs.loginguest = false
                            }
                            if (prefs.loginStatusSocial == true) {
                                prefs.accessToken = ""
                                prefs.loginStatusSocial = false
                                prefs.full_name = ""
                                prefs.email = ""
                                prefs.profile_image = ""
                            }
                            if (prefs.loginStatus == true) {
                                prefs.accessToken = ""
                                prefs.loginStatus = false
                                prefs.full_name = ""
                                prefs.email = ""
                                prefs.mobile_no = ""
                                prefs.profile_image = ""
                            }
                            showToast(requireActivity(),"Unauthorized:You are already login with other device.")
                            activity?.let {
                                val intent = Intent(it, LoginActivity::class.java)
                                it.startActivity(intent)
                                it.finish()
                            }
                        }*/
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.VISIBLE
                    }
                }

            })
    }

    fun getAllTrendingProduct(accessToken: String) {
//        progressBar.visibility = View.VISIBLE
        //tvReload.visibility = View.GONE

        trendingViewModel.getAllTrendingProducts(
            accessToken,
            object : Callback<TrendingDataModel> {
                override fun onFailure(call: Call<TrendingDataModel>, t: Throwable) {
                    //progressBar.visibility = View.GONE
                    //tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<TrendingDataModel>,
                    response: Response<TrendingDataModel>
                ) {

                    if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.GONE
                        listOfTrendingProduct.clear()
                        listOfTrendingProduct.addAll(response.body()!!.product_list.data)

                        Log.d("size==", listOfTrendingProduct.size.toString())
                        trendingRecyclerAdapter!!.notifyDataSetChanged()

                    } else {

                    }
                }

            })
    }

    fun getAllRandomProduct(accessToken: String) {
//        progressBar.visibility = View.VISIBLE
        //tvReload.visibility = View.GONE

        trendingViewModel.getAllRandomProducts(
            accessToken,
            "64",
            object : Callback<RandomProductDataModel> {
                override fun onFailure(call: Call<RandomProductDataModel>, t: Throwable) {
                    Log.e("onfailure",t.message.toString() +" == "+call.toString())
                    //progressBar.visibility = View.GONE
                    //tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<RandomProductDataModel>,
                    response: Response<RandomProductDataModel>
                ) {

                    if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.GONE
                        listOfRandomProduct.clear()
                        listOfRandomProduct.addAll(response.body()!!.product_list)

                        Log.d("size==", listOfRandomProduct.size.toString())
                        randomproductsRecyclerAdapter!!.notifyDataSetChanged()

                    } else {

                    }
                }

            })
    }


    fun getSearchProduct(accessToken: String, storeId: String, page: String,productName :String) {
//        progressBar.visibility = View.VISIBLE
        //tvReload.visibility = View.GONE

        homeSearchViewModel.getsearchProduct(
            accessToken,
            storeId,
            page,
            productName,
            object : Callback<HomeSearchResponse> {
                override fun onFailure(call: Call<HomeSearchResponse>, t: Throwable) {
                    //progressBar.visibility = View.GONE
                    //tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<HomeSearchResponse>,
                    response: Response<HomeSearchResponse>
                ) {

                    if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    tvReload.visibility = View.GONE
                      /*  listOfTrendingProduct.clear()
                        listOfTrendingProduct.addAll(response.body()!!.product_list.data)*/

                        val stringList: MutableList<String> = ArrayList()
                       /* try {

                            for (i in 0 until (response.body()?.product_list?.data?.size!!)) {

                                stringList.add(response.body()?.product_list?.data!![i].name)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } */
                        //IMPORTANT: set data here and notify
                        //IMPORTANT: set data here and notify
                        var productList = emptyList<UserData>()
                        if (response.body()?.categories?.data?.size!! > 0) {
                             productList = response.body()?.product_list?.data?.mix(
                                 response.body()?.categories?.data
                             )!!
                        }else{
                            productList = response.body()?.product_list?.data!!
                        }
                        autoSuggestAdapter!!.setData(productList)
                        autoSuggestAdapter!!.notifyDataSetChanged()

     /*                   val adp2 = Commentsadapter(
                            response.body()!!, response.body()?.product_list!!.data,
                            object : Commentsadapter.commentsdeleteClickListner {

                                // delete for item
                                override fun oncommentsdeleteClick(position: Int) {

                                }
                                override fun oncommentsupdateClick(
                                    position: Int,
                                    review_txt: String
                                ) {




                                }
                            })
                        rv_serach.visibility = View.VISIBLE
                        rv_serach.setHasFixedSize(true)
                        rv_serach.adapter = adp2



                        selectProductList.clear()
                       *//* selectProductList.add(
                            SpinnerModel(
                                id = "0",
                                name = requireActivity().getString(R.string.category)
                            )
                        )*//*



                        for (element in response.body()!!.product_list.data) {
                            Log.e("Bharat", element.name!!)
                            selectProductList.add(SpinnerModel(element.id.toString(), element.name))
                        }

                        sp_category.adapter = CustomDropDownAdapter(requireActivity(), selectProductList)

                        sp_category?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {

                                (sp_category.getSelectedView() as TextView).setTextColor(
                                    resources.getColor(
                                        R.color.black
                                    )
                                )
                                (sp_category.getSelectedView().setBackgroundColor(
                                    resources.getColor(
                                        R.color.white
                                    )
                                ))

                                if (selectProductList[position].id != 0.toString()) {

                                  val productID = selectProductList[position].id
                                  val  productName = selectProductList[position].name
                               *//*     context!!.startActivity(
                                        Intent(
                                            context,
                                            ProductDetailActivity::class.java
                                        ).putExtra("product_id", productID)

                                    )*//*

                                } else {
                                  val  productid = selectProductList[position].id



                                }
                            }
                        }

                        Log.d("size==", response.body()?.product_list.toString())*/
                     /*   trendingRecyclerAdapter!!.notifyDataSetChanged()*/
                    } else {

                    }
                }

            })
    }

    fun <T> List<T>.mix(other: List<T>?): List<T> {
        val first = iterator()

        var list: ArrayList<T>? = null
        other?.let {
            list = ArrayList<T>(minOf(this.size, other.size))
            val second = it.iterator()
            while (first.hasNext() && second.hasNext()) {
                list!!.add(first.next())
                list!!.add(second.next())
            }
        }

        return list!!
    }

    fun getDeals() {
        prductViewModel.getDeals(
            sharedPreferences.accessToken,
            sharedPreferences.storeId,
            object : Callback<DealsDataModel> {
                override fun onFailure(call: Call<DealsDataModel>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<DealsDataModel>,
                    response: Response<DealsDataModel>
                ) {
                    if(response.isSuccessful){
                        Log.e("DealData",response.body()!!.deals.toString())
                        listOfDeals.clear()
                        listOfDeals.addAll(response.body()!!.deals)
                        dealsRecyclerAdapter!!.notifyDataSetChanged()
                    }
                }

            })
    }

    @SuppressLint("ResourceAsColor")
    private fun countryDialog() {

        val dialog = Dialog(context!!, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_dialog_select_country)
        dialog.setCancelable(true)
        dialog.show()

        val llCountryDubai = dialog.findViewById<LinearLayout>(R.id.ll_dubai)
        val tvCountryDubai = dialog.findViewById<TextView>(R.id.tv_country_uae)
        val llCountryOman = dialog.findViewById<LinearLayout>(R.id.ll_oman)
        val tvCountryOman = dialog.findViewById<TextView>(R.id.tv_country_oman)
        val tvSubmit = dialog.findViewById<TextView>(R.id.tv_submit)

        var selectedCountry = ""

        getCheckCartListAndWishList()


        if (sharedPreferences.country == "United Arab Emirates") {

            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryDubai.setTextColor(Color.parseColor("#18A44E"));
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryOman.setTextColor(Color.parseColor("#999999"));


        } else if (sharedPreferences.country == "Oman") {
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryOman.setTextColor(Color.parseColor("#18A44E"));

            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryDubai.setTextColor(Color.parseColor("#999999"));
        }

        llCountryDubai.setOnClickListener {
            selectedCountry = "United Arab Emirates"
            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryDubai.setTextColor(Color.parseColor("#18A44E"));
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryOman.setTextColor(Color.parseColor("#999999"));

        }

        llCountryOman.setOnClickListener {
            selectedCountry = "Oman"
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryOman.setTextColor(Color.parseColor("#18A44E"));

            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryDubai.setTextColor(Color.parseColor("#999999"));

        }

        tvSubmit.setOnClickListener {
            if (selectedCountry.isNotEmpty()) {
                sharedPreferences = SharedPreferenceUtil.getInstance(context!!)


                if (sharedPreferences.country !== selectedCountry) {
                    //   sharedPreferences.storeId = 0
           /*         if (sharedPreferences.country == "Oman") {
                        sharedPreferences.storeId = 2
                        sharedPreferences.storeName = "Yas for Modern center"
                        ivCountryFlag.setImageResource(R.drawable.ic_oman)
                    } else if (sharedPreferences.country == "United Arab Emirates") {
                        sharedPreferences.storeId = 1
                        sharedPreferences.storeName = "YasMart BaniYas"
                        ivCountryFlag.setImageResource(R.drawable.ic_united_arab_emirates)

                    }
                    tvStoreName.text = (sharedPreferences.storeName)

                    dialog.dismiss()
                    EventBus.getDefault().postSticky(HomePageReloadEventPojo(true))*/

                    if (isCartWhislist){
                        alertDialog(selectedCountry)

                    }else{
                        sharedPreferences.country = selectedCountry
                        if (sharedPreferences.country == "Oman") {
                            sharedPreferences.storeId = 2
                            sharedPreferences.storeName = "Yas for Modern center"
                            ivCountryFlag.setImageResource(R.drawable.ic_oman)
                        } else if (sharedPreferences.country == "United Arab Emirates") {
                            sharedPreferences.storeId = 1
                            sharedPreferences.storeName = "YasMart BaniYas"
                            ivCountryFlag.setImageResource(R.drawable.ic_united_arab_emirates)

                        }
                        tvStoreName.text = (sharedPreferences.country)

                        EventBus.getDefault().postSticky(HomePageReloadEventPojo(true))
                    }
                    dialog.dismiss()

                } else {

                    //sharedPreferences.country = selectedCountry
                    dialog.dismiss()

                }


            } else {
                dialog.dismiss()
            }

        }

    }


    @SuppressLint("ResourceAsColor")
    private fun alertDialog(selectedCountry:String) {

        val dialog = Dialog(context!!, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_dialog_alert)
        dialog.setCancelable(true)
        dialog.show()


        val tvCancel=dialog.findViewById<TextView>(R.id.tv_cancel)
        val tvOk=dialog.findViewById<TextView>(R.id.tv_ok)



        tvOk.setOnClickListener {

            if (Helper.isNetworkAvailable(activity!!)){
                postRemoveCartListAndWishList(selectedCountry)
                dialog.dismiss()
            }else{
                Toast.makeText(activity,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
            }

        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }




    }

    fun postRemoveCartListAndWishList(selectedCountry: String){
        prductViewModel.postRemoveCartListAndWishList(sharedPreferences.accessToken,object :Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful){
                    sharedPreferences.country = selectedCountry

                    if (sharedPreferences.country == "Oman") {
                        sharedPreferences.storeId = 2
                        sharedPreferences.storeName = "Yas for Modern center"
                        ivCountryFlag.setImageResource(R.drawable.ic_oman)
                    } else if (sharedPreferences.country == "United Arab Emirates") {
                        sharedPreferences.storeId = 1
                        sharedPreferences.storeName = "YasMart BaniYas"
                        ivCountryFlag.setImageResource(R.drawable.ic_united_arab_emirates)

                    }
                    tvStoreName.text = (sharedPreferences.country)

                    EventBus.getDefault().postSticky(HomePageReloadEventPojo(true))
                }
            }

        })
    }

    fun getCheckCartListAndWishList(){
        prductViewModel.getCheckCartListAndWishList(sharedPreferences.accessToken,object :Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful){

                    val jsonObject = Gson().toJson(response.body())

                    Log.d("response==", jsonObject)

                  //  Log.d("response==", "c="+count)
                    try {
                        val jsonObject1 = JSONObject(jsonObject)

                        isCartWhislist=jsonObject1.getBoolean("is_exist")
                        // listOfCart[position].price
                     //   val cartId = jsonObject1.getInt("cart_id")
                        //  productListGridAdapter!!.getItem(position).cart_id = cartId

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
//                    isCartWhislist=true

                }
            }

        })
    }


    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(sharedPreferences.language != null){
            if(sharedPreferences.language == "en"){
                setLocale("en")
            } else if(sharedPreferences.language == "ar"){
                setLocale("ar")
            }else{
                setLocale("en")
            }
        }
    }

    private fun ParentItemList(): List<ParentItem>? {
        val itemList: MutableList<ParentItem> = ArrayList()
        productViewModel.getProductListFromModel().observe(activity!!, androidx.lifecycle.Observer {

            if (listOfCategory.size>0) {
                val item = listOfCategory.get(0).subCategory?.let { it1 ->
                    ParentItem(
                        it1,
                        it
                    )
                }
                item?.let { it1 -> itemList.add(it1) }
//            val item1 = ParentItem(
//                "Title 2",
//                it
//            )
//            itemList.add(item1)
//            val item2 = ParentItem(
//                "Title 3",
//                it
//            )
//            itemList.add(item2)
//            val item3 = ParentItem(
//                "Title 4",
//                it
//            )
//            itemList.add(item3)
                parentItemAdapter?.setdata()
                parentItemAdapter?.notifyDataSetChanged()
            }
        })


        return itemList
    }

    private fun ChildItemList(): List<ChildItem>? {
        val ChildItemList: MutableList<ChildItem> = ArrayList()
        ChildItemList.add(ChildItem("Card 1"))
        ChildItemList.add(ChildItem("Card 2"))
        ChildItemList.add(ChildItem("Card 3"))
        ChildItemList.add(ChildItem("Card 4"))
        return ChildItemList
    }
}
