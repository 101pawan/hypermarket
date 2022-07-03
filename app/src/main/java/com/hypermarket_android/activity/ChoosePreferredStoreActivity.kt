package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.StoreAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.StoreDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.GridSpacingItemDecoration
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.StoreViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChoosePreferredStoreActivity : AppCompatActivity() {

    private var selectedStoreId: Int = 0
    private var selectedStoreName: String = ""
    private var listOfStore: ArrayList<StoreDataModel.StoreData> = ArrayList()
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var storeViewModel: StoreViewModel
    private var isHome: Boolean = false

    private lateinit var sharedPreference: SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_preferred_store)
        val tvToolBarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        val rvStore = findViewById<RecyclerView>(R.id.rv_store)
        progressBar = findViewById(R.id.progressbar)
        tvReload = findViewById(R.id.tv_reload)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        isHome = intent.getBooleanExtra("isHome", false)
        sharedPreference = SharedPreferenceUtil.getInstance(this)
        if (isHome) {
            tvToolBarTitle.text = resources.getString(R.string.stores)
        } else {
            tvToolBarTitle.text = resources.getString(R.string.choose_preffered_store)
        }
        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);
        rvStore.layoutManager == GridLayoutManager(baseContext, 2)
        rvStore.addItemDecoration(GridSpacingItemDecoration(5))
        storeAdapter = StoreAdapter(isHome, listOfStore, this)
        storeAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(
                id: Int,
                position: Int
            ) {
                if (storeAdapter.getItem(position).isSelected) {
                    selectedStoreId = storeAdapter.getItem(position).id
                    selectedStoreName = storeAdapter.getItem(position).name
                    Log.d("storeId==", selectedStoreId.toString() + "=1")
                } else {
                    selectedStoreId = 0
                    Log.d("storeId==", selectedStoreId.toString() + "=2")
                }
            }
        })
        rvStore.adapter = storeAdapter
        if (Helper.isNetworkAvailable(this)) {
            getStoreList()
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(this)) {
                getStoreList()
            } else {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnSubmit.setOnClickListener {

            Log.d("storeId==", selectedStoreId.toString() + "=3")

            if (selectedStoreId != 0) {
                sharedPreference.storeId = selectedStoreId
                sharedPreference.storeName = selectedStoreName
                val intent = Intent(baseContext, HomeActivity::class.java)
                intent.putExtra("storeId", selectedStoreId)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else if (sharedPreference.storeId != 0) {
                onBackPressed()
            } else {
                Toast.makeText(baseContext, resources.getString(R.string.please_select_store), Toast.LENGTH_SHORT).show()
            }
        }


    }


    fun getStoreList() {

        val sharedPreference = SharedPreferenceUtil.getInstance(this)
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE
        storeViewModel.getStoreList(sharedPreference.country, object : Callback<StoreDataModel> {
            override fun onFailure(call: Call<StoreDataModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                tvReload.visibility = View.VISIBLE

                Log.d("country==", "error==" + t.message)

            }

            override fun onResponse(
                call: Call<StoreDataModel>,
                response: Response<StoreDataModel>
            ) {

                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.GONE
                    listOfStore.clear()



                    listOfStore.addAll(response.body()!!.store_list)

                    /*     for (store in listOfStore){
                             if (store.id==sharedPreference.storeId){
                                 store.isSelected=true
                                 listOfStore.add(store)

                             }
                         }*/
                    storeAdapter.notifyDataSetChanged()

                } else {
                    Log.d("country==", "error1==" + response.message())

                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }
            }

        })
    }


}
