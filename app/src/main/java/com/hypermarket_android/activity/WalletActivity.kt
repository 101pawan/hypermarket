package com.hypermarket_android.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.WalletViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.activity_wallet.img_settings_profile
import kotlinx.android.synthetic.main.activity_wallet.tv_settings_name

class WalletActivity:  BaseActivity(){
    val prefs: SharedPreferenceUtil by lazy {
        SharedPreferenceUtil.getInstance(this)
    }
    private lateinit var walletViewModel: WalletViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        walletViewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        walletViewModel.getWalletData(sharedPreference.accessToken,sharedPreference.userId)
        updateView()
    }

    private fun updateView() {
        walletViewModel.amount.observe(this, Observer {
            tv_available_balance.text = it
            balance.text = it
        })
        iv_actionbar_back.setOnClickListener {
            onBackPressed()
        }
        if (prefs.loginguest == true) {
            img_settings_profile.setImageResource(R.drawable.placeholder_img)
        } else if (prefs.loginStatusSocial == true) {
            if (prefs.profile_image != null && !prefs.profile_image.isEmpty()) {
                Picasso.get()
                    .load(prefs.profile_image)
                    .placeholder(R.drawable.profile_dummy_img)
                    .fit()
                    .into(img_settings_profile)
            } else {
                img_settings_profile.setImageResource(R.drawable.placeholder_img)
            }
            tv_settings_name.text = prefs.full_name
        } else {
            if (prefs.profile_image != null && !prefs.profile_image.isEmpty()) {
                Picasso.get()
                    .load(prefs.profile_image)
                    .placeholder(R.drawable.profile_dummy_img)
                    .fit()
                    .into(img_settings_profile)
                tv_settings_name.text = prefs.full_name
            } else {
                img_settings_profile.setImageResource(R.drawable.placeholder_img)
                tv_settings_name.text = prefs.full_name
            }
        }
    }

    override fun initViews() {

    }

    override fun initControl() {
    }

}