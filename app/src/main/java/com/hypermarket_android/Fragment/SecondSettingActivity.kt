package com.hypermarket_android.Fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.changepassword.ChangePasswordActivity
import com.hypermarket_android.ui.feedback.FeedbackActivity
import kotlinx.android.synthetic.main.activity_second_setting.*

class SecondSettingActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_setting)
        initViews()
        initControl()
    }

    override fun initViews() {

    }

    override fun initControl() {

        ll_legal.setOnClickListener(this)
        ll_my_term_condition.setOnClickListener(this)
        ll_neeed_help.setOnClickListener(this)
        ll_feedback.setOnClickListener(this)
        ll_change.setOnClickListener(this)
        ll_notification.setOnClickListener(this)
        img_second_settings.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.ll_legal -> {
               startActivity(Intent(this, WebActivity::class.java).apply {
                   putExtra("titlename",resources.getString(R.string.privacy_policy))
                   if (sharedPreference.language == "ar"){
                       putExtra("url","https://yasmart.azurewebsites.net/privacy-policy-arr")
                   }else{
                       putExtra("url","https://yasmart.azurewebsites.net/privacy-policy")
                   }

               })
           } R.id.ll_my_term_condition -> {
           startActivity(Intent(this, WebActivity::class.java).apply {
               putExtra("titlename",resources.getString(R.string.terms_conditions))
               if (sharedPreference.language == "ar"){
                   putExtra("url","https://yasmart.azurewebsites.net/term-condition-arr")
               }else{
                   putExtra("url","https://yasmart.azurewebsites.net/term-condition")
               }

           })
           //http://3.6.74.92/term-condition
           //http://3.6.74.92/privacy-policyhttp://3.6.74.92/privacy-policy
           } R.id.ll_neeed_help -> {
           startActivity(Intent(this, WebActivity::class.java).apply {
               putExtra("titlename",resources.getString(R.string.help))
               if (sharedPreference.language == "ar"){
                   putExtra("url","https://yasmart.azurewebsites.net/help-arr")
               }else{
                   putExtra("url","https://yasmart.azurewebsites.net/help")
               }

           })

           } R.id.ll_feedback -> {
           startActivity(Intent(this, FeedbackActivity::class.java))

           } R.id.ll_change -> {
           startActivity(Intent(this, ChangePasswordActivity::class.java))

           } R.id.ll_notification -> {
           }
           R.id.img_second_settings -> {
               onBackPressed()
           }
       }
    }
}