package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.util.convertNewDate
import kotlinx.android.synthetic.main.activity_order_track.*

class OrderTrackActivity : BaseActivity() {

    companion object {
        var addOrderResponse: OrderListResponse.OrderData? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_track)
        initViews()
        initControl()
    }

    override fun initViews() {
        products_recyclerview.layoutManager = LinearLayoutManager(this)
        products_recyclerview.adapter = addOrderResponse!!.expected_delivery?.toLong()?.let {
            OrderProductAdapterTrack(
                this,
                listOf(addOrderResponse),
                it
            )
        }
        tv_address_detail.text =
            " ${addOrderResponse?.house_number ?: ""}, ${addOrderResponse?.building_name ?: ""}, ${addOrderResponse?.street ?: ""} "

        tv_user_name.text = sharedPreference.full_name
        net_payable_amount.text = addOrderResponse!!.total_payable_amount + resources.getString(R.string.aed)
        total_amount_pay.text = addOrderResponse!!.total_payable_amount + resources.getString(R.string.aed)
        order_id.text = "#" + addOrderResponse!!.order_id
        order_date.text = convertNewDate(addOrderResponse!!.created_at!!.split(".")[0]!!)

        setTracking()
    }


    private fun setTracking() {
        Log.e("settracking",addOrderResponse?.order_status.toString())
        when(addOrderResponse?.order_status){
            "2" -> {
                iv_dispatched.background = resources.getDrawable(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_shipped.background = resources.getDrawable(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.background = resources.getDrawable(R.drawable.circle_white)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.color_text_9F9F9F))
                iv_delivered_food.background = resources.getDrawable(R.drawable.circle_white)
            }
            "3" -> {
                iv_shipped.background = resources.getDrawable(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.background = resources.getDrawable(R.drawable.circle_bg)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_delivered_food.background = resources.getDrawable(R.drawable.circle_white)
            }
            "4" -> {
                iv_dispatched.background = resources.getDrawable(R.drawable.circle_bg)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_shipped.background = resources.getDrawable(R.drawable.circle_bg)
                v_shipped.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                out_for_Delivery.background = resources.getDrawable(R.drawable.circle_bg)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.light_green_00973D))
                iv_delivered_food.background = resources.getDrawable(R.drawable.circle_bg)
            }
            else -> {
                iv_dispatched.background = resources.getDrawable(R.drawable.circle_white)
                v_dispatched.setBackgroundColor(resources.getColor(R.color.color_text_9F9F9F))
                iv_shipped.background = resources.getDrawable(R.drawable.circle_white)
                v_shipped.setBackgroundColor(resources.getColor(R.color.color_text_9F9F9F))
                out_for_Delivery.background = resources.getDrawable(R.drawable.circle_white)
                v_out_for_Delivery.setBackgroundColor(resources.getColor(R.color.color_text_9F9F9F))
                iv_delivered_food.background = resources.getDrawable(R.drawable.circle_white)
            }
        }
    }

    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_cancel.setOnClickListener {
            finish()
        }
        btn_continue.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }
}