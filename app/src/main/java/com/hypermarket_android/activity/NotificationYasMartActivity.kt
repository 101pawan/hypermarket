package com.hypermarket_android.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hypermarket_android.Adapter.NotificationAdapter
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.viewModel.NotificationViewModel
import kotlinx.android.synthetic.main.notification_activity_yasmart.*

class NotificationYasMartActivity : BaseActivity() {
    var mNotificationAdapter : NotificationAdapter? = null

    private lateinit var mNotificationViewModel: NotificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.notification_activity_yasmart)
        mNotificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        mNotificationAdapter = NotificationAdapter(this)
         rv_notification.layoutManager = LinearLayoutManager(this)
         rv_notification.adapter = mNotificationAdapter
          iv_actionbar_back.setOnClickListener {
             onBackPressed()
         }
        notificationObservable()
        if (Helper.isNetworkAvailable(this)) {
            mNotificationViewModel.hitNotificationApi(sharedPreference.accessToken)

        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.message_no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun notificationObservable() {

        mNotificationViewModel.notificationResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            mNotificationAdapter?.setData(it.result)
            if (it.result.isEmpty()){
              rv_notification.visibility = View.GONE
              no_data.visibility = View.VISIBLE
            }else{
                rv_notification.visibility = View.VISIBLE
                no_data.visibility = View.GONE

            }


        })
        mNotificationViewModel.errorNotification.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        mNotificationViewModel.mProgessNotification.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })


    }


    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun initControl() {
        TODO("Not yet implemented")
    }





}