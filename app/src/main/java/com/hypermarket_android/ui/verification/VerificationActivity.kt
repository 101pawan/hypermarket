package com.hypermarket_android.ui.verification

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.personalDetailActivity.PersonalDetailActivity
import com.hypermarket_android.ui.resetPassword.ResetPasswordActivity
import com.hypermarket_android.util.ErrorUtils
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.showToast
import kotlinx.android.synthetic.main.activity_verification.*


class VerificationActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mviewModel: OtpViewModel
    var key: String? = null
    lateinit var timer: CountDownTimer
    private var progressBar: ProgressBar? = null
    private var toast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        progressBar = findViewById(R.id.progressbar)
        initViews()
        initControl()
        myObserver()
        statusBackGroundColor()
    }


    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(OtpViewModel::class.java)
        timer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                // time--
                tv_resend_otp.visibility = View.GONE
                val time1 = millisUntilFinished / 1000
                //makeMeTwoDigits(time, millisUntilFinished)
                makeMeTwoDigits(time1)
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                tv_resend_otp.visibility = View.VISIBLE
                toast = Toast.makeText(this@VerificationActivity, "OTP Expired", Toast.LENGTH_SHORT)
                toast!!.show()


            }

        }.start()


        // getIntentData()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }

    fun makeMeTwoDigits(n: Long) {

        //  tvTimer.setText("0: ${n} ")

        if (n.toString().length < 2) {
            tv_time.setText("00:" + "0$n" + "sec")
        } else {
            tv_time.setText("00:" + "$n" + "sec")
        }
    }

    private fun getIntentData() {
        if (intent.hasExtra("forgot_password")) {
            key = intent.getStringExtra("1")
        }
    }

    private fun myObserver() {
        mviewModel.mResponseOtp.observe(this, androidx.lifecycle.Observer {
            progressbar.visibility = View.GONE

            if (it.message != null) {
                if (sharedPreference.forgot.equals("1")) {
                    showToast(it.message)
                    if (toast !== null) {
                        toast!!.cancel()
                    }
                    timer.cancel()
                    startActivity(Intent(this, ResetPasswordActivity::class.java))
                    finish()
                } else {
                    showToast(it.message)
                    nextScreen()
                }
            }

        })

        mviewModel.mResponseResendOtp.observe(this, Observer {
            if (it.user_data != null) {
                //prefs.userOtp = it.data!!.otp
                showToast(it.message!!)
                timer = object : CountDownTimer(60000, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        // time--

                        tv_resend_otp.visibility = View.GONE

                        val time1 = millisUntilFinished / 1000

                        //makeMeTwoDigits(time, millisUntilFinished)
                        makeMeTwoDigits(time1)


                        //here you can have your logic to set text to edittext
                    }

                    override fun onFinish() {
                        tv_resend_otp.visibility = View.VISIBLE

                    }

                }.start()

            } else {
                showToast(it.message!!)
            }
        })

        mviewModel.mError.observe(this, androidx.lifecycle.Observer {
            progressbar.visibility = View.GONE

            ErrorUtils.handlerGeneralError(this, it)

        })
    }

    override fun initControl() {
        img_back.setOnClickListener(this)
        tv_verify.setOnClickListener(this)
        tv_resend_otp.setOnClickListener(this)

        et1_otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et2_otp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        et2_otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et3_otp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        et3_otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et4_otp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        et4_otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et4_otp.clearFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_verify -> {
                if (progressBar!!.visibility == View.GONE) {
                    var userId = sharedPreference.userId
                    Log.d("userId==", userId)
                    if (otpValid()) {
                        if (Helper.isNetworkAvailable(this)) {
                            if (tv_resend_otp.visibility == View.GONE) {
                                Helper.hideSoftKeyboard(this)
                                progressbar.visibility = View.VISIBLE
                                mviewModel.getOtp(
                                    userId,
                                    et1_otp.text.toString() + et2_otp.text.toString() + et3_otp.text.toString() + et4_otp.text.toString()
                                )

                            } else {
                                Toast.makeText(baseContext, "OTP expired.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

            R.id.img_back -> {
                onBackPressed()
            }

            R.id.tv_resend_otp -> {
                tv_resend_otp.visibility = View.GONE
                mviewModel.getResendOtp(sharedPreference.userId)
            }
        }

    }


    private fun otpValid(): Boolean {
        if (et1_otp!!.text.toString().isEmpty()) {
            showToast("Please Enter OTP")
            et1_otp!!.requestFocus()
            return false
        }
        if (et2_otp!!.text.toString().isEmpty()) {
            showToast("Please Enter OTP")
            et2_otp!!.requestFocus()
            return false
        }
        if (et3_otp!!.text.toString().isEmpty()) {
            showToast("Please Enter OTP")
            et3_otp!!.requestFocus()
            return false
        }
        if (et4_otp!!.text.toString().isEmpty()) {
            showToast("Please Enter OTP")
            et4_otp!!.requestFocus()
            return false
        }
        return true
    }

    private fun nextScreen() {
        timer.cancel()
        startActivity(Intent(this, PersonalDetailActivity::class.java))
        finish()
    }
}