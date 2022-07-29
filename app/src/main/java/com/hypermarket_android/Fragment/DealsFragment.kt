package com.hypermarket_android.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.DealsTabRecyclerAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.DealsDataModel
import com.hypermarket_android.util.GridSpacingItemDecoration
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.hypermarket_android.viewModel.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DealsFragment : Fragment() {

    private var listOfDeals: ArrayList<DealsDataModel.DealsData> = ArrayList()

    private lateinit var productViewModel: ProductViewModel
    private lateinit var dealAdapter: DealsTabRecyclerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var sharedPreferences: SharedPreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_deals, container, false)


        progressBar = view.findViewById(R.id.progressbar)
        tvReload = view.findViewById(R.id.tv_reload)
        val rvDeals = view.findViewById<RecyclerView>(R.id.rv_deals)


        tvReload.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_reload, 0, 0);

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        sharedPreferences = SharedPreferenceUtil.getInstance(context!!)


        rvDeals.layoutManager == LinearLayoutManager(context)
        rvDeals.addItemDecoration(GridSpacingItemDecoration(10))
        dealAdapter = DealsTabRecyclerAdapter(activity, listOfDeals)
        rvDeals.adapter = dealAdapter

        if (Helper.isNetworkAvailable(activity!!)) {
            getDeals()
        } else {
            tvReload.visibility = View.VISIBLE
        }

        tvReload.setOnClickListener {
            if (Helper.isNetworkAvailable(activity!!)) {
                getDeals()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.message_no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }


    fun getDeals() {
        progressBar.visibility = View.VISIBLE
        tvReload.visibility = View.GONE

        productViewModel.getDeals(sharedPreferences.accessToken,
            sharedPreferences.storeId,
            object : Callback<DealsDataModel> {
                override fun onFailure(call: Call<DealsDataModel>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    tvReload.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<DealsDataModel>,
                    response: Response<DealsDataModel>
                ) {

                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.GONE
                        listOfDeals.clear()
                        listOfDeals.addAll(response.body()!!.deals)
                        dealAdapter.notifyDataSetChanged()

                    } else {
                        progressBar.visibility = View.GONE
                        tvReload.visibility = View.VISIBLE
                    }
                }

            })
    }
}
