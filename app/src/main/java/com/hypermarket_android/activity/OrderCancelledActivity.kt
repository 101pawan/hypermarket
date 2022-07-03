package com.hypermarket_android.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.OrderListResponse
import kotlinx.android.synthetic.main.activity_order_cancelled.*

class OrderCancelledActivity : BaseActivity() {

    companion object {
        var addOrderResponse: OrderListResponse.OrderData? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_cancelled)
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
            " ${addOrderResponse!!.house_number ?: ""}, ${addOrderResponse!!.building_name ?: ""}, ${addOrderResponse!!.street ?: ""} "

        tv_user_name.text = sharedPreference.full_name
        net_payable_amount.text = addOrderResponse!!.total_payable_amount + " AED"
        total_amount_pay.text = addOrderResponse!!.total_payable_amount + " AED"
        order_id.text = "#" + addOrderResponse!!.order_id
        order_date.text = addOrderResponse!!.order_date
    }

    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
    }


}