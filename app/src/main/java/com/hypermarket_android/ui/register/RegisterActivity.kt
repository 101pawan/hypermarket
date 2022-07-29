package com.hypermarket_android.ui.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.constant.NetworkConstants
import com.hypermarket_android.notification.MyAppsNotificationManager
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.verification.VerificationActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.hypermarket_android.eventPojos.HomePageReloadEventPojo
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.tv_login
import org.greenrobot.eventbus.EventBus

class RegisterActivity : BaseActivity(), View.OnClickListener {
    // var list_of_items = arrayOf("Select Country","Oman", "UAE","India")
    var email: String = ""
    var password = ""
    var full_name = ""
    var mobile_no = ""
    var country_code = ""
    var country_name = ""
    private lateinit var mviewModel: RegisterViewModel

    private var progressBar: ProgressBar? = null
    // private val mCustomProgressDialog: CustomProgressDialog? = null
    var myAppsNotificationManager: MyAppsNotificationManager? = null
//    var list_of_items = arrayOf("Select Country","Oman", "UAE","India")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressBar = findViewById(R.id.progressbar)

        initViews()
        initControl()
        myObserver()
        statusBackGroundColor()
    }

//    private fun getIntentData() {
//        if (intent.hasExtra("country_code")){
//            country_code = intent.getStringExtra("country_code").toString()
//        }
//
//    }


    private fun myObserver() {
        mviewModel.mResponseRegister.observe(this, androidx.lifecycle.Observer {
            progressBar!!.visibility = View.GONE
            if (it.user_data != null) {
                showToast(it.message)
                sharedPreference.userId = it.user_data.id.toString()
                sharedPreference.referralCode=it.user_data.referCode
//               full_name = it.user_data.name
//               mobile_no = it.user_data.mobile_number
//               email = it.user_data.email
                sharedPreference.full_name = it.user_data.name.toString()
                sharedPreference.email = it.user_data.email.toString()
                sharedPreference.mobile_no = it.user_data.mobile_number.toString()
                sharedPreference.forgot = it.user_data.is_register.toString()
//                sharedPreference.accessToken = it.user_data.access_token
                nextScreen()
            }
        })

        mviewModel.mError.observe(this, androidx.lifecycle.Observer {
            progressBar!!.visibility = View.GONE
            ErrorUtils.handlerGeneralError(this, it)

        })
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
//        val aa = ArrayAdapter(this, R.layout.spinner_item, list_of_items)
//        aa.setDropDownViewResource(R.layout.spinner_item)
        // Set Adapter to Spinner
//        spinner_country2!!.setAdapter(aa)
        // getIntentData()
        country_code = sharedPreference.country_code

        tv_country_code_reg.setText(country_code + "  -")
//        if (country_code.equals("+968")) {
//            country_name = "Oman"
//        } else
            if (country_code.equals("+971")) {
            country_name = "UAE"
        }
        tv_country_code_reg.setOnClickListener(View.OnClickListener {
            countryDialog()
        })
        // et_reg_mobile_no.setText(country_code)

    }
    @SuppressLint("ResourceAsColor")
    private fun countryDialog() {

        val dialog = Dialog(this, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_dialog_select_country)
        dialog.setCancelable(true)
        dialog.show()

        val llCountryDubai = dialog.findViewById<LinearLayout>(R.id.ll_dubai)
        val tvCountryDubai = dialog.findViewById<TextView>(R.id.tv_country_uae)
        val llCountryOman = dialog.findViewById<LinearLayout>(R.id.ll_oman)
        val tvCountryOman = dialog.findViewById<TextView>(R.id.tv_country_oman)
        val tvSubmit = dialog.findViewById<TextView>(R.id.tv_submit)

        var selectedCountry = ""




        if (sharedPreference.country == "United Arab Emirates") {

            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryDubai.setTextColor(Color.parseColor("#18A44E"));
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryOman.setTextColor(Color.parseColor("#999999"));


        } else if (sharedPreference.country == "Oman") {
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryOman.setTextColor(Color.parseColor("#18A44E"));

            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryDubai.setTextColor(Color.parseColor("#999999"));
        }

        llCountryDubai.setOnClickListener {
            selectedCountry = "United Arab Emirates"
            sharedPreference.country_code = "+971"
            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryDubai.setTextColor(Color.parseColor("#18A44E"));
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryOman.setTextColor(Color.parseColor("#999999"));

        }

        llCountryOman.setOnClickListener {
            selectedCountry = "Oman"
            sharedPreference.country_code = "+968"
            llCountryOman.background = resources.getDrawable(R.drawable.country_background_green)
            tvCountryOman.setTextColor(Color.parseColor("#18A44E"));
            llCountryDubai.background = resources.getDrawable(R.drawable.country_background_grey)
            tvCountryDubai.setTextColor(Color.parseColor("#999999"));
        }
        tvSubmit.setOnClickListener {
            sharedPreference.country = selectedCountry
            country_code = sharedPreference.country_code
            tv_country_code_reg.setText(country_code + "  -")
            if (country_code.equals("+968")) {
                country_name = "Oman"
            } else if (country_code.equals("+971")) {
                country_name = "UAE"
            }
            dialog.dismiss()
        }

    }


    override fun initControl() {
        tv_login.setOnClickListener(this)
        tv_reg_submit.setOnClickListener(this)
//        spinner_country2!!.setOnItemSelectedListener(this)
        img_regtrpass_visible.setOnClickListener(this)
        img_confrmpass_visible.setOnClickListener(this)
        et_reg_fullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length > 35) {
                    et_reg_fullname.requestFocus()
                    showToast(resources.getString(R.string.full_name_character_limit_exceeded))

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

        et_reg_mobile_no.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length > 12) {
                    et_reg_mobile_no.requestFocus()
                    showToast(resources.getString(R.string.mobile_number_should_not_exceed_characters_long))

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
            R.id.tv_reg_submit -> {
                if (progressBar!!.visibility == View.GONE) {
                    if (isValid()) {

                        if (Helper.isNetworkAvailable(this)) {
                            Helper.hideSoftKeyboard(this)
                            progressBar!!.visibility = View.VISIBLE
                            myAppsNotificationManager = MyAppsNotificationManager.getInstance(applicationContext)
                            FirebaseMessaging.getInstance().isAutoInitEnabled
                            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isComplete) {
                                    Log.i(getString(R.string.DEBUG_TAG), " Task Failed")
                                    return@OnCompleteListener
                                }
                                else{
                                    Log.d(
                                        getString(R.string.DEBUG_TAG), "firebase-token-register: " + task.result!!.token
                                    )
                                    var language = ""
                                    if (sharedPreference.language == "ar"){
                                        language = "Arabic"
                                    }else{
                                        language = "English"
                                    }
                                    mviewModel.getRegister(
                                        et_reg_fullname.text.toString(), et_reg_mobile_no.text.toString(),
                                        et_reg_email.text.toString(), country_name,
                                        et_regtr_pass.text.toString(),
                                        country_code, task.result!!.token, "1",
                                        language
                                    )
                                }


                                //Making an API call - Thread, Volley, okHttp, Retrofit


                            })
                        }else {
                            Toast.makeText(
                                baseContext,
                                resources.getString(R.string.message_no_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            R.id.tv_login -> {
                loginScreen()
            }
            R.id.img_regtrpass_visible -> {
                if (et_regtr_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    et_regtr_pass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    img_regtrpass_visible.setImageResource(R.drawable.eye_close)
                } else {
                    et_regtr_pass.transformationMethod = PasswordTransformationMethod.getInstance()
                    img_regtrpass_visible.setImageResource(R.drawable.eye_open)
                }
            }
            R.id.img_confrmpass_visible -> {
                if (et_regtr_confrmpass.getTransformationMethod().equals(
                        PasswordTransformationMethod.getInstance()
                    )
                ) {
                    et_regtr_confrmpass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    img_confrmpass_visible.setImageResource(R.drawable.eye_close)
                } else {
                    et_regtr_confrmpass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    img_confrmpass_visible.setImageResource(R.drawable.eye_open)
                }
            }
        }

    }


    private fun nextScreen() {
        startActivity(Intent(this, VerificationActivity::class.java))
        //startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun loginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    fun isValid(): Boolean {
        if (et_reg_fullname.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_full_name))
            et_reg_fullname.requestFocus()
            return false
        }
        if (et_reg_fullname.text.toString().length < 3) {
            showToast(resources.getString(R.string.First_name_should_atleast_of_3_character))
            et_reg_fullname.requestFocus()
            return false
        }

        if (et_reg_fullname.text.toString().length > 35) {
            showToast(resources.getString(R.string.full_name_character_limit_exceeded))
            et_reg_fullname.requestFocus()
            return false
        }

        if (et_reg_mobile_no.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_mobile_number))
            et_reg_mobile_no.requestFocus()
            return false
        }

        if (et_reg_mobile_no.text.toString().trim().length < 6) {
            showToast(resources.getString(R.string.please_enter_your_valid_mobile_number))
            et_reg_mobile_no.requestFocus()
            return false
        }

        if (et_reg_mobile_no.text.toString().length > 12) {
            showToast(resources.getString(R.string.mobile_number_should_not_exceed_characters_long))
            et_reg_mobile_no.requestFocus()
            return false
        }
        if (et_reg_mobile_no.text.toString().startsWith("0")) {
            showToast(resources.getString(R.string.mob_bet))
            et_reg_mobile_no.requestFocus()
            return false
        }

        if (et_reg_email.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_emailid))
            et_reg_email.requestFocus()
            return false
        }
        if (!et_reg_email.text.toString().trim().matches(NetworkConstants.EMAIL_PATTERN.toRegex())) {
            showToast(resources.getString(R.string.please_enter_valid_emailid))
            et_reg_email.requestFocus()
            return false
        }


        /*   if (et_ship_address.text.toString().trim().isEmpty()) {
               showToast("Please enter your shipping address")
               et_ship_address.requestFocus()
               return false
           }*/

        if (et_regtr_pass.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_password))
            et_regtr_pass.requestFocus(et_regtr_pass.length())
            return false
        }

        if (!et_regtr_pass.text.toString().isValidPassword) {
            showToast(resources.getString(R.string.password_should_have_8_character))
            et_regtr_pass.requestFocus(et_regtr_pass.length())
            return false
        }
        if (et_regtr_confrmpass.text.toString().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_confirm_password))
            et_regtr_confrmpass.requestFocus()
            return false
        }
        if (!et_regtr_confrmpass.text.toString().equals(et_regtr_pass.text.toString())) {
            showToast(resources.getString(R.string.password_and_confirm_password_must_be_same))
            et_regtr_confrmpass.requestFocus(et_regtr_confrmpass.length())
            return false
        }
        return true
    }

}