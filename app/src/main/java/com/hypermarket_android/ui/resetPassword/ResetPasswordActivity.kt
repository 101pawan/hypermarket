package com.hypermarket_android.ui.resetPassword

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.hypermarket_android.ui.forgotPassword.ForgotPasswordActivity
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.util.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.img_back_forgot

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mviewModel: ResetPasswordViewModel
     var progressBar:ProgressBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        progressBar=findViewById(R.id.progressbar)
        initControl()
        initViews()
        myObserver()
        statusBackGroundColor()
    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)
    }


    private fun myObserver() {
        mviewModel.mResponseResetPassword.observe(this, Observer {
            progressBar!!.visibility=View.GONE
            if (it.message!=null) {
                showToast(it.message!!)
                nextScreen()
            }
        })

        mviewModel.mError.observe(this, Observer {
            progressBar!!.visibility=View.GONE
            ErrorUtils.handlerGeneralError(this,it)
        })
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.sattus_back)
    }
    override fun initControl() {
        btn_submit.setOnClickListener(this)
        img_back_forgot.setOnClickListener(this)
        img_newpass_visible.setOnClickListener {
            if( et_resetnew_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                et_resetnew_pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                img_newpass_visible.setImageResource(R.drawable.eye_close)
            } else{
                et_resetnew_pass.transformationMethod = PasswordTransformationMethod.getInstance()
                img_newpass_visible.setImageResource(R.drawable.eye_open)
            }
        }

        img_re_enter_pass_visible.setOnClickListener {
            if( et_re_enter_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                et_re_enter_pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                img_re_enter_pass_visible.setImageResource(R.drawable.eye_close)
            } else{
                et_re_enter_pass.transformationMethod = PasswordTransformationMethod.getInstance()
                img_re_enter_pass_visible.setImageResource(R.drawable.eye_open)
            }
        }

    }



    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_submit -> {
                if (progressBar!!.visibility==View.GONE) {
                    if (isValidField()) {
                        Helper.hideSoftKeyboard(this)
                        if (Helper.isNetworkAvailable(this)) {
                            progressBar!!.visibility = View.VISIBLE
                            mviewModel.getResetPassword(
                                sharedPreference.userId,
                                et_resetnew_pass.text.toString()
                            )
                        }else{
                            Toast.makeText(baseContext,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }
            R.id.img_back_forgot ->{
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
                finish()
            }

        }
        }

    private fun isValidField(): Boolean {
        if (et_resetnew_pass.text?.trim().toString().isEmpty()){
            showToast(resources.getString(R.string.please_enter_password))
            return false
        }

        if (!et_resetnew_pass.text.toString().isValidPassword) {
            showToast(resources.getString(R.string.password_should_have_8_character))
            et_resetnew_pass.requestFocus(et_resetnew_pass.length())
            return false
        }

        if (et_re_enter_pass.text?.trim().toString().isEmpty()){
            showToast(resources.getString(R.string.re_enter_new_password))
            return false
        }
        if (!et_re_enter_pass.isPasswordMatch(et_resetnew_pass.text.toString(), resources.getString(R.string.password_and_confirm_password_must_be_same))) {
            return false
        }

        return true
    }

    private fun nextScreen() {
        val intent = Intent(baseContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}