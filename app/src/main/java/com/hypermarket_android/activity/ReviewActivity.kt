package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.AddRatingViewModel
import kotlinx.android.synthetic.main.activity_review.*


class ReviewActivity : BaseActivity() {
    lateinit var ratingViewModel: AddRatingViewModel

    companion object {
        var productImage = ""
        var productName = ""
        var product_id = ""

    }
    private var ratingValue = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        initViews()
        initControl()
    }
    override fun initViews() {
        ratingViewModel = ViewModelProvider(this).get(AddRatingViewModel::class.java)
        ratingbar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingValue = rating.toString()
             //   showToast(ratingValue)
            }

        product_name.text = productName
        Glide.with(this).load(productImage).into(product_img)


    }

    override fun initControl() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_cancel.setOnClickListener {
            finish()
        }
        btn_submit.setOnClickListener {


            when {
                ratingValue == "0" -> {
                    showToast(resources.getString(R.string.please_enter_rating))
                }
                review_message.text.toString() == "" -> {
                    showToast(resources.getString(R.string.please_enter_review))
                }
                else -> {
                    ProgressDialogUtils.getInstance().showProgress(this, false)
                    ratingViewModel.addRating(
                        sharedPreference.accessToken,
                        sharedPreference.userId,
                        product_id,
                        ratingValue,
                        review_message.text.toString()
                    )
                }
            }
        }

        ratingViewModel.addRatingResponse.observe(this, Observer {
            showToast(it.message)
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()

        })
        ratingViewModel.errorRatingResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })


    }
}