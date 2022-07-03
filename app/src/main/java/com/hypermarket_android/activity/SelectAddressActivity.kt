package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pharmadawa.ui.notification.AddressListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.ManageAddressResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.SelectAddressViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_select_address.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SelectAddressActivity : BaseActivity() {

    lateinit var selectAddressViewModel: SelectAddressViewModel

    var address_id = ""
    var is_available="0"
    var availableMessage="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        initViews()
        initControl()
        addressObservable()

        iv_actionbar_back.setOnClickListener {
            onBackPressed()
        }
        btn_add_new_address.setOnClickListener {
            startActivityForResult(
                Intent(this, AddUpdateAddressActivity::class.java),
                REQUEST_NEW_ADDRESS
            )
        }
        rv_address.layoutManager = LinearLayoutManager(baseContext)
    }

    override fun initViews() {
        selectAddressViewModel = ViewModelProvider(this).get(SelectAddressViewModel::class.java)
        getAddress()
    }

    override fun initControl() {
        btn_continue.setOnClickListener {
            if (address_id == "") {
                showToast(resources.getString(R.string.select_address))
            } else {
                when(is_available){
                    "1"->{
                        SelectPaymentModeActivity.address_id = address_id
                        startActivity(Intent(this, SelectPaymentModeActivity::class.java))
                    }
                    "0"->{
                        showToast(availableMessage)
                    }
                }
            }
        }
    }

    private fun addressObservable() {
        selectAddressViewModel.manageAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            rv_address.adapter = AddressListAdapter(this, it.user_address, object :
                AddressListAdapter.onClickListener {
                override fun onClick(
                    userAddres: ManageAddressResponse.UserAddres,
                    type: String
                ) {
                    when (type) {
                        "delete" -> {
                            selectAddressViewModel.hitDeletAddressApi(
                                sharedPreference.accessToken,
                                userAddres.id.toString()
                            )
                        }
                        "edit" -> {
                            startActivityForResult(
                                Intent(applicationContext, AddUpdateAddressActivity::class.java).putExtra(
                                    DATA_UPDATE_ADDRESS,
                                    Gson().toJson(userAddres)
                                ), REQUEST_NEW_ADDRESS
                            )
                            /*startActivity(
                                Intent(
                                    applicationContext,
                                    AddUpdateAddressActivity::class.java
                                )
                            )*/
                        }
                        "select" -> {
                            //  showToast(userAddres.id.toString())
//                            SelectPaymentModeActivity.charge_delivery = userAddres.pr/
                            address_id = userAddres.id.toString()
                            ProgressDialogUtils.getInstance().showProgress(
                                this@SelectAddressActivity,
                                true
                            )
                            selectAddressViewModel.productServiceCharge(
                                sharedPreference.accessToken,
                                userAddres.city.toString()
                            )
                        }
                    }
                }
            }
            )
        })

        selectAddressViewModel.errorManageAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })


        selectAddressViewModel.checkAvailablityResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            is_available = it.is_available!!
            when (is_available) {
                "1" -> {
                    availableMessage=it.message!!
                    //showToast(resources.getString(R.string.please_enter_review))
                    Log.e("checkAvailablityResponse",it.data!!.price!!.toString())
                    SelectPaymentModeActivity.charge_delivery = it.data!!.price!!
                    val dt = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
                    println(dt)
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val c: Calendar = Calendar.getInstance()
                    try {
                        c.time = sdf.parse(dt)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    c.add(
                        Calendar.DATE,
                        it.data.deliveryTime!!.toInt()
                    ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

                    val sdf1 = SimpleDateFormat("dd-MMM-yyyy")
                    val output: String = sdf1.format(c.time)
                    println(output)
                    println(c.time.time.toString())
                    SelectPaymentModeActivity.deliveryDate = output
                    println("Bharat===>"+SimpleDateFormat("dd-MMMM-yyyy").format(Date( c.time.time)))
                    SelectPaymentModeActivity.expected_delivery = c.time.time.toString()
                }
                "0" -> {
                    availableMessage=it.message!!
                    showToast(availableMessage)

                }
            }
        })
        selectAddressViewModel.deleteAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            getAddress()

        })
        selectAddressViewModel.errorDeleteAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
        })
    }



    companion object {
        const val REQUEST_NEW_ADDRESS = 1234
        const val DATA_UPDATE_ADDRESS = "data_update_address"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEW_ADDRESS && resultCode == RESULT_OK) {
            getAddress()
        }
    }


    private fun getAddress() {
        ProgressDialogUtils.progressDialog?.showProgress(this, false)
        selectAddressViewModel.hitManageAddressApi(sharedPreference.accessToken)

    }

    override fun onResume() {
        super.onResume()
       // getAddress()
    }


}
