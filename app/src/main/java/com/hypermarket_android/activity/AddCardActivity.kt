package com.hypermarket_android.activity

import android.os.Bundle
import android.util.Log
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.showToast
import kotlinx.android.synthetic.main.activity_add_card.*


class AddCardActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initViews()
        initControl()
    }

    override fun initViews() {
        btn_back.setOnClickListener { this.finish() }
    }

    override fun initControl() {
        add_Card.setOnClickListener {
            when {
                cardno.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_the_card_number))
                }
                cardno.text.toString().replace(" ","").length != 16 -> {
                 Log.e("testcardno",cardno.text.toString().length.toString()+" == "+cardno.text)
                    showToast(resources.getString(R.string.please_enter_the_valid_card_number))
                }
                cardholdername.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_card_holder_name))
                }
                cvv.text.toString().length != 3 -> {
                    showToast(resources.getString(R.string.please_enter_the_valid_cvv_number))
                }
                months.text.toString().length != 2 -> {
                    showToast(resources.getString(R.string.please_enter_the_valid_expiry_date))
                }
                year.text.toString().length != 4 -> {
                    showToast(resources.getString(R.string.please_enter_valid_year))
                }
                else -> {

                    val intent = intent
                    intent.putExtra("cardNo",cardno.text.toString() )
                    intent.putExtra("cardholder", cardholdername.text.toString())
                    intent.putExtra("cvv", cvv.text.toString())
                    intent.putExtra("month", months.text.toString())
                    intent.putExtra("year", year.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        }

    }
}