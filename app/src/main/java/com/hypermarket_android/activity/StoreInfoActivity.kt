package com.hypermarket_android.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.StoreDetailDataModel
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.StoreViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreInfoActivity : AppCompatActivity() {

    private lateinit var ivStoreImage: ImageView
    private lateinit var tvStoreName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvAboutText: TextView
    private lateinit var rbStore: RatingBar
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var clParentLayout: ConstraintLayout
    private lateinit var storeViewModel: StoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info)

        val storeId = intent.getIntExtra("storeId", 0)

        clParentLayout = findViewById(R.id.cl_parent_layout)
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivStoreImage = findViewById(R.id.iv_store_image)
        tvStoreName = findViewById(R.id.tv_store_name)
        tvAddress = findViewById(R.id.tv_store_address)
        tvAboutText = findViewById(R.id.tv_about_text)
        tvRating = findViewById(R.id.tv_rating)
        rbStore = findViewById(R.id.simpleRatingBar)
        tvReload = findViewById(R.id.tv_reload)
        progressBar = findViewById(R.id.progressbar)

        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        ivBack.setOnClickListener {
            onBackPressed()
        }



        if (Helper.isNetworkAvailable(this)) {
            getStoreDetail(storeId)
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(this)) {
                getStoreDetail(storeId)
            } else {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun getStoreDetail(storeId: Int) {

        val sharedPreference = SharedPreferenceUtil.getInstance(this)
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE
        storeViewModel.getStoreDetail(storeId, object : Callback<StoreDetailDataModel> {
            override fun onFailure(call: Call<StoreDetailDataModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                tvReload.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<StoreDetailDataModel>,
                response: Response<StoreDetailDataModel>
            ) {

                if (response.isSuccessful) {
                    clParentLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.GONE

                    setData(response.body()!!)

                } else {

                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }
            }

        })
    }


    fun setData(storeDetailDataModel: StoreDetailDataModel) {

        Glide.with(this).load(storeDetailDataModel.store_detail.image).thumbnail(0.1f)
            .into(ivStoreImage)
        tvStoreName.text = storeDetailDataModel.store_detail.name
        tvAddress.text = storeDetailDataModel.store_detail.address
        rbStore.rating = storeDetailDataModel.store_detail.rating.toFloat()
        tvRating.text = storeDetailDataModel.store_detail.rating
        tvAboutText.text = storeDetailDataModel.store_detail.about

    }

    fun setVisibilityVisible() {
        ivStoreImage.visibility = View.VISIBLE

    }


}
