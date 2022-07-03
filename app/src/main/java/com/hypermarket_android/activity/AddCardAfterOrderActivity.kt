package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.AddOrderViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_add_card.*
import java.text.SimpleDateFormat
import java.util.*


class AddCardAfterOrderActivity : BaseActivity() {
    lateinit var addOrderViewModel: AddOrderViewModel

    companion object {
        lateinit var order_id: String
        lateinit var totalAmount: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
       /* Log.e("fsfsd", order_id)*/
        initViews()
        initControl()
    }

    private val space = ' '

    override fun initViews() {

        months.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var working = s.toString()
                var isValid = true
                if (working.length == 2 && before === 0) {
                    if (working.toInt() < 1 || working.toInt() > 12) {
                        isValid = false
                    } else {
//                        working += "/"
                        months.setText(working)
                        year.requestFocus()
//                        months.setSelection(working.length)
                    }
                }
//                else if (working.length == 7 && before === 0) {
//                    val enteredYear = working.substring(3)
//                    val currentYear = Calendar.getInstance()[Calendar.YEAR]
//                    if (enteredYear.toInt() < currentYear) {
//                        isValid = false
//                    }
//                } else if (working.length != 7) {
//                    isValid = false
//                }

                if (!isValid) {
                    months.error = resources.getString(R.string.enter_valid_date)+": MM/YYYY"
                } else {
                    months.setError(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })



        btn_back.setOnClickListener { onBackPressed() }

        addOrderViewModel = ViewModelProvider(this).get(AddOrderViewModel::class.java)
        observers()
    }

    override fun initControl() {
        add_Card.setOnClickListener {
            when {
                cardno.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_the_card_number))
                }
                /*cardno.text.toString().length != 16 -> {
                    showToast("Please Enter Correct Card No")
                }*/
                cardholdername.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_card_holder_name))
                }
                cvv.text.toString().length != 3 -> {
                    showToast(resources.getString(R.string.please_enter_the_valid_cvv_number))
                }
                months.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_the_expiry_date))
                }
               /* months.text.toString().length != 2 -> {
                    showToast("Please Enter Correct Expiry Month")
                }
                year.text.toString().length != 4 -> {
                    showToast("Please Enter Correct Expiry year")
                }*/
                else -> {
                    /*      val intent = intent
                          intent.putExtra("cardNo",cardno.text.toString() )
                          intent.putExtra("cardholder", cardholdername.text.toString())
                          intent.putExtra("cvv", cvv.text.toString())
                          intent.putExtra("month", month.text.toString())
                          intent.putExtra("year", year.text.toString())
                          setResult(RESULT_OK, intent)
                          finish()*/
                    var cardNo_s =  cardno.text.toString()
                    var month_s =  SimpleDateFormat("MM").format(SimpleDateFormat("MM").parse(months.text.toString()))
                    var year_s =  SimpleDateFormat("yyyy").format(SimpleDateFormat("yyyy").parse(year.text.toString()))
                    var cvv_s =  cvv.text.toString()
                   val backIntent = Intent()
                    backIntent.putExtra("cardNo_s", cardNo_s.replace("\\s".toRegex(), ""))
                    backIntent.putExtra("month_s", month_s)
                    backIntent.putExtra("year_s", year_s)
                    backIntent.putExtra("cvv_s", cvv_s)
                    setResult(RESULT_OK, backIntent)
                    finish()
                }
            }

        }
    }

    fun observers() {
        addOrderViewModel.errorOrderResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })
        addOrderViewModel.paymentResponse.observe(this, Observer {
            Log.e("testordersummury","addcrrdActivity")
            startActivity(Intent(this, OrderSummaryActivity::class.java))
            finishAffinity()
        })

    }
    override fun onBackPressed() {
        val backIntent = Intent()
        setResult(RESULT_CANCELED, backIntent)
        finish()
       super.onBackPressed()
    }
    data class PaymentOrderModel(
        @SerializedName("OrderID")
        val OrderID: String,
        @SerializedName("Amount")
        val Amount: String,
        @SerializedName("CardNumber")
        val CardNumber: String,
        @SerializedName("ExpiryMonth")
        val ExpiryMonth: String,
        @SerializedName("ExpiryYear")
        val ExpiryYear: String,
        @SerializedName("CVV")
        val CVV: String,
        @SerializedName("UserID")
        val UserID: String
    )
}