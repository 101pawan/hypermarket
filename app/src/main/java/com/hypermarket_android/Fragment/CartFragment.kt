package com.hypermarket_android.Fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CartListAdapter
import com.app.pharmadawa.ui.notification.SaveForLaterAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.ProductDetailActivity
import com.hypermarket_android.activity.SelectAddressActivity
import com.hypermarket_android.activity.SelectPaymentModeActivity
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.dataModel.CartDataModel
import com.hypermarket_android.dataModel.SaverLaterDataModel
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.ProductViewModel
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.BottomMenuHelper
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : BaseFragment() {

    private var listOfCart: ArrayList<CartDataModel.CartData> = ArrayList()
    private var listOfSaveForLater: ArrayList<SaverLaterDataModel.SaverLaterData> = ArrayList()
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartAdapter: CartListAdapter
    private lateinit var saveForLaterAdapter: SaveForLaterAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var llParentLayout: LinearLayout
    private lateinit var tv_empty_message: TextView
    private lateinit var tv_total:TextView
    private lateinit var sharedPreferences: SharedPreferenceUtil
    private var isIncrease: Boolean = false
    private var count = 0
    private var total: Double = 0.toDouble()
    var quantity = ""
    var product_id = ""
    var barcodeId = ""
    var productColor = ""
    companion object {
        var cartCount = "0"
    }
    var homeActivity: HomeActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        progressBar = view.findViewById(R.id.progressbar)
        tvReload = view.findViewById(R.id.tv_reload)
        homeActivity = activity as HomeActivity
        val btnProceedToCheckOut = view.findViewById<Button>(R.id.btn_proceed)
        val rvCart = view.findViewById<RecyclerView>(R.id.rv_cart)
        val cbPoints = view.findViewById<CheckBox>(R.id.cb_points)
        val btnApply = view.findViewById<Button>(R.id.btn_apply)
        tv_empty_message = view.findViewById(R.id.tv_empty_message)
        tv_total = view.findViewById(R.id.tv_total)
        llParentLayout = view.findViewById(R.id.ll_parent_layout)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0)
        productViewModel = ViewModelProvider(this.requireActivity()).get(ProductViewModel::class.java)
        sharedPreferences = SharedPreferenceUtil.getInstance(context!!)
        if (listOfCart.size == 0) {
            llParentLayout.visibility = View.GONE
        }
        //   rvCart.isNestedScrollingEnabled=false
        val layoutManager = LinearLayoutManager(context)
        rvCart.setLayoutManager(layoutManager);
        rvCart.setNestedScrollingEnabled(false);
        cartAdapter = CartListAdapter(activity!!, listOfCart)
        cartAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (!progressBar.isShown) {
                    progressBar.visibility = View.VISIBLE
                    manageItems(id, position)
                }
            }
        })
        rvCart.adapter = cartAdapter

        if (Helper.isNetworkAvailable(activity!!)) {
            if(sharedPreferences.accessToken != null && sharedPreferences.accessToken != ""){
                getCart()
            }
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                getCart()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnProceedToCheckOut.setOnClickListener {

            var totalBarcodeId = ""
            var totalColorId = ""
            var totalProductId = ""
            var totalQuantity = ""
            var totalPrice = ""
            var total = 0.0
            for(item in listOfCart){
                if (totalBarcodeId == ""){
                    totalBarcodeId = item.barcode_id.toString()
                }else{
                    totalBarcodeId += ","+item.barcode_id.toString()
                }

                if (totalColorId == ""){
                    totalColorId = item.colored_images_id.toString()
                }else{
                    totalColorId += ","+item.colored_images_id.toString()
                }

                if (totalProductId == ""){
                    totalProductId = item.product_id.toString()
                }else{
                    totalProductId += ","+item.product_id.toString()
                }

                if (totalQuantity == ""){
                    item.price?.let {
                        item.quantity?.let { qty ->
                            Log.e("checkprices",(it.toDouble() * qty.toDouble()).toString())
                            total += it.toDouble() * qty.toDouble()
                        }
                    }

                    totalQuantity = item.quantity.toString()
                }else{
                    item.price?.let {
                        item.quantity?.let { qty ->
                            Log.e("checkprices",(it.toDouble() * qty.toDouble()).toString())
                            total += it.toDouble() * qty.toDouble()
                        }
                    }
                    totalQuantity += ","+item.quantity.toString()
                }

                if (totalPrice == ""){
                    totalPrice = item.price.toString()
                }else{
                    totalPrice += ","+item.price.toString()
                }
            }

            SelectPaymentModeActivity.totalAmount = total.toString()
            Log.e("lastcheckout",totalBarcodeId+" == "+totalProductId+" == "+totalQuantity+" == "+totalPrice +" =="+total)
            SelectPaymentModeActivity.product_id = totalProductId
            SelectPaymentModeActivity.quantity = totalQuantity
            SelectPaymentModeActivity.barCodeId = totalBarcodeId
            SelectPaymentModeActivity.totalPrice = totalPrice
            SelectPaymentModeActivity.totalColor = totalColorId

            startActivity(Intent(activity, SelectAddressActivity::class.java))
            // Toast.makeText(context,"UnderDevelopment",Toast.LENGTH_SHORT).show()
        }


        btnApply.setOnClickListener {
            Toast.makeText(context,  resources.getString(R.string.under_development), Toast.LENGTH_SHORT).show()
        }
        getCart()
        return view
    }

    fun manageItems(id:Int, position: Int){
        Log.e("manageitems",id.toString()+" == "+ R.id.tv_remove)
        if (id == R.id.ll_parent_layout) {
            val bundle = Bundle()
            val productDetFragment = ProductDetailsFragment()
            bundle.putInt("product_id",
                cartAdapter.getItem(position).product_id!!
            )
            bundle.putString("act","Cart")
            productDetFragment.arguments = bundle
            val transaction = activity!!.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, productDetFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
            /*startActivity(
                Intent(
                    context,
                    ProductDetailActivity::class.java
                ).putExtra(
                    "product_id",
                    cartAdapter.getItem(position).product_id
                )
            )*/
        } else if (id == R.id.iv_plus) {
            if (Helper.getCurrentLocation(activity!!).equals(sharedPreferences.country)) {
                if (cartAdapter.getItem(position).available_qty!! > 0) {
                    if (Helper.isNetworkAvailable(activity!!)) {
                        progressbar.visibility = View.VISIBLE
                        count = cartAdapter.getItem(position).quantity!!
                        count++
                        cartAdapter.getItem(position).quantity = count
                        cartAdapter.getItem(position).available_qty =
                            cartAdapter.getItem(position).available_qty!! - 1
                        cartAdapter.notifyDataSetChanged()
//                        productViewModel.getCartObserable(accessToken = sharedPreferences.accessToken)
                        postUpdateAddToCart(
                            position,
                            cartAdapter.getItem(position).product_id!!,
                            cartAdapter.getItem(position).quantity!!,
                            cartAdapter.getItem(position).id!!,
                            cartAdapter.getItem(position).barcode_id.toString()
                        )
                        getTotalCost()
                    } else {
                        Toast.makeText(
                            activity!!,
                            resources.getString(R.string.message_no_internet_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Quantity not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (id == R.id.iv_minus) {
            if (Helper.getCurrentLocation(activity!!).equals(sharedPreferences.country)) {
                if (cartAdapter.getItem(position).quantity!! > 0) {
                    progressbar.visibility = View.VISIBLE
                    count = cartAdapter.getItem(position).quantity!!
                    count--
                    Log.d("count==", count.toString())
                    if (count == 0) {
                        postRemoveFromCart(position, cartAdapter.getItem(position).id!!)
                    } else {
                        cartAdapter.getItem(position).quantity = count
                        cartAdapter.getItem(position).available_qty =
                            cartAdapter.getItem(position).available_qty!! + 1
                        cartAdapter.notifyDataSetChanged()
                        getTotalCost()
//                        productViewModel.getCartObserable(accessToken = sharedPreferences.accessToken)
                        postUpdateAddToCart(
                            position,
                            cartAdapter.getItem(position).product_id!!,
                            cartAdapter.getItem(position).quantity!!,
                            cartAdapter.getItem(position).cart_id!!,
                            cartAdapter.getItem(position).barcode_id.toString()
                        )
                    }
                } else {
//                    productViewModel.getCartObserable(accessToken = sharedPreferences.accessToken)
                    postRemoveFromCart(position, cartAdapter.getItem(position).id!!)
                }
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (id == R.id.tv_remove) {
            if (Helper.getCurrentLocation(activity!!).equals(sharedPreferences.country)) {
                    if (Helper.isNetworkAvailable(activity!!)) {
                        progressbar.visibility = View.VISIBLE
//                        productViewModel.getCartObserable(accessToken = sharedPreferences.accessToken)
                        postRemoveFromCart(position, cartAdapter.getItem(position).id!!)
                    } else {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.message_no_internet_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()
            }
//                    listOfCart.removeAt(position)
//                    cartAdapter.notifyDataSetChanged()
        } else if (id == R.id.tv_add_to_wishlist) {
            if (Helper.getCurrentLocation(activity!!).equals(sharedPreferences.country)) {
                if (Helper.isNetworkAvailable(activity!!)) {
                    cartAdapter.getItem(position).is_wishlist=1
                    postAddToWishList(
                        position,
                        cartAdapter.getItem(position).product_id!!,
                        cartAdapter.getItem(position).cart_id!!
                    )
                    //cartAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.message_no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.please_select_country),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun initViews() {

    }

    override fun initControl() {

    }
    fun getCart() {
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE
        productViewModel.getCartList(sharedPreferences.accessToken,
            object : Callback<CartDataModel> {
                override fun onFailure(call: Call<CartDataModel>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<CartDataModel>,
                    response: Response<CartDataModel>
                ) {

                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.GONE
                        listOfCart.clear()

                        for (cart in response.body()!!.cart_list!!) {
                            cart.available_qty = cart.available_qty!! - cart.quantity!!
                            if (cart.quantity!! > cart.current_inventory!!){
                                cart.quantity = cart.current_inventory!!
                            }
                        }

                        listOfCart.addAll(response.body()!!.cart_list!!)

                        if (listOfCart.size == 0) {
                            try{
                                llParentLayout.visibility = View.GONE
                                tv_empty_message.visibility = View.VISIBLE
                                tv_empty_message.text = resources.getString(R.string.my_cart_list_empty)
                                BottomMenuHelper.showBadge(activity,homeActivity?.navigation,R.id.nav_cart,"0")
                            }catch (e:Exception){

                            }
                        } else {
                            llParentLayout.visibility = View.VISIBLE
                            tv_empty_message.visibility = View.GONE
                            var cartCount = 0
                            for (cart in listOfCart){
                                cartCount += cart.quantity!!
                            }

                            getTotalCost()

                        }
                        cartAdapter.notifyDataSetChanged()

                    } else {
                        if(response.code()==401){
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
                            prefs.referralCode = ""
                            try {
                                showToast(requireActivity(),"Unauthorized:You are already login with other device.")
                                activity?.let {
                                    val intent = Intent(it, LoginActivity::class.java)
                                    it.startActivity(intent)
                                    it.finish()
                                }
                            }catch (e:Exception){

                            }

                        }else{
                            llParentLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            tvReload.visibility = View.VISIBLE
                        }
                    }
                }

            })

    }
    fun postRemoveFromCart(position: Int, cartId: Int) {
        productViewModel.postRemoveCart(
            sharedPreferences.accessToken,
            cartId,
            object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        listOfCart.removeAt(position)
                        cartAdapter.notifyDataSetChanged()
                        if (listOfCart.size == 0) {
                            tv_empty_message.visibility = View.VISIBLE
                            llParentLayout.visibility = View.GONE
                            tv_empty_message.text = resources.getString(R.string.my_cart_list_empty)
                            getTotalCost()
                        } else {
                            tv_empty_message.visibility = View.GONE
                            llParentLayout.visibility = View.VISIBLE
                            //getCart()
                            getTotalCost()
                        }
                    } else {
                        Toast.makeText(context, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }
                }

            })

    }


    fun postAddSaveForLaterData(cartId: Int) {
        productViewModel.postAddSaverLaterData(
            sharedPreferences.accessToken,
            cartId,
            object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        //  getSaverLaterdata()
                    } else {
                        Toast.makeText(context, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }
                }

            })

    }


    //Event Notification Count
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SelectCartEventPojo) {

        if (event.isApiHit()!!) {
            getCart()
            EventBus.getDefault().postSticky(SelectCartEventPojo(false, false))

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


    /*  fun saveForList(){

          rvSaveForLater.layoutManager == LinearLayoutManager(context)
          rvSaveForLater.itemAnimator = DefaultItemAnimator()
          saveForLaterAdapter= SaveForLaterAdapter(activity!!,listOfSaveForLater)

          saveForLaterAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
              override fun onClick(id: Int, position: Int) {
                  if (id==R.id.tv_move_to_cart){
                      if(Helper.isNetworkAvailable(activity!!)){
                          postMoveToCart(position,saveForLaterAdapter.getItem(position).id!!)
                      }else{
                          Toast.makeText(activity!!,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
                      }
                  }else if (id==R.id.tv_remove){
                      if(Helper.isNetworkAvailable(activity!!)){
                          postRemoveSaverLater(position,saveForLaterAdapter.getItem(position).id!!)
                      }else{
                          Toast.makeText(activity!!,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
                      }
                  }

              }
          })
          rvSaveForLater.adapter=saveForLaterAdapter

          getSaverLaterdata()


      }


      fun getSaverLaterdata(){

          progressBar.visibility=View.VISIBLE
          productViewModel.getSaverLaterData(sharedPreferences.accessToken,object :Callback<SaverLaterDataModel>{
              override fun onFailure(call: Call<SaverLaterDataModel>, t: Throwable) {
                  progressBar.visibility=View.GONE
              }

              @SuppressLint("SetTextI18n")
              override fun onResponse(
                  call: Call<SaverLaterDataModel>,
                  response: Response<SaverLaterDataModel>
              ) {
                  progressBar.visibility=View.GONE

                  if (response.isSuccessful){

                      listOfSaveForLater.clear()
                      listOfSaveForLater.addAll(response.body()!!.savelater_list)
                      saveForLaterAdapter.notifyDataSetChanged()

                      if (response.body()!!.save_later_count==0){
                          tv_save_for_later.visibility=View.GONE
                      }else{
                          tv_save_for_later.visibility=View.VISIBLE
                          tv_save_for_later.text="Save For Later "+"("+response.body()!!.save_later_count+")"

                      }
                  }

              }

          })
      }

      fun postMoveToCart(position:Int,cartId:Int){
          productViewModel.postMoveToCart(sharedPreferences.accessToken,cartId,object :Callback<Any>{
              override fun onFailure(call: Call<Any>, t: Throwable) {

                  Toast.makeText(context,"item move to cart successfully",Toast.LENGTH_SHORT).show()

              }

              override fun onResponse(call: Call<Any>, response: Response<Any>) {
                  if (response.isSuccessful){

                      listOfSaveForLater.removeAt(position)
                      saveForLaterAdapter.notifyDataSetChanged()
                      getCart()
                      Toast.makeText(context,"item move to cart successfully",Toast.LENGTH_SHORT).show()
                  }
              }

          })
      }


      fun postRemoveSaverLater(position:Int,cartId:Int){
          productViewModel.postRemoveSaverLaterData(sharedPreferences.accessToken,cartId,object :Callback<Any>{
              override fun onFailure(call: Call<Any>, t: Throwable) {


              }

              @SuppressLint("SetTextI18n")
              override fun onResponse(call: Call<Any>, response: Response<Any>) {
                  if (response.isSuccessful){

                      listOfSaveForLater.removeAt(position)
                      saveForLaterAdapter.notifyDataSetChanged()

                      if (listOfSaveForLater.size==0){
                          tv_save_for_later.visibility=View.GONE
                      }else{
                          tv_save_for_later.visibility=View.VISIBLE

                          tv_save_for_later.text="Save For Later"+listOfSaveForLater.size

                      }
                  }
              }

          })
      }
  */


    fun postAddToWishList(position: Int, productId: Int, cartId: Int) {
        productViewModel.postAddToWishList(
            sharedPreferences.accessToken,
            productId, cartId,
            object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {

                        //listOfCart.removeAt(position)
                        cartAdapter.notifyDataSetChanged()

                        if (listOfCart.size == 0) {
                            tv_empty_message.visibility = View.VISIBLE
                            llParentLayout.visibility = View.GONE
                            tv_empty_message.text = resources.getString(R.string.my_cart_list_empty)


                        } else {
                            tv_empty_message.visibility = View.GONE
                            llParentLayout.visibility = View.VISIBLE
                            //getCart()
                            getTotalCost()


                        }
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.product_added_to_wishlist_successfully),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            })

    }


    fun postUpdateAddToCart(position: Int, productId: Int, quantity: Int, cartId: Int,barcodeId:String) {
        productViewModel.postUpdateAddToCart(
            sharedPreferences.accessToken,
            productId,
            sharedPreferences.storeId,
            quantity,
            cartId,
            barcodeId,
            object :
                Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    progressbar.visibility = View.GONE
                }
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        progressbar.visibility = View.GONE
                        val jsonObject = Gson().toJson(response.body())
                        Log.d("response==", jsonObject)
                        Log.d("response==", "c=" + count)
                        try {
                            val jsonObject1 = JSONObject(jsonObject)
                            // listOfCart[position].price
                            val cartId = jsonObject1.getInt("cart_id")
                            //  productListGridAdapter!!.getItem(position).cart_id = cartId
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        //getCart()
                        getTotalCost()

                    }
                }

            })
    }


    @SuppressLint("SetTextI18n")
    private fun getTotalCost() {

        total = 0.0
        product_id=""
        barcodeId = ""
        val list =listOfCart.distinctBy { it.barcode_id }
        var productids = ArrayList<Int>()
        for (cart in list) {
            Log.e("checkproducts",cart.product_id.toString())
            if(!productids.contains(cart.product_id))
            {
                productids.add(cart.product_id!!)
                if (quantity == "") {
                    quantity = cart.quantity.toString()
                } else {
                    quantity += "," + cart.quantity
                }
                if (product_id.equals("")) {
                    Log.e("checkproducts2",product_id+" == "+cart.product_id.toString())
                    product_id = cart.product_id.toString()
                } else {
                    Log.e("checkproducts1",product_id+" == "+cart.product_id.toString())
                    product_id += "," + cart.product_id
                }
                if (barcodeId.equals("")) {
                    barcodeId = cart.barcode_id.toString()
                } else {
                    barcodeId += "," + cart.barcode_id
                }
//                total += cart.price!!.toDouble() * cart.quantity!!
            }
        }
        Log.e("getTotalCost",product_id+" == "+barcodeId+" == "+total)
        Log.e("Fsdfds", listOfCart.size.toString())
        var cartCount = 0
        for (cart in list){
            cartCount += cart.quantity!!
        }
        BottomMenuHelper.showBadge(activity,homeActivity?.navigation,R.id.nav_cart,listOfCart.size.toString())
        productViewModel.carDataList.value = cartCount
        if (!list.isEmpty()) {
            tv_total!!.text = total.toString() + " " + list[0].currency
        }
    }
}

