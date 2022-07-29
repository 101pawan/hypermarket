package com.hypermarket_android.ui.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.SendMessageViewModel
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_send_message.*

class FeedbackActivity : BaseActivity(),View.OnClickListener {
    private lateinit var feedbackViewModel: FeedbackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        feedbackViewModel = ViewModelProvider(this).get(FeedbackViewModel::class.java)
        initViews()
        initControl()

        feedbackObservable()
    }

    override fun initViews() {

    }

    override fun initControl() {
        iv_feedback_back.setOnClickListener(this)
        btn_feedback_submit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      when(p0?.id){
          R.id.iv_feedback_back ->{
              onBackPressed()
          }

          R.id.btn_feedback_submit -> {
              if (Helper.isNetworkAvailable(this!!)) {
                  feedbackViewModel.hitFeedbackApi(
                      sharedPreference.accessToken,
                      edt_feedback_text.text.toString()
                  )
              }
              else {
                  Toast.makeText(
                      this,
                      resources.getString(R.string.message_no_internet_connection),
                      Toast.LENGTH_SHORT
                  ).show()

              }
          }
      }
    }

    private fun feedbackObservable() {
        feedbackViewModel.feedbackResponse.observe(this, Observer {
            showToast(it.message)
            finish()
        })
        feedbackViewModel.errorFeedback.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
//            showToast(it.message?:"")
            ErrorUtil.handlerGeneralError(this, it)

        })

        feedbackViewModel.mProgessFeedback.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }
}