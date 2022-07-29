package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import kotlinx.android.synthetic.main.activity_support.*

class SupportActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        iv_actionbar_back.setOnClickListener { onBackPressed() }
        btn_send_us_message.setOnClickListener {
        startActivity(Intent(this, SendMessageActivity::class.java ))

        }
    }


    override fun initViews() {

    }

    override fun initControl() {
    }


}