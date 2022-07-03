package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.annotations.SerializedName
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.paymentgateway.PaymentActivity
import com.hypermarket_android.util.AESUtils
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.AddOrderViewModel
import kotlinx.android.synthetic.main.activity_payment_summary.*
import kotlinx.android.synthetic.main.activity_payment_summary.btn_back
import kotlinx.android.synthetic.main.activity_payment_summary.cod_layout
import kotlinx.android.synthetic.main.activity_payment_summary.discountAmount
import kotlinx.android.synthetic.main.activity_payment_summary.discount_layout
import kotlinx.android.synthetic.main.activity_payment_summary.total_amount
import kotlinx.android.synthetic.main.activity_select_payment.*
import mumbai.dev.sdkdubai.*
import kotlin.math.roundToInt


class PaymentSummaryActivity : BaseActivity(), CustomModel.OnCustomStateListener {

    private var cardNo_s: String? = ""
    private var month_s: String? = ""
    private var year_s: String? = ""
    private var cvv_s: String? = ""
    private var order_id: String? = ""
    lateinit var addOrderViewModel: AddOrderViewModel
    private val shippingFee = 10.00
    private val cashOnDelivery = 10.00

    companion object {
        var totalAmount = ""
        var taxAmount = ""
        var walletAmount = 0.0
        var total_item_price = 0.0
        var totalAmounts = ""
        var address_id = ""
        var coupon_id = ""
        var payment_mode = ""
        var expected_delivery = ""
        var deliveryDate = ""
        var walletInclude = 0
        var isOnline: Boolean? = false
        var charge_delivery = ""
        var product_id = ""
        var discountedAmount = 0.0
        var discountInclude = 0
        var barCodeId = ""
        var productColor = ""
        var redeemCode = ""
        var quantity = ""
        var totalPrice = ""
        var totalColorId = ""
        var cardNumber = ""
        var cvv = ""

        var expiryMonth = ""

        var expiryYear = ""
        var remainingPoint = "0"

        var is_redeemPoint = "0"
        var redeemAmount = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_summary)
        initViews()
        initControl()

        if (payment_mode == "1") {
            cod_layout.visibility = GONE
        } else {
            cod_layout.visibility = VISIBLE
        }
        if (walletInclude == 1) {
            walletlayout.visibility = VISIBLE
        } else {
            walletlayout.visibility = GONE
        }
        CustomModel.getInstance().setListener(this)
    }

    override fun initViews() {
        if (discountInclude == 1) {
            discount_layout.visibility = View.VISIBLE
            discountAmount.text = discountedAmount.toString() +" "+resources.getString(R.string.aed)
        }
        addOrderViewModel = ViewModelProvider(this).get(AddOrderViewModel::class.java)
        observers()
        val totAmt = totalAmount.replace(resources.getString(R.string.aed), "")
        val delivery = charge_delivery.replace(resources.getString(R.string.aed), "")
        if (payment_mode == "1") {
            totalAmount = (totAmt.toDouble() + shippingFee).toString()
        } else {
            totalAmount = (totAmt.toDouble() + shippingFee + cashOnDelivery).toString()
        }
        if (is_redeemPoint == "1") {
            total_amount.text = ((redeemAmount.toDouble() * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
            estimated_delivery.text = deliveryDate
            net_payable_amount.text = totalAmounts + resources.getString(R.string.aed)
            total_amount_pay.text = ((redeemAmount.toDouble() * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
            delivery_charge_value.text = charge_delivery + resources.getString(R.string.aed)
        } else {
            total_amount.text = ((totalAmount.toDouble() * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
            estimated_delivery.text = deliveryDate
            net_payable_amount.text = totalAmounts + resources.getString(R.string.aed)
            total_amount_pay.text = ((totalAmount.toDouble() * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
            delivery_charge_value.text = charge_delivery + resources.getString(R.string.aed)
        }
        net_wallet_amount.text = walletAmount.toString()
    }

    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
        confirm_order.setOnClickListener {
            if (payment_mode != "1") {
                hitConfirmationPayment()
            } else {
//                startActivityForResult(Intent(this, AddCardAfterOrderActivity::class.java),101)
                getCardPayment()
//
            }

        }
    }

//    override fun stateChanged() {
//        val modelState = CustomModel.getInstance().state
//        Log.e("stateChanged", modelState)
//
//    }

    fun getCardPayment() {
        val randomNum = PaymentActivity.randInt(0, 9999999)
        val m = MerchantDetails()
        m.access_code = "AVQN03IK92BL47NQLB"
        m.merchant_id = "45990"
        m.currency = "AED"
        m.amount = totalAmount
        m.redirect_url = "https://yasmart.azurewebsites.net/ccavResponseHandler"
        m.cancel_url = "https://yasmart.azurewebsites.net/ccavResponseHandler"
        m.rsa_url = "https://yasmart.azurewebsites.net/getrsa"
        m.order_id = randomNum.toString()
        m.customer_id = sharedPreference.storeId.toString()
        m.promo_code = ""
        m.add1 = "test1"
        m.add2 = "test1"
        m.add3 = "test1"
        m.add4 = "test1"
        m.add5 = "test1"
        val b = BillingAddress()
        b.name = "ABC"
        b.address = address_id
        b.country = "United Arab Emirates"
        b.state = "Dubai"
        b.city = "Dubai"
        b.telephone = "+9711234567890"
        b.email = "test@gmail.com"
        val s = ShippingAddress()
        s.name = "XYZ"
        s.address = address_id
        s.country = "United Arab Emirates"
        s.state = "Dubai"
        s.city = "Dubai"
        s.telephone = "+9719876543210"
        m.isCCAvenue_promo = false

        m.isShow_addr = false


        /*// SI data //
            StandardInstructions si = new StandardInstructions();

            String setup_amt ="";
            int selected_setup_amt = radioGroup_setup_amt.getCheckedRadioButtonId();
            if (selected_setup_amt == R.id.radio_yes){
                setup_amt = "Y";
            }else if (selected_setup_amt == R.id.radio_no) {
                setup_amt = "N";
            }

            String freq_type = "";
            int selected_freq_type = radioGroup_freq_type.getCheckedRadioButtonId();
            if (selected_freq_type == R.id.radio_days){
                freq_type = "days";
            }else if (selected_freq_type == R.id.radio_month){
                freq_type = "month";
            }else if (selected_freq_type == R.id.radio_year){



                freq_type = "year";
            }

            String si_type = "";
            int selected_si_type = radioGroup_si_type.getCheckedRadioButtonId();
            if (selected_si_type == R.id.radio_fixed){
                si_type = "FIXED";
                if (!setup_amt.equals("") && !ed_si_amount.getText().toString().equals("")
                        && !start_date.getText().toString().equals("") && !ed_si_freq.getText().toString().equals("")
                        && !ed_bill_cycle.getText().toString().equals("") && !freq_type.equals("")){

                    si.setSi_type(si_type);
                    si.setSi_mer_ref_no(ed_si_ref_no.getText().toString());
                    si.setSi_is_setup_amt(setup_amt);
                    si.setSi_amount(ed_si_amount.getText().toString());
                    si.setSi_start_date(start_date.getText().toString());
                    si.setSi_frequency_type(freq_type);
                    si.setSi_frequency(ed_si_freq.getText().toString());
                    si.setSi_bill_cycle(ed_bill_cycle.getText().toString());

                    Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                    i.putExtra("merchant",m);
                    i.putExtra("billing",b);
                    i.putExtra("shipping",s);
                    i.putExtra("standard instructions", si);
                    startActivity(i);

                }else {
                    Toast.makeText(this, "SI Parameters missing", Toast.LENGTH_SHORT).show();
                }

            }else if (selected_si_type == R.id.radio_on_demand){
                si_type = "ONDEMAND";
                if (!start_date.getText().toString().equals("") && !setup_amt.equals("")){

                    si.setSi_type(si_type);
                    si.setSi_mer_ref_no(ed_si_ref_no.getText().toString());
                    si.setSi_is_setup_amt(setup_amt);
                    si.setSi_start_date(start_date.getText().toString());

                    Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                    i.putExtra("merchant",m);
                    i.putExtra("billing",b);
                    i.putExtra("shipping",s);
                    i.putExtra("standard instructions", si);
                    startActivity(i);

                }else {
                    Toast.makeText(this, "SI Parameters missing", Toast.LENGTH_SHORT).show();
                }
            }else {

                si.setSi_type("");

                Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                i.putExtra("merchant",m);
                i.putExtra("billing",b);
                i.putExtra("shipping",s);
                i.putExtra("standard instructions", si);
                startActivity(i);
            }*/
        val i = Intent(this@PaymentSummaryActivity, PaymentOptions::class.java)
        i.putExtra("merchant", m)
        i.putExtra("billing", b)
        i.putExtra("shipping", s)
        startActivity(i)
    }

    fun hitConfirmationPayment() {
        ProgressDialogUtils.getInstance().showProgress(this, false)
        val wa = if (walletInclude != 1) "0" else walletAmount.toString()
        addOrderViewModel.placeOrder(
            accessToken = sharedPreference.accessToken,
            address_id = address_id,
            store_id = sharedPreference.storeId.toString(),
            coupon_id = coupon_id,
            payment_mode = payment_mode,
            expected_Delivery = expected_delivery,
            delivery_charge = charge_delivery,
            redeemCode = redeemCode,
            total_payable_amount = totalAmount,
            product_id = product_id,
            quantity = quantity,
            remainingPoint = remainingPoint,
            is_redeemPoint = is_redeemPoint,
            redeemAmount = redeemAmount,
            barCodeId = barCodeId,
            totalPrice = totalPrice,
            product_color = totalColorId,
            taxCharge = taxAmount,
            cashOnDelivery = "10",
            walletAmountDeduction = wa,
            total_item_price = total_item_price.toString()
        )
    }

    private fun observers() {
        addOrderViewModel.addOrderReponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            order_id = it.order_summary.order_id
            if (payment_mode != "1") {
                OrderSummaryActivity.addOrderResponse = it
                OrderSummaryActivity.status = 1
                OrderSummaryActivity.payment_mode = payment_mode
                OrderSummaryActivity.shipingCharges = charge_delivery.replace("AED", "")
                Log.e("testordersummury", "paymentSummaryActivity1")
                startActivity(Intent(this, OrderSummaryActivity::class.java))
                finishAffinity()
            } else {
                OrderSummaryActivity.addOrderResponse = it
                var encryptedorder_id = ""
                var encryptedtotalAmount = ""
                var encryptedcardNo_s = ""
                var encryptedmonth_s = ""
                var encryptedyear_s = ""
                var encryptedcvv_s = ""
                var encrypteduserId = ""
                /* val sourceStr = "This is any source string"*/
                try {
                    encryptedorder_id = AESUtils.encrypt(order_id)
                    Log.d("TEST", "encrypted:$encryptedorder_id")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encryptedtotalAmount = AESUtils.encrypt(totalAmount)
                    Log.d("TEST", "encrypted:$encryptedtotalAmount")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encryptedcardNo_s = AESUtils.encrypt(cardNo_s)
                    Log.d("TEST", "encrypted:$encryptedcardNo_s")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encryptedmonth_s = AESUtils.encrypt(month_s)
                    Log.d("TEST", "encrypted:$encryptedmonth_s")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encryptedyear_s = AESUtils.encrypt(year_s)
                    Log.d("TEST", "encrypted:$encryptedyear_s")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encryptedcvv_s = AESUtils.encrypt(cvv)
                    Log.d("TEST", "encrypted:$encryptedcvv_s")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    encrypteduserId = AESUtils.encrypt(sharedPreference.userId)
                    Log.d("TEST", "encrypted:$encrypteduserId")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                ProgressDialogUtils.getInstance().showProgress(this, false)
                addOrderViewModel.doOnlinePayment(
                    sharedPreference.accessToken, AddCardAfterOrderActivity.PaymentOrderModel(
                        encryptedorder_id!!,
                        encryptedtotalAmount,
                        encryptedcardNo_s!!,
                        encryptedmonth_s!!,
                        encryptedyear_s!!,
                        encryptedcvv_s!!, encrypteduserId
                    )
                )
                /*OrderSummaryActivity.addOrderResponse = it
                startActivity(Intent(this, OrderSummaryActivity::class.java))
                finishAffinity()*/
            }

        })

        addOrderViewModel.errorOrderResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        addOrderViewModel.paymentResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            if (it.status == "Error") {
                showToast(it.message)
                OrderSummaryActivity.status = 2
                Log.e("testordersummury", "payment2Activity")
                startActivity(Intent(this, OrderSummaryActivity::class.java))
                finishAffinity()
            } else {
                OrderSummaryActivity.status = 1
                OrderSummaryActivity.payment_mode = payment_mode
                OrderSummaryActivity.shipingCharges = charge_delivery.replace("AED", "")
                Log.e("testordersummury", "payment3Activity")
                startActivity(Intent(this, OrderSummaryActivity::class.java))
                finishAffinity()
//                hitConfirmationPayment()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                cardNo_s = data!!.getStringExtra("cardNo_s")
                month_s = data.getStringExtra("month_s")
                year_s = data.getStringExtra("year_s")
                cvv_s = data.getStringExtra("cvv_s")
                hitConfirmationPayment()
            } else if (resultCode == RESULT_CANCELED) {
                return
            }
        }
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

    override fun stateChanged() {
        val modelState = CustomModel.getInstance().state
        hitConfirmationPayment()
        Log.e("stateChanged", modelState)
        Log.e("stateChanged", "stateChanged")
    }


}