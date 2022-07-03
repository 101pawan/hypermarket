package com.hypermarket_android.ui.forgotPassword

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.verification.VerificationActivity
import com.hypermarket_android.util.ErrorUtils
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.showToast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*


class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mviewModel: ForgotViewModel
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        progressBar = findViewById(R.id.progressbar)
        initControl()
        initViews()
        myObserver()
        statusBackGroundColor()
    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(ForgotViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)

    }

    private fun myObserver() {
        mviewModel.mResponseForgot.observe(this, Observer {
            progressBar!!.visibility = View.GONE
            if (it != null) {
                showToast(it.message!!)
                if (it.user_data!!.is_register == 1) {
                    sharedPreference.userId = it.user_data.id.toString()
                    sharedPreference.forgot = it.user_data.is_register.toString()
                    startActivity(Intent(this, VerificationActivity()::class.java))
                    finish()

                } else if (it.user_data!!.is_register == 0 && it.user_data!!.otp_verified == 1) {
                    showToast(it.message!!)
                    sharedPreference.userId = it.user_data.id.toString()
                    sharedPreference.full_name = it.user_data.name.toString()
                    sharedPreference.email = it.user_data.email.toString()
                    sharedPreference.mobile_no = it.user_data.mobile_number.toString()
                    sharedPreference.forgot = it.user_data.is_register.toString()
                    startActivity(Intent(this, VerificationActivity()::class.java))
                }
            }
        })

        mviewModel.mError.observe(this, Observer {
            progressBar!!.visibility = View.GONE
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    override fun initControl() {
        img_back_forgot.setOnClickListener(this)
        btn_forgot_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back_forgot -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.btn_forgot_submit -> {
                if (progressBar!!.visibility == View.GONE) {
                    if (isValidField()) {
                        Helper.hideSoftKeyboard(this)
                        if (Helper.isNetworkAvailable(this)) {
                            progressBar!!.visibility = View.VISIBLE
                            var num = etEmailForgot.text.toString()
                            if (num.contains("[0-9]".toRegex())){
                                if (num[0] == '0'){
                                    num = num.drop(1)
                                }
                            }
                            mviewModel.getForgetPassword(num, baseContext)
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

            }
        }
    }

    private fun isValidField(): Boolean {
        if (etEmailForgot.text?.trim().toString().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_your_registered_email_id_and_mobile_no))
            return false
        }
        return true
    }

}