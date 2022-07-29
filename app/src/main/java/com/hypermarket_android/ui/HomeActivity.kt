package com.hypermarket_android.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.hypermarket_android.Fragment.*
import com.hypermarket_android.R
import com.hypermarket_android.customView.CustomViewPager
import com.hypermarket_android.eventPojos.HomePageReloadEventPojo
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.eventPojos.SelectCategoryEventPojo
import com.hypermarket_android.eventPojos.SelectDealsEventPojo
import com.hypermarket_android.util.BottomMenuHelper
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val BACK_TIME_DELAY = 2000
    private var back_pressed: Long = 0
    var fragment: Fragment? = null
    //private var viewPager: CustomViewPager? = null
    public var navigation: BottomNavigationView? = null
   // internal var adapter = ViewPagerAdapter(supportFragmentManager)
    private var storeId: Int? = null
    lateinit var productViewModel: ProductViewModel
    lateinit var sharedPreference: SharedPreferenceUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if(intent.getIntExtra("storeId", 0) != null){
            storeId = intent.getIntExtra("storeId", 0)
        }
        sharedPreference = SharedPreferenceUtil.getInstance(this)
        Helper.getCurrentLocation(this)
        //viewPager = findViewById(R.id.viewpager)
        //viewPager!!.setOffscreenPageLimit(4)
//        toolbar!!.setTitle("")
//        setSupportActionBar(toolbar)

        navigation = findViewById(R.id.navigation)
        disableShiftMode(navigation!!)
        onNavigationItemSelected(navigation!!.getMenu().findItem(R.id.nav_home))
        navigation!!.setOnNavigationItemSelectedListener(this)
        //  navigation!!.setOnNavigationItemReselectedListener(this)
        navigation!!.menu.getItem(0).isChecked = true

        //Fetching SetupWithViewpager
        //setupViewPager(viewPager!!)
        //viewPager!!.currentItem = 0
        //  BottomMenuHelper.showBadge(this, navigation, R.id.nav_cart, "1")
        //val bundle = Bundle()
       // val teamArrayList: ArrayList<Team> = this.teamInfoModule.getAllTeamData()
        val homeFragment = HomeFragment()

       // bundle.putParcelableArrayList("teamData", teamArrayList)
       // homeFragment.arguments = bundle
        if(savedInstanceState == null) { // initial transaction should be wrapped like this
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, homeFragment)
                .commitAllowingStateLoss()
        }
        backGroundColor()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }

     fun setFragmentView(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack("cat")
        transaction.commit()
    }
    fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@HomeActivity.supportFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }
    fun initViews() {
        sharedPreference = SharedPreferenceUtil.getInstance(this)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getCartObserable(accessToken = sharedPreference.accessToken)
        productViewModel.carDataList.observe(this, androidx.lifecycle.Observer {
            if (it != 0) {
                Log.e("CartCountData",it.toString())
                BottomMenuHelper.showBadge(
                    this,
                    navigation,
                    R.id.nav_cart,
                    it.toString()
                )
            }
            else{
                BottomMenuHelper.showBadge(this,
                    navigation,
                    R.id.nav_cart,
                    "0")
            }


            //  navigation!!.getOrCreateBadge(R.id.nav_cart).number = it.cart_list!!.size
        })
        if(sharedPreference.language != null){
            if(sharedPreference.language == "en"){
                setLocale("en")
            } else if(sharedPreference.language == "ar"){
                setLocale("ar")
            }else{
                setLocale("en")
            }
        }

    }

    /*private fun setupViewPager(viewPager: ViewPager) {
        adapter.addFrag(HomeFragment(), resources.getString(R.string.home))
        adapter.addFrag(CategoryFragment(), resources.getString(R.string.category))
        adapter.addFrag(DealsFragment(), resources.getString(R.string.deals))
        adapter.addFrag(CartFragment(), resources.getString(R.string.cart))
        adapter.addFrag(SettingFragment(), resources.getString(R.string.setting))

        viewPager.adapter = adapter
    }

     */

    /*class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        // 2
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        // 3
        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    } */


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if(getVisibleFragment() != null && getVisibleFragment() != HomeFragment()){
                    setFragmentView(HomeFragment())
                }
                return true
            }
            R.id.nav_category -> {
                if(getVisibleFragment() != null && getVisibleFragment() != CategoryFragment()){
                    setFragmentView(CategoryFragment())
                }
                return true
            }
            R.id.nav_deals -> {
                if(getVisibleFragment() != null && getVisibleFragment() != DealsFragment()){
                    setFragmentView(DealsFragment())
                }
                return true
            }
            R.id.nav_cart -> {
                if(getVisibleFragment() != null && getVisibleFragment() != CartFragment()){
                    setFragmentView(CartFragment())
                }
                return true
            }
            R.id.nav_setting -> {
                if(getVisibleFragment() != null && getVisibleFragment() != SettingFragment()){
                    setFragmentView(SettingFragment())
                }
                return true
            }
        }
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.setAccessible(true)
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.setAccessible(false)
            for (i in 0 until menuView.getChildCount()) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked())
            }
        } catch (e: NoSuchFieldException) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (e: IllegalAccessException) {
        }

    }
    override fun onBackPressed() {
        if (back_pressed + BACK_TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(
                baseContext, resources.getString(R.string.message_press_once_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
        }
        back_pressed = System.currentTimeMillis()
    }
    //Event SelectCategoryEventPojo
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SelectCategoryEventPojo) {
        if (event.isSelectCategory()!!) {
            Log.d("isEvent==", "isEvent")
            if(getVisibleFragment() != null && getVisibleFragment() != CategoryFragment()){
                setFragmentView(CategoryFragment())
            }
            navigation!!.getMenu().getItem(1).setChecked(true)
            EventBus.getDefault().postSticky(SelectCategoryEventPojo(false))

        }
    }
    //Event SelectDealsEventPojo
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SelectDealsEventPojo) {
        if (event.isDeals()!!) {
            Log.d("isEvent==", "isEvent")
            if(getVisibleFragment() != null && getVisibleFragment() != DealsFragment()){
                setFragmentView(DealsFragment())
            }
            navigation!!.getMenu().getItem(2).setChecked(true)
            EventBus.getDefault().postSticky(SelectDealsEventPojo(false))
        }
    }
    //Event SelectCartEventPojo
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SelectCartEventPojo) {
        if (event.isSelectCart()!!) {
            Log.d("isEvent==", "isEvent")
            if(getVisibleFragment() != null && getVisibleFragment() != CartFragment()){
                setFragmentView(CartFragment())
            }
            navigation!!.getMenu().getItem(3).setChecked(true)
            EventBus.getDefault().postSticky(SelectCartEventPojo(false, false))

        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomePageReloadEventPojo) {
        if (event.isReloadPage()!!) {
            reload()
            EventBus.getDefault().postSticky(HomePageReloadEventPojo(false))

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
    fun reload() {

        val intent = getIntent()
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        startActivity(intent
//        )
//        val intent = getIntent()
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    override fun onResume() {
        super.onResume()
        initViews()
    }
    override fun onPause() {
        super.onPause()
        if(sharedPreference.language != null){
            if(sharedPreference.language == "en"){
                setLocale("en")
            } else if(sharedPreference.language == "ar"){
                setLocale("ar")
            }else{
                setLocale("en")
            }
        }

    }
    override fun onRestart() {
        super.onRestart()
        if(sharedPreference.language != null){
            if(sharedPreference.language == "en"){
                setLocale("en")
            } else if(sharedPreference.language == "ar"){
                setLocale("ar")
            }else{
                setLocale("en")
            }
        }
    }
    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}