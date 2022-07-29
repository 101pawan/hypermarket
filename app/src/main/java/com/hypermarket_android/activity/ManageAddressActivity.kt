package com.hypermarket_android.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hypermarket_android.Adapter.ManageAddressAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.ManageAddressResponse
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.ManageAddressViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_manage_address.*
import kotlinx.android.synthetic.main.dialog_item_delete_address.*

class ManageAddressActivity : BaseActivity(), ManageAddressAdapter.ManageAddressClickListener {
    private var mAdapter: ManageAddressAdapter? = null
    private lateinit var mAddAddressViewModel: ManageAddressViewModel

    companion object {
        const val REQUEST_NEW_ADDRESS = 1234
        const val DATA_UPDATE_ADDRESS = "data_update_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_address)
        mAddAddressViewModel = ViewModelProvider(this).get(ManageAddressViewModel::class.java)
        rv_manage_address.layoutManager = LinearLayoutManager(this)
        mAdapter = ManageAddressAdapter(this, this)
        rv_manage_address.adapter = mAdapter


        iv_actionbar_back.setOnClickListener { onBackPressed() }


        // get manage address listing from server
        if (Helper.isNetworkAvailable(this!!)) {
            mAddAddressViewModel.hitManageAddressApi(sharedPreference.accessToken)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.message_no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()


        }
        manageAddressObervable()
        deleteAddressObervable()
    }

    private fun manageAddressObervable() {
        mAddAddressViewModel.manageAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            val data = getData(it.user_address)
            mAdapter?.setData(data)
        })

        mAddAddressViewModel.errorManageAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        mAddAddressViewModel.mProgessManageAddress.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }


    private fun deleteAddressObervable() {
        mAddAddressViewModel.deleteAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            showToast(it.message)
            mAddAddressViewModel.hitManageAddressApi(sharedPreference.accessToken)
        })


        mAddAddressViewModel.errorDeleteAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        mAddAddressViewModel.mProgessDeleteAddress.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }


    private fun getData(list: List<ManageAddressResponse.UserAddres>): List<ManageAddressAdapter.Item> {
        val result = ArrayList<ManageAddressAdapter.Item>()
        for (item in list) {
            val content = ManageAddressAdapter.Item.Builder()
                .type(ManageAddressAdapter.MANAGE_ADDRESS_CONTENT)
                .addressItem(item)
                .build()
            result.add(content)
        }
        val footer = ManageAddressAdapter.Item.Builder().addressItem(null)
            .type(ManageAddressAdapter.MANAGE_ADDRESS_FOOTER).build()
        result.add(footer)

        return result
    }

    override fun initViews() {
    }

    override fun initControl() {
    }

    // start add new address
    override fun onSaveBtnClick() {

        /*startActivityForResult(Intent(this, AddAddressActivity::class.java)
            .putExtra(RESTAURANT_DATA_FOR_CREDIT_CARD, Gson().toJson(mCartResponseResult)), REQUEST_LOCATION)
        */
        startActivityForResult(
            Intent(this, AddUpdateAddressActivity::class.java),
            REQUEST_NEW_ADDRESS
        )

    }

    override fun onEditBtnClick(addRessItem: ManageAddressResponse.UserAddres?) {
        startActivityForResult(
            Intent(this, AddUpdateAddressActivity::class.java).putExtra(
                DATA_UPDATE_ADDRESS,
                Gson().toJson(addRessItem)
            ), REQUEST_NEW_ADDRESS
        )
    }





    override fun onDeleteBtnClick(addRessItem: ManageAddressResponse.UserAddres?) {
        dialogDeleteItem(resources.getString(R.string.are_you_sure_you_want_to_delete), addRessItem?.id!!)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEW_ADDRESS && resultCode == RESULT_OK) {
            mAddAddressViewModel.hitManageAddressApi(sharedPreference.accessToken)
        }
    }


    private fun dialogDeleteItem(msg: String, addressId: Int) {
        var dialog = this?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_item_delete_address)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.tv_message.setText(msg)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.tv_no.setOnClickListener {

            dialog.dismiss()
        }
        dialog.tv_yes.setOnClickListener {
            this?.let {
                mAddAddressViewModel.hitDeletAddressApi(
                    sharedPreference.accessToken,
                    addressId = addressId.toString()!!
                )
            }
            dialog.dismiss()
        }
        val window = dialog.getWindow()
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }
}