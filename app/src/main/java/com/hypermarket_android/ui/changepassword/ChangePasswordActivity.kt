package com.hypermarket_android.ui.changepassword

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.LoginManager
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.feedback.FeedbackViewModel
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.util.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.btn_submit
import kotlinx.android.synthetic.main.activity_reset_password.img_back_forgot
import kotlinx.android.synthetic.main.dialoge_password_change.*
import kotlinx.android.synthetic.main.dialogue_sign_out.*
import kotlinx.android.synthetic.main.dialogue_sign_out.btnOkSignout

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {
    //private lateinit var mviewModel: ChangePasswordViewModel
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        changePasswordViewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        initControl()
        initViews()
        chnagePasswordObservable()
    }

    override fun initViews() {
       // mviewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
    }


    private fun chnagePasswordObservable() {
        changePasswordViewModel.changePasswordResponse.observe(this, Observer {
            showToast(it.message)
            dialogChangePassowrd()


        })
        changePasswordViewModel.errorChangePassword.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
//            showToast(it.message?:"")
            ErrorUtil.handlerGeneralError(this, it)

        })

        changePasswordViewModel.mProgessChangePassword.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }



    override fun initControl() {
        btn_chnage_password_submit.setOnClickListener(this)
        img_back_change.setOnClickListener(this)
        /*img_newpass_visible.setOnClickListener {
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
        }*/

    }



    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_chnage_password_submit -> {
                if (isValid()){
                    if (Helper.isNetworkAvailable(this!!)) {
                        changePasswordViewModel.hitChangePasswordApi(
                            sharedPreference.accessToken,
                           et_current_pass.text.toString().trim(),
                           et_new_pass.text.toString().trim()
                        )
                    }
                    else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.message_no_internet_connection),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                   // mviewModel.getResetPassword(sharedPreference.userId,et_resetnew_pass.text.toString())
                }

            }
            R.id.img_back_change ->{
               onBackPressed()
            }

        }
        }


    private fun nextScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun isValid() : Boolean{


        if(et_current_pass.text?.isEmpty()!!){
            Toast.makeText(this,getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show()
            et_current_pass.requestFocus()
            return false
        }

        if(et_new_pass.text?.isEmpty()!!){
            Toast.makeText(this,getString(R.string.re_enter_new_password), Toast.LENGTH_SHORT).show()
            et_new_pass.requestFocus()

            return false
        }

       if (!et_confirm_pass.text.toString().contains(et_new_pass.text.toString())) {
            et_confirm_pass.setError( resources.getString(R.string.password_and_confirm_password_must_be_same),null)
            et_confirm_pass.requestFocus(et_confirm_pass.length())
            return false
        }

        return true
    }

    private fun dialogChangePassowrd() {
        var dialog = this.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialoge_password_change)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)

        dialog.show()
        dialog.btnOkchange.setOnClickListener {

            dialog.dismiss()
            finish()
        }


        val window = dialog.getWindow()
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

}