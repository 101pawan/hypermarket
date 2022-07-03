package com.hypermarket_android.ui.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.internal.Utility
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.constant.NetworkConstants
import com.hypermarket_android.notification.MyAppsNotificationManager
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.ui.forgotPassword.ForgotPasswordActivity
import com.hypermarket_android.ui.register.RegisterActivity
import com.hypermarket_android.ui.verification.VerificationActivity
import com.hypermarket_android.util.ErrorUtils
import com.hypermarket_android.util.Helper
import com.hypermarket_android.util.showToast
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.hypermarket_android.activity.HomeDeleveryActivity
import com.hypermarket_android.ui.selectLanguage.SelectLanguageViewModel
import com.statusapp.socialLogin.GoogleLogin
import com.statusapp.socialLogin.GoogleLogin.Companion.RC_SIGN_IN
import com.statusapp.socialLogin.facebook.FacebookManager
import com.statusapp.socialLogin.facebook.OnFacebookLoginListener
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : BaseActivity(), View.OnClickListener, GoogleLogin.OnClientConnectedListener,
    OnFacebookLoginListener {
    private lateinit var mviewModel: LoginViewModel
    private lateinit var plusLogin: GoogleLogin
    private lateinit var fbManager: FacebookManager
    private lateinit var social_id: String
    private var social_image = ""
    private lateinit var social_name: String
    private lateinit var social_email: String
    private lateinit var login_type: String
    private lateinit var mviewModelSignUp: SocialLoginViewModel
    private lateinit var vmodel: SearchEmailViewModel
    private var progressBar: ProgressBar? = null
    var arrayListEmail: ArrayList<String> = ArrayList()
    var myAppsNotificationManager: MyAppsNotificationManager? = null
    var facebook: String? = null
    var first_name = ""
    var last_name = ""
    var country_code = ""
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var languageViewModel: SelectLanguageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_forgot_proceed.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        progressBar = findViewById(R.id.progressbar)
        /*  val textView = findViewById(R.id.tv_hyper_link) as TextView
          textView.setClickable(true)
          textView.setMovementMethod(LinkMovementMethod.getInstance())
          val text = "<a href='http://www.google.com'> Google </a>"
        //  HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
          textView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))*/
        initViews()
        initControl()
        myObserver()
        generateKeyTool()
        statusBackGroundColor()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    private fun generateKeyTool() {
        try {

            val info: PackageInfo = getPackageManager().getPackageInfo(
                "com.hypermarket_android",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e(
                    "maalik-key-hash  ",  Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
                /*Toast.makeText(
                    getApplicationContext(), Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    ), Toast.LENGTH_LONG
                ).show()*/

                Log.e("maalik-key-hash1", Base64.encodeToString(
                    md.digest(),
                    Base64.DEFAULT
                ) )

            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }


    override fun initViews() {
        languageViewModel = ViewModelProviders.of(this).get(SelectLanguageViewModel::class.java)
        mviewModelSignUp = ViewModelProviders.of(this).get(SocialLoginViewModel::class.java)
        mviewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        vmodel = ViewModelProviders.of(this).get(SearchEmailViewModel::class.java)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        plusLogin = GoogleLogin(this, null, this)
        plusLogin.mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL)

        fbManager = FacebookManager(this)
        fbManager.setOnLoginListener(this)

        country_code = sharedPreference.country_code.toString()
        sharedPreference.firstOpen = false


//        emailDialog()

        //getIntentData()
        // tv_country_code.setText(country_code)
    }


    override fun initControl() {
        tv_forgot_proceed.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)
        tv_login.setOnClickListener(this)
        tv_login_as_guest.setOnClickListener(this)
        tv_google.setOnClickListener(this)
        tv_facebook.setOnClickListener(this)
//        vmodel.getEmail(et_login_email.text.toString())
//
        et_login_email.threshold = 1

        et_login_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    arrayListEmail.clear()
                    vmodel.getEmail(et_login_email.text.toString())
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

        img_pass_visible.setOnClickListener {
            if (et_pass.transformationMethod
                    .equals(PasswordTransformationMethod.getInstance())
            ) {
                et_pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                img_pass_visible.setImageResource(R.drawable.eye_close)
            } else {
                et_pass.transformationMethod = PasswordTransformationMethod.getInstance()
                img_pass_visible.setImageResource(R.drawable.eye_open)
            }
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.tv_forgot_proceed -> {
                nextScreen()
            }
            R.id.tv_sign_up -> {
                RegisterScreen()
            }
            R.id.tv_login -> {
                if (progressBar!!.visibility == View.GONE) {
                    if (isValidField()) {
                        if (Helper.isNetworkAvailable(this)) {
                            progressBar!!.visibility = View.VISIBLE
                            myAppsNotificationManager = MyAppsNotificationManager.getInstance(applicationContext)
                            FirebaseMessaging.getInstance().isAutoInitEnabled
                            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                                if (!task.isComplete) {
                                    Log.i(getString(R.string.DEBUG_TAG), " Task Failed")
                                    return@OnCompleteListener
                                }
                                else{
                                    Log.d(
                                        getString(R.string.DEBUG_TAG), "firebase-token-normal-Login: " + task.result!!
                                            .token
                                    )
                                    var num = et_login_email.text.toString();
                                    if (num.contains("[0-9]".toRegex())){
                                        if (num[0] == '0'){
                                           num = num.drop(1)
                                        }
                                    }
                                    mviewModel.getLogin(
                                        num,
                                        country_code,
                                        task.result!!.token,
                                        "1",
                                        et_pass.text.toString()
                                    )
                                }


                                //Making an API call - Thread, Volley, okHttp, Retrofit


                            })








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

            R.id.tv_login_as_guest -> {
                sharedPreference.loginguest = true
                homeScreen()
            }
            R.id.tv_google -> {
                /*val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)*/
              //  plusLogin.signIn()
                if (Helper.isNetworkAvailable(this)) {
                    googleSignIn()
                }
                else {
                    Toast.makeText(
                        baseContext,
                        resources.getString(R.string.message_no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()

                }
               // googleSignIn()
            }
            R.id.tv_facebook -> {
                if (progressBar!!.visibility == View.GONE) {
                    if (Helper.isNetworkAvailable(this)) {
                        //  facebookLogin()
                        fbManager.doLogin()
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
    }
    private fun googleSignIn() {
        facebook = "google"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Context.CONTEXT_INCLUDE_CODE)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        val account = task?.getResult(ApiException::class.java)

        if (account != null) {
            social_name = account.displayName!!
            social_email = account.email!!
            social_id = account.id!!
            social_image = account.photoUrl.toString()
            Log.d("Pgoogle SignInResult",account.photoUrl.toString())

            if (!social_id.isNullOrEmpty())
                Log.e("google", social_id!!)


            if (social_email == null) {
              //  logoutDialoge()

            } else {
                social_email = account.email!!
                val SocialType = "2"
                login_type = NetworkConstants.LOGIN_TYPE_GOOGLE

                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isComplete) {
                        Log.i(getString(R.string.DEBUG_TAG), " Task Failed")
                        return@OnCompleteListener
                    } else {
                        Log.d(
                            getString(R.string.DEBUG_TAG),
                            "firebase-token-google-profile-fetch: " + task.result!!
                                .token
                        )
                        var language = ""
                        if (sharedPreference.language == "ar"){
                            language = "Arabic"
                        }else{
                            language = "English"
                        }
                        mviewModelSignUp.getSocialSignUp(
                            social_email,
                            task.result!!.token,
                            "1",
                            social_id,
                            login_type,
                            first_name,
                            social_image,
                            language
                        )
                    }
                    /*  mviewModelSignUp.getSocialSignUp(
                        social_email,
                        task.result.id!!,
                        "1",  // 1 for android
                        social_id,
                        login_type,
                        first_name,
                        social_image
                    )*/
                })
            }
        }
        googleSignInClient.signOut()
    }



    override fun onFacebookLogin(
        name: String,
        email: String,
        id: String,
        imageUrl: String,
        gender: String
    ) {
        progressBar!!.visibility = View.VISIBLE

        //  Log.d("email==",social_email.trim()+"6")

        social_id = id
        social_email = email
        social_name = name
        social_image = imageUrl
        login_type = NetworkConstants.LOGIN_TYPE_FACEBOOK


        var nameList = social_name.split(" ")
        if (nameList.size > 1) {
            first_name = nameList[0]
            last_name = nameList[nameList.size - 1]
        } else {
            first_name = nameList[0]
        }


//            Toast.makeText(this, social_email, Toast.LENGTH_LONG).show()

        // progressBar!!.visibility=View.VISIBLE






        myAppsNotificationManager = MyAppsNotificationManager.getInstance(applicationContext)
        FirebaseMessaging.getInstance().isAutoInitEnabled


        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isComplete) {
                Log.e(getString(R.string.DEBUG_TAG), " Task Failed")
                return@OnCompleteListener
            }
            else{
                Log.e(
                    getString(R.string.DEBUG_TAG), "firebase-token-facebook :  " + task.result!!
                        .token
                )
                var language = ""
                if (sharedPreference.language == "ar"){
                    language = "Arabic"
                }else{
                    language = "English"
                }
                mviewModelSignUp.getSocialSignUp(
                    social_email,
                    task.result!!.token,
                    "1",  // 1 for android
                    social_id,
                    login_type,
                    first_name,
                    social_image,
                    language
                )
            }


            //Making an API call - Thread, Volley, okHttp, Retrofit


        })







        /*  if (!Helper.isEmailValid(social_email)) {
              Log.d("email==", social_email)
              emailDialog()
              progressBar!!.visibility = View.GONE

          } else {
  //            Toast.makeText(this, social_email, Toast.LENGTH_LONG).show()

              // progressBar!!.visibility=View.VISIBLE
              mviewModelSignUp.getSocialSignUp(
                  social_email,
                  "kjkjj",
                  "1",
                  social_id,
                  login_type,
                  first_name,
                  social_image
              )
          }*/


    }

    override fun onFacebookError(message: String) {
        progressBar!!.visibility = View.GONE

        val errorCode = "190"
        if (message.contains(errorCode)) {
            LoginManager.getInstance().logOut()
            Toast.makeText(this, resources.getString(R.string.please_login_again), Toast.LENGTH_LONG).show()
        }
        showToast("Facebook login error")
    }

    override fun onGoogleProfileFetchComplete(
        id: String?,
        name: String?,
        email: String?,
        picURL: String,
        gender: String
    ) {

        social_id = id!!
        social_email = email!!
        social_name = name!!
        social_image = picURL
        login_type = NetworkConstants.LOGIN_TYPE_GOOGLE

        /*prefs.userSocialId = social_id
        prefs.userSocialtype = login_type*/

        val nameList = social_name.split(" ")
        if (nameList.size > 1) {
            first_name = nameList[0]
            last_name = nameList[nameList.size - 1]
        } else {
            first_name = nameList[0]
        }

        //ProgressDialogUtils.getInstance().showProgress(this, false)


        myAppsNotificationManager = MyAppsNotificationManager.getInstance(applicationContext)
        FirebaseMessaging.getInstance().isAutoInitEnabled


        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isComplete) {
                Log.i(getString(R.string.DEBUG_TAG), " Task Failed")
                return@OnCompleteListener
            }
            else{
                Log.d(
                    getString(R.string.DEBUG_TAG), "firebase-token-google-profile-fetch: " + task.result!!
                        .token
                )
                var language = ""
                if (sharedPreference.language == "ar"){
                    language = "Arabic"
                }else{
                    language = "English"
                }
                mviewModelSignUp.getSocialSignUp(
                    social_email, task.result!!.token, "1", social_id, login_type, first_name, social_image,language
                )
            }
            //Making an API call - Thread, Volley, okHttp, Retrofit
        })

    }


    private fun myObserver() {
        mviewModel.mResponseLogin.observe(this, Observer {
            progressBar!!.visibility = View.GONE
            if (it.user_data != null) {
                sharedPreference.loginStatus = true
                sharedPreference.userId = it.user_data.id.toString()
                sharedPreference.full_name = it.user_data.name.toString()
                sharedPreference.email = it.user_data.email.toString()
                sharedPreference.accessToken=it.user_data.access_token
                sharedPreference.deliveryBoy = it.user_data.is_delivery_boy
                if(it.user_data.referCode!=null){
                    sharedPreference.referralCode=it.user_data.referCode
                }else{
                    sharedPreference.referralCode="NA"
                }
                if (it.user_data.mobile_number != null) {
                    sharedPreference.mobile_no = it.user_data.mobile_number
                }
                if (it.user_data.image != null) {
                    sharedPreference.profile_image = it.user_data.image
                }
                if (it.user_data.otp_verified == 0) {
                    startActivity(Intent(this, VerificationActivity::class.java))
                } /*else if (it.user_data.is_register == 0) {
                    startActivity(Intent(this, RegisterActivity()::class.java))
                    finish()
                }*/ else {
                    progressBar!!.visibility = View.GONE
                    showToast(it.message)
                    //homeScreen()
                    var lang = ""
                    if (it.user_data.language == "Arabic"){
                        lang = "ar"
                    }else{
                        lang = "en"
                    }
                    if (sharedPreference.language == lang){
                        if (sharedPreference.deliveryBoy == "Yes"){
                            openDeliveryDialog()
                        }else{
                            homeScreen()
                        }
                    }else{
                        confirmationDialog(sharedPreference.language,it.user_data.language)
                    }

                }
            }
        })
        languageViewModel.mResponseLanguage.observe(this,{
            progressBar!!.visibility = View.GONE
            showToast(it.message)
            if (sharedPreference.deliveryBoy == "Yes"){
                openDeliveryDialog()
            }else{
                homeScreen()
            }
        })
        mviewModelSignUp.mResponseSocialSignUp.observe(this, Observer {
            progressBar!!.visibility = View.GONE
            if (it.user_data != null) {
                sharedPreference.userId = it.user_data.id.toString()
                showToast(it.message)
                sharedPreference.loginStatusSocial = true
                if(it.user_data.name != null){
                    sharedPreference.full_name = it.user_data.name.toString()
                }
                if(it.user_data.email != null){
                    sharedPreference.email = it.user_data.email.toString()
                }
                if(it.user_data.access_token != null){
                    sharedPreference.accessToken=it.user_data.access_token
                }
                //sharedPreference.mobile_no= it.user_data?.mobile_number.toString()
                if (it.user_data.image != null) {
                    sharedPreference.profile_image = it.user_data.image.toString()
                }
                startActivity(Intent(this, HomeActivity::class.java))
//
//                startActivity(Intent(this, ChoosePreferredStoreActivity::class.java))
            } else {
                progressBar!!.visibility == View.GONE
                if (it.message == "User not found.") {
                    emailDialog()
                }
                showToast(it.message)
            }
        })

        vmodel.mResponseSearchEmail.observe(this, Observer {

            if (it.data != null) {
                //  for (i in it.data) {
                arrayListEmail.add(it.data.email)
                //  }

                et_login_email.threshold = 1
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1, arrayListEmail
                )
                et_login_email.setAdapter(adapter)
            } else {
                arrayListEmail.add(" ")
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1, arrayListEmail
                )
                et_login_email.setAdapter(adapter)
            }
        })
        vmodel.mError.observe(this, Observer {
            progressBar!!.visibility = View.GONE

            ErrorUtils.handlerGeneralError(this, it)
        })

        mviewModel.mError.observe(this, Observer {
            ErrorUtils.handlerGeneralError(this, it)
            progressBar!!.visibility = View.GONE
        })

        mviewModelSignUp.mError.observe(this, Observer {
            progressBar!!.visibility = View.GONE
            ErrorUtils.handlerGeneralError(this, it)
        })
        languageViewModel.mError.observe(this,{
            progressBar!!.visibility = View.GONE
            ErrorUtils.handlerGeneralError(this, it)
        })


    }

    private fun nextScreen() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
        finish()
    }

    private fun RegisterScreen() {
        startActivity(Intent(this, RegisterActivity()::class.java))
        finish()
    }

    private fun homeScreen() {

       /* val intent = Intent(baseContext, ChoosePreferredStoreActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()*/


        val intent = Intent(baseContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun deliveryScreen(){
        val intent = Intent(baseContext, HomeDeleveryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    private fun isValidField(): Boolean {
        if (et_login_email.text?.trim().toString().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_your_registered_email_id_and_mobile_no))
            return false
        }
        if (et_pass.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_password))
            et_pass.requestFocus()
            return false
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     //   Log.e("RequestCode", requestCode.toString())
        if (requestCode == Context.CONTEXT_INCLUDE_CODE) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
                Toast.makeText(this, "Google SignIn Success", Toast.LENGTH_SHORT).show()
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, "Google SignIn"+e.toString(), Toast.LENGTH_SHORT).show()
                Log.e("RequestCode", e.toString())
            }

        }
        if (requestCode == RC_SIGN_IN) {
            plusLogin.onActivityResult(requestCode, resultCode, data!!)
        } else {
            fbManager.onActivityResult(requestCode, resultCode, data!!)
        }
    }


    override fun onClientFailed(msg: String?) {
        showToast("Google login error")
    }
    private fun openDeliveryDialog(){
        val dialog = Dialog(this@LoginActivity, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delivery_confirmation)
        dialog.setCancelable(false)
        dialog.show()
        val tvUser=dialog.findViewById<TextView>(R.id.tv_user)
        val tvDelivery=dialog.findViewById<TextView>(R.id.tv_delivery)
        tvDelivery.setOnClickListener {
            dialog.dismiss()
            sharedPreference.isDeliveryBoy = true
            deliveryScreen()
        }
        tvUser.setOnClickListener {
            dialog.dismiss()
            sharedPreference.isDeliveryBoy = false
            homeScreen()
        }
    }
    private fun confirmationDialog(clng:String,dlng:String){
        var lang = ""
        if (dlng == "Arabic"){
            lang = "ar"
        }else{
            lang = "en"
        }
        val dialog = Dialog(this@LoginActivity, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_language_confirm)
        dialog.setCancelable(false)
        dialog.show()
        val tvcurrent=dialog.findViewById<TextView>(R.id.tv_current)
        val tvdefault=dialog.findViewById<TextView>(R.id.tv_default)
        tvdefault.setOnClickListener {
            if (Helper.isNetworkAvailable(this@LoginActivity)){
                dialog.dismiss()
                sharedPreference.language = lang
                setLocale(lang)
                if (sharedPreference.deliveryBoy == "Yes"){
                    openDeliveryDialog()
                }else{
                    homeScreen()
                }
            }else{
                Toast.makeText(this@LoginActivity,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
            }

        }
        tvcurrent.setOnClickListener {
            dialog.dismiss()
            var cLang = ""
            if (clng == "ar"){
                cLang = "Arabic"
            }else{
                cLang = "English"
            }
            if (Helper.isNetworkAvailable(this@LoginActivity)){
                dialog.dismiss()
                progressBar!!.visibility = View.VISIBLE
                languageViewModel.updateLanguage(
                    sharedPreference.accessToken,
                    sharedPreference.userId,
                    cLang

                )
            }else{
                Toast.makeText(this@LoginActivity,resources.getString(R.string.message_no_internet_connection),Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    private fun emailDialog() {
        val dialog = Dialog(this@LoginActivity, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_dialog_email)
        dialog.setCancelable(true)
        val etEmail = dialog.findViewById<EditText>(R.id.et_email_id)
        val btnEmailSumit = dialog.findViewById<TextView>(R.id.btn_email_submit)

        btnEmailSumit.setOnClickListener {

            if (!etEmail.text.isBlank()) {
                if (Helper.isEmailValid(etEmail.text.toString())) {
                    Helper.hideSoftKeyboard(this)
                    social_email = etEmail.text.toString().trim()
                    progressBar!!.visibility = View.VISIBLE

                    myAppsNotificationManager = MyAppsNotificationManager.getInstance(applicationContext)
                    FirebaseMessaging.getInstance().isAutoInitEnabled
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isComplete) {
                            Log.i(getString(R.string.DEBUG_TAG), " Task Failed")
                            return@OnCompleteListener
                        }
                        else{
                            Log.d(
                                getString(R.string.DEBUG_TAG), "firebase-token-email-dialog " + task.result!!
                                    .token
                            )
                            var language = ""
                            if (sharedPreference.language == "ar"){
                                language = "Arabic"
                            }else{
                                language = "English"
                            }
                            mviewModelSignUp.getSocialSignUp(
                                social_email,
                                task.result!!.token,
                                "1",
                                social_id,
                                login_type,
                                first_name,
                                social_image,
                                language
                            )
                        }
                        //Making an API call - Thread, Volley, okHttp, Retrofit
                    })

                    dialog.hide()

                } else {
                    Toast.makeText(baseContext, resources.getString(R.string.please_enter_valid_emailid), Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(baseContext, resources.getString(R.string.please_enter_emailid), Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

    }

}