package com.hypermarket_android.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.CouponListResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.MyPointsViewModel
import com.hypermarket_android.viewModel.WalletViewModel
import kotlinx.android.synthetic.main.activity_select_payment.*
import kotlinx.android.synthetic.main.activity_select_payment.btn_back
import kotlinx.android.synthetic.main.activity_wallet.*
import java.text.DecimalFormat
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class SelectPaymentModeActivity : BaseActivity() {

    private var paymentMode = "0"
    var redeemCode = "0"
    val REQUEST_CODE_ADD_CARD = 1
    val REQUEST_COUPON_CODE = 2
    var WALLETBALANCE = ""
    val tax = 5
    var walletInclude = 0
    var discountInclude = 0
    var isCardDetailsFilled = false
    var offerAmount = 0.0
    lateinit var myPointsViewModel: MyPointsViewModel
    companion object {
        var totalAmount = "0"
        var address_id = ""
        var product_id = ""
        var barCodeId = ""
        var coupon_id = "0"
        var quantity = ""
        var totalPrice = ""
        var totalColor = ""
        var charge_delivery = ""
        var expected_delivery = ""
        var deliveryDate = ""
        var cardNumber = ""
        var cardHolderName = ""
        var cvv = ""
        var expiryMonth = ""
        var expiryYear = ""
        var couponModel: CouponListResponse.CouponModel? = null
    }
    var totalMainAmount = "0"
    var originalAmount = 0.0
    var originalWalletAmount = 0.0
    private lateinit var walletViewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        walletViewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        walletViewModel.getWalletData(sharedPreference.accessToken,sharedPreference.userId)
        initViews()
        initControl()
        initCheckBox()
    }

    private fun initCheckBox() {
        walletViewModel.amount.observe(this, Observer {
            Log.e("initCheckBox",it)
            wallet_amount.text = it
            WALLETBALANCE = it
        })
        includeWallet.setOnCheckedChangeListener { buttonView, isChecked ->
            val walletAmount = wallet_amount.text
            val payableAmount = total_amount.text.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
            val payableWalletAmount = walletAmount.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
            var reducingWalletAmount = 0.0
            var usedBalance = 0.0
            var resultAmount = 0.0
            if (isChecked){
                originalAmount = payableAmount
                originalWalletAmount = payableWalletAmount
                Log.e("payableAmount",payableAmount.toString())
                Log.e("originalAmount",originalAmount.toString())
                if (payableAmount > payableWalletAmount){
                    reducingWalletAmount = 0.0
                    resultAmount = payableAmount - originalWalletAmount
                }else{
                    reducingWalletAmount = originalWalletAmount - payableAmount
                }
                usedBalance = originalWalletAmount - reducingWalletAmount
                walletInclude = 1
            }else{
                Log.e("originalAmount",originalAmount.toString())
                if (payableAmount < originalWalletAmount){
                    resultAmount = originalAmount
                }else{
                    resultAmount = payableAmount + originalWalletAmount
                }
                reducingWalletAmount = originalWalletAmount
                usedBalance = 0.0
                walletInclude = 0
            }
            wallet_amount.text = "${(reducingWalletAmount * 100.0).roundToInt() / 100.0}"
            WALLETBALANCE = usedBalance.toString()
            total_amount.text = "${(resultAmount * 100.0).roundToInt() / 100.0} ${resources.getString(R.string.aed)}"
//            total_amount.text = "$resultAmount ${resources.getString(R.string.aed)}"
        }
    }

    override fun initViews() {
        totalAmount = totalAmount.replace("null","").trim()
        val maintotals =totalAmount.toDouble() + charge_delivery.toDouble()
        val maintotal= DecimalFormat("#.##").format(maintotals)
        subtotal_amount.text = "${(totalAmount.toDouble() - totalAmount.toDouble() * tax/100 )} ${resources.getString(R.string.aed)}"
        cashAmount.text = charge_delivery.toDouble().toString() + resources.getString(R.string.aed)
        tv_tax_value.text = (totalAmount.toDouble() * tax/100).toString() + resources.getString(R.string.aed)
        total_amount.text =  (((maintotal.toDouble() + 10.0 + 10.0) * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
        totalMainAmount = (maintotal).toString()
        cash.isChecked = true
        myPointsViewModel = ViewModelProvider(this).get(MyPointsViewModel::class.java)
        hitGetEarnPoints()
        observers()
    }

    fun observers() {
        myPointsViewModel.errorResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })
        myPointsViewModel.earnResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            tvPoints.text=it.earnPoint!!.toString()+" points"
            val earnpoints= it.earnPoint!!.toString()
            val point= it.pointValue!!.toString()
            val totalPoint=it.TotalPoint!!.toString()
            checkPoints.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (earnpoints.toInt() >= totalPoint.toInt()) {
                        /*{"message":"success","earnPoint"120,"TotalPoint":"100","pointValue":"100"}*/
                        var remainAmt = 0.0f
                        //                            var minAMt=5*it.pointValue.toFloat()
                        var minAMt = it.pointValue.toFloat()
                        if (totalAmount.toFloat() >= minAMt) {
                            remainAmt = totalAmount.toFloat() - point.toFloat()
                            val remainingPoints = earnpoints.toInt() - totalPoint.toInt()
                            val maintotals =
                                remainAmt.toDouble().absoluteValue + charge_delivery.toDouble()
                            val maintotal = DecimalFormat("#.##").format(maintotals)
                            subtotal_amount.text =
                                remainAmt.toDouble().absoluteValue.toString() + resources.getString(
                                    R.string.aed
                                )
                            cashAmount.text = charge_delivery.toDouble()
                                .toString() + resources.getString(R.string.aed)
                            total_amount.text = ((maintotal.toDouble()  * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                            totalMainAmount = maintotal.toString()
                            PaymentSummaryActivity.is_redeemPoint = "1"
                            PaymentSummaryActivity.redeemAmount = "$remainAmt"
                            PaymentSummaryActivity.remainingPoint = "$remainingPoints"

                        } else {
                            remainAmt = point.toFloat() - totalAmount.toFloat()
                            showToast("Add $remainAmt AED more to avail this redeem points.")
                            PaymentSummaryActivity.is_redeemPoint = "0"
                            PaymentSummaryActivity.redeemAmount = "0.0"
                            PaymentSummaryActivity.remainingPoint = "0"
                            checkPoints.isChecked = false
                            val maintotals = totalAmount.toDouble() + charge_delivery.toDouble()
                            val maintotal = DecimalFormat("#.##").format(maintotals)
                            subtotal_amount.text = totalAmount.toDouble()
                                .toString() + resources.getString(R.string.aed)
                            cashAmount.text = charge_delivery.toDouble()
                                .toString() + resources.getString(R.string.aed)
                            total_amount.text = ((maintotal.toDouble()  * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                            totalMainAmount = maintotal.toString()
                        }
                    } else {
                        checkPoints.isChecked = false
                        showToast("You have required atleast ${totalPoint.toInt()} points to redeem.")

                        PaymentSummaryActivity.is_redeemPoint = "0"
                        PaymentSummaryActivity.redeemAmount = "0.0"
                        PaymentSummaryActivity.remainingPoint = "0"

                        val maintotals = totalAmount.toDouble() + charge_delivery.toDouble()
                        val maintotal = DecimalFormat("#.##").format(maintotals)
                        subtotal_amount.text =
                            ((totalAmount.toDouble()  * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                        cashAmount.text = charge_delivery.toDouble()
                            .toString() + resources.getString(R.string.aed)
                        total_amount.text = ((maintotal.toDouble()  * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                        totalMainAmount = maintotal.toString()
                    }

                } else {
                    PaymentSummaryActivity.is_redeemPoint = "0"
                    PaymentSummaryActivity.redeemAmount = "0.0"
                    PaymentSummaryActivity.remainingPoint = "0"
                    val maintotals = totalAmount.toDouble() + charge_delivery.toDouble()
                    val maintotal = DecimalFormat("#.##").format(maintotals)
                    subtotal_amount.text =
                        ((totalAmount.toDouble() * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                    cashAmount.text =
                        charge_delivery.toDouble().toString() + resources.getString(R.string.aed)
                    total_amount.text = ((maintotal.toDouble()  * 100.0).roundToInt() / 100.0).toString() + resources.getString(R.string.aed)
                    totalMainAmount = maintotal.toString()
                }
            }
        })
    }


    fun hitGetEarnPoints(){
        ProgressDialogUtils.getInstance().showProgress(this,true)
        myPointsViewModel.getUserRewardPoint(
            accessToken =  sharedPreference.accessToken,
            user_id =  sharedPreference.userId)
    }


    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }

        ll_debit_credit_card.setOnClickListener {
            add_Card.visibility = View.VISIBLE
            card.isChecked = true
            cash.isChecked = false
            paymentMode = "1"
        }
        ll_cash.setOnClickListener {
            add_Card.visibility = View.GONE
            cash.isChecked = true
            card.isChecked = false
            paymentMode = "0"
        }
        add_Card.setOnClickListener {
            val intent = Intent(
                this, AddCardActivity::class.java
            )
            startActivityForResult(intent, REQUEST_CODE_ADD_CARD)
            // startActivity(Intent(this, AddCardActivity::class.java))

        }

        tv_apply_coupon_code_dotted_btn.setOnClickListener {
            MyCouponsActivity.product_id = product_id
            val intent = Intent(
                this, MyCouponsActivity::class.java
            )
            startActivityForResult(intent, REQUEST_COUPON_CODE)
            // startActivity(Intent(this, MyCouponsActivity::class.java))
        }
        place_order.setOnClickListener {
            var payableAmount = total_amount.text.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
            var resultAmount = 0.0
            if(paymentMode == "1"){
                payableAmount = payableAmount - 10.0
            }else{
                payableAmount = payableAmount - 10.0 - 10.0
            }
            PaymentSummaryActivity.totalAmount = "$payableAmount ${resources.getString(R.string.aed)}"
            PaymentSummaryActivity.walletAmount = WALLETBALANCE.toDouble()
            PaymentSummaryActivity.total_item_price = totalAmount.toDouble()
            PaymentSummaryActivity.taxAmount = tv_tax_value.text.toString().replace(resources.getString(R.string.aed),"").trim()
            PaymentSummaryActivity.totalAmounts="$totalAmount"
            PaymentSummaryActivity.totalAmounts="$totalAmount"
            PaymentSummaryActivity.address_id = address_id
            if (couponModel != null) {
                coupon_id = couponModel?.id.toString()
            }
            PaymentSummaryActivity.coupon_id = coupon_id
            PaymentSummaryActivity.charge_delivery = charge_delivery
            PaymentSummaryActivity.payment_mode = paymentMode
            PaymentSummaryActivity.quantity = quantity
            PaymentSummaryActivity.totalPrice = totalPrice
            PaymentSummaryActivity.totalColorId = totalColor
            PaymentSummaryActivity.product_id = product_id
            PaymentSummaryActivity.discountedAmount = offerAmount
            PaymentSummaryActivity.discountInclude = discountInclude
            PaymentSummaryActivity.barCodeId = barCodeId
            PaymentSummaryActivity.redeemCode = redeemCode
            PaymentSummaryActivity.expected_delivery = expected_delivery
            PaymentSummaryActivity.deliveryDate = deliveryDate
            PaymentSummaryActivity.walletInclude = walletInclude
//            if (paymentMode == "0") {
//                startActivity(Intent(this, PaymentSummaryActivity::class.java))
//            } else {
//                if (isCardDetailsFilled) {
//                    startActivity(Intent(this, PaymentSummaryActivity::class.java))
//                } else {
//                    showToast("Please Enter Card Details !")
//                }
//            }
            startActivity(Intent(this, PaymentSummaryActivity::class.java))
        }

        card.setOnCheckedChangeListener { compoundButton, b ->
            val payableAmount = total_amount.text.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
            var resultAmount = 0.0
            if (b) {
                add_Card.visibility = View.GONE
                cash.isChecked = false
                card.isChecked = true
                paymentMode = "1"
                cod_layout.visibility = GONE
                resultAmount = payableAmount - 10.0
            } else {
                cod_layout.visibility = VISIBLE
                add_Card.visibility = View.GONE
                card.isChecked = false
                cash.isChecked = true
                paymentMode = "0"
                resultAmount = payableAmount + 10.0
            }
            total_amount.text = "${(resultAmount  * 100.0).roundToInt() / 100.0} ${resources.getString(R.string.aed)}"
        }
        cash.setOnCheckedChangeListener { compoundButton, b ->
            val payableAmount = total_amount.text.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
            var resultAmount = 0.0
            if (b) {
                add_Card.visibility = View.GONE
                cash.isChecked = true
                card.isChecked = false
                paymentMode = "0"
                cod_layout.visibility = VISIBLE
                resultAmount = payableAmount + 10.0
            } else {
                cod_layout.visibility = GONE
                add_Card.visibility = View.GONE
                card.isChecked = true
                cash.isChecked = false
                paymentMode = "1"
                resultAmount = payableAmount - 10.0
            }
            total_amount.text = "${(resultAmount * 100.0).roundToInt() / 100.0} ${resources.getString(R.string.aed)}"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_ADD_CARD && resultCode == Activity.RESULT_OK) {
                PaymentSummaryActivity.cardNumber = data?.getStringExtra("cardNo")!!
                PaymentSummaryActivity.cvv = data.getStringExtra("cvv")!!
                PaymentSummaryActivity.expiryYear = data.getStringExtra("year")!!
                PaymentSummaryActivity.expiryMonth = data.getStringExtra("month")!!
                isCardDetailsFilled = true
            } else if (requestCode == REQUEST_COUPON_CODE && resultCode == Activity.RESULT_OK) {
                Log.e("checkcoupontype", couponModel?.coupon_type.toString())
                if (couponModel?.coupon_type != "Cart") {
                    off_discount.text = couponModel!!.discount + " % off"
                }else{
                    off_discount.text = "${couponModel!!.discount} ${getString(R.string.aed)} off"
                }
                code_Desc.text = couponModel!!.description
                tv_apply_coupon_code_dotted_btn.text = couponModel!!.coupon_code
                couponModel?.discount?.let { Log.e("getcouponcode", it) }

                val payableAmount = (((DecimalFormat("#.##").format(totalAmount.toDouble() + charge_delivery.toDouble()).toDouble() + 10.0 + 10.0) * 100.0).roundToInt() / 100.0)
//                val payableAmount = total_amount.text.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
//                val payableWalletAmount = walletAmount.toString().replace(resources.getString(R.string.aed),"").trim().toDouble()
                var reducingWalletAmount = 0.0
                var usedBalance = 0.0
                var resultAmount = 0.0
                originalAmount = payableAmount
//                originalWalletAmount = payableWalletAmount
                Log.e("payableAmount",payableAmount.toString())
                Log.e("originalAmount",originalAmount.toString())
                Log.e("totalAmount",totalAmount.toString())

                if (couponModel?.coupon_type != "Cart") {
                    couponModel?.discount?.let {
                        offerAmount = totalAmount.toDouble() * it.toDouble() / 100
                    }
                }else{
                    couponModel?.discount?.let {
                        offerAmount =  it.toDouble()
                    }
                }


//                if (payableAmount > payableWalletAmount){
//                    reducingWalletAmount = 0.0
                    resultAmount = payableAmount - offerAmount
//                }else{
//                    reducingWalletAmount = originalWalletAmount - payableAmount
//                }
//                usedBalance = originalWalletAmount - reducingWalletAmount
//
//                wallet_amount.text = "${(reducingWalletAmount * 100.0).roundToInt() / 100.0}"
//                WALLETBALANCE = usedBalance.toString()
                Log.e("resultAmount",resultAmount.toString())
                discountInclude = 1
                discount_layout.visibility = VISIBLE
                Log.e("testdiscount",discount_layout.isVisible.toString())
                total_amount.text = "${(resultAmount * 100.0).roundToInt() / 100.0} ${resources.getString(R.string.aed)}"
                discountAmount.text = offerAmount.toString() +" "+resources.getString(R.string.aed)
            }
        } catch (ex: Exception) {
            Toast.makeText(
                this, ex.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}