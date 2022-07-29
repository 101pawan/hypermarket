package com.hypermarket_android.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.facebook.login.LoginManager
import com.google.android.material.tabs.TabLayout
import com.hypermarket_android.Fragment.*
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home_delevery.*
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.dialogue_sign_out.*

class HomeDeleveryActivity : BaseActivity() {
    lateinit var vpOrder: ViewPager
    internal var adapter = MyOrderActivity.ViewPagerAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_delevery)
        initViews()
        initControl()
        statusBackGroundColor()
    }

    override fun initViews() {
        vpOrder=findViewById(R.id.vp_order)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(vpOrder)
        vpOrder.offscreenPageLimit = 2
        setupViewPager(vpOrder)
    }

    override fun initControl() {
        iv_actionbar_logout.setOnClickListener(View.OnClickListener {
           dialogSignout()
        })
    }
    private fun dialogSignout() {
        var dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialogue_sign_out)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.btnOkSignout.setOnClickListener {
            LoginManager.getInstance().logOut()
            if (sharedPreference.loginguest) {
                sharedPreference.loginguest = false
            }
            if (sharedPreference.loginStatusSocial) {
                sharedPreference.accessToken = ""
                sharedPreference.loginStatusSocial = false
                sharedPreference.full_name = ""
                sharedPreference.email = ""
                sharedPreference.profile_image = ""
                sharedPreference.isDeliveryBoy = false
                sharedPreference.deliveryBoy = ""
            }
            if (sharedPreference.loginStatus) {
                sharedPreference.accessToken = ""
                sharedPreference.loginStatus = false
                sharedPreference.full_name = ""
                sharedPreference.email = ""
                sharedPreference.mobile_no = ""
                sharedPreference.profile_image = ""
                sharedPreference.isDeliveryBoy = false
                sharedPreference.deliveryBoy = ""
            }
            //activity?.let {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            //}
            dialog.dismiss()
        }
        dialog.btnCancelSignout.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupViewPager(viewPager: ViewPager) {
        adapter.addFrag(DeleveryOngoingFragment(11), resources.getString(R.string.incoming_orders))
        adapter.addFrag(DeleveryOngoingFragment(12), resources.getString(R.string.ongoing_orders))
        adapter.addFrag(OutOfDeliveryFragment(),resources.getString(R.string.out_delivery))
        adapter.addFrag(DeleveryPastFragment(), resources.getString(R.string.past_orders))
        adapter.addFrag(DeleveryCancelFragment(), resources.getString(R.string.cancelled_orders))
        adapter.addFrag(ReturnOrderFragment(), resources.getString(R.string.return_orders))
        viewPager.adapter = adapter
    }

    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
}