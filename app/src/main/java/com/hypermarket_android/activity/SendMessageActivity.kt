package com.hypermarket_android.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.*
import com.hypermarket_android.viewModel.SendMessageViewModel
import kotlinx.android.synthetic.main.activity_send_message.*

class SendMessageActivity : BaseActivity() {
    private lateinit var sendMessageViewModel: SendMessageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)
        sendMessageViewModel = ViewModelProvider(this).get(SendMessageViewModel::class.java)
        iv_actionbar_back.setOnClickListener { onBackPressed() }

        sendMessageObservable()


        tv_send_message.setOnClickListener {
            val isFormValid = validateSendMessageForm(
                et_full_name_send_msg.text.toString(),
                et_mobile_number_send_message.text.toString(),
                et_email_id_send_msg.text.toString(),
                et_msg_is_about.text.toString(),
                et_write_msg.text.toString()
            )

            if (isFormValid) {
                if (Helper.isNetworkAvailable(this!!)) {
                    sendMessageViewModel.hitSendMessageApi(
                        sharedPreference.accessToken,
                        et_full_name_send_msg.text.toString(),
                        ccd_send_message.selectedCountryCodeWithPlus.toString(),
                        et_mobile_number_send_message.text.toString(),
                        et_email_id_send_msg.text.toString(),
                        et_msg_is_about.text.toString(),
                        et_write_msg.text.toString()
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

    private fun validateSendMessageForm(
        fullName: String,
        mobileNo: String,
        emailId: String,
        msg: String,
        writeMsg: String
    ): Boolean {
        if (TextUtils.isEmpty(fullName)) {
            showToast(resources.getString(R.string.please_enter_full_name))
            return false
        }
        if (TextUtils.isEmpty(mobileNo)) {
            showToast(resources.getString(R.string.please_enter_mobile_number))
            return false
        }
        if (TextUtils.isEmpty(emailId)) {
            showToast(resources.getString(R.string.please_enter_emailid))
            return false
        }

        if (!emailId.isValidEmail) {
            showToast(resources.getString(R.string.please_enter_valid_emailid))
            return false
        }




        if (TextUtils.isEmpty(msg)) {
            showToast(resources.getString(R.string.please_enter_what_message_is_all_about))
            return false
        }

        if (TextUtils.isEmpty(writeMsg)) {
            showToast(resources.getString(R.string.please_enter_detailed_message))
            return false
        }

        return true
    }


    private fun sendMessageObservable() {
        sendMessageViewModel.sendMessageResponse.observe(this, Observer {
            showToast(it.message)
            finish()
        })
        sendMessageViewModel.errorSendMessage.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
//            showToast(it.message?:"")
            ErrorUtil.handlerGeneralError(this, it)

        })

        sendMessageViewModel.mProgessSendMessage.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }

    override fun initViews() {
    }

    override fun initControl() {
    }
}