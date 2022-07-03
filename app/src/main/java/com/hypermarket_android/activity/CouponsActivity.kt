package com.hypermarket_android.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.CouponAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.viewModel.CouponListViewModel
import kotlinx.android.synthetic.main.activity_my_coupons.*
import kotlinx.android.synthetic.main.activity_my_coupons.btn_back

class CouponsActivity : BaseActivity() {
    private lateinit var couponListViewModel: CouponListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_coupons)
        initViews()
        initControl()
    }
    override fun initViews() {
        couponListViewModel = ViewModelProvider(this).get(CouponListViewModel::class.java)
        ProgressDialogUtils.getInstance().showProgress(this, false)
        couponListViewModel.getCoupons(
            sharedPreference.accessToken,
            sharedPreference.storeId.toString(),
            ""
        )
    }

    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
        couponListViewModel.couponListResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            couponlist.layoutManager = LinearLayoutManager(this)
            if(it.response.size == 0){
                no_coupon.visibility = View.VISIBLE
            }else{
                no_coupon.visibility = View.GONE
            }
            couponlist.adapter = CouponAdapter(this, it.response)
        })
        couponListViewModel.errorOrderList.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
        })

    }
}