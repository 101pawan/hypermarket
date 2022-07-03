package com.hypermarket_android.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.CouponListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.CouponListResponse
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.viewModel.CouponListViewModel
import kotlinx.android.synthetic.main.activity_my_coupons.*
import kotlinx.android.synthetic.main.activity_my_coupons.btn_back

class MyCouponsActivity : BaseActivity() {
    private lateinit var couponListViewModel: CouponListViewModel

    companion object {
        var product_id = ""
    }

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
            product_id
        )
    }
    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
        couponListViewModel.couponListResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            if (it.response.isEmpty()){
                couponlist.visibility = View.GONE
                no_coupon.visibility = View.VISIBLE
            }else{
                couponlist.visibility = View.VISIBLE
                no_coupon.visibility = View.GONE
            }
            couponlist.layoutManager = LinearLayoutManager(this)
            couponlist.adapter =
                CouponListAdapter(this, it.response, object : CouponListAdapter.onClickListener {
                    override fun onClick(couponModel: CouponListResponse.CouponModel) {
                        SelectPaymentModeActivity.couponModel = couponModel
                        val intent = intent
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                })

        })
        couponListViewModel.errorOrderList.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

        })

    }
}