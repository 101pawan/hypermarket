package com.hypermarket_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hypermarket_android.Fragment.*
import com.hypermarket_android.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_my_order.*
import java.util.ArrayList

class MyOrderActivity : AppCompatActivity() {

    lateinit var vpOrder:ViewPager

    internal var adapter = ViewPagerAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

         vpOrder=findViewById(R.id.vp_order)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(vpOrder)
        vpOrder.setOffscreenPageLimit(2)
        setupViewPager(vpOrder)
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

    private fun setupViewPager(viewPager: ViewPager) {
//        adapter.addFrag(OngoingOrdersFragment(), resources.getString(R.string.ongoing_orders))
        adapter.addFrag(OngoingOrderListFragment(), resources.getString(R.string.ongoing_orders))
//        adapter.addFrag(PastOrdersFragment(), resources.getString(R.string.past_orders))
        adapter.addFrag(NewPastOrderFragment(), resources.getString(R.string.past_orders))
        adapter.addFrag(CancelledOrdersFragment(), resources.getString(R.string.cancelled_orders))
        viewPager.adapter = adapter
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
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
    }
}
