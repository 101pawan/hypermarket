package com.hypermarket_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hypermarket_android.Fragment.OrderItemsFragment
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.OrderListResponse
import kotlinx.android.synthetic.main.activity_my_order.*

class OrderItemsActivity : AppCompatActivity() {

    var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_items)
        showMyFragment()
        iv_actionbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    fun showMyFragment() {
//        val order_products: ArrayList<OrderListResponse.OrderProducts> = intent.getSerializableExtra("products") as ArrayList<OrderListResponse.OrderProducts>

        var position = intent.getStringExtra("order_position").toString().toIntOrNull()
        var fragment_position = intent.getStringExtra("fragment_position").toString()
        fragment =   OrderItemsFragment(position!!,fragment_position!!)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment as OrderItemsFragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}