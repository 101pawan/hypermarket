package com.hypermarket_android.ui.selectLanguage

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProviders
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.login.LoginViewModel
import com.hypermarket_android.ui.verification.VerificationActivity
import com.hypermarket_android.util.ApiInterface
import com.hypermarket_android.util.ErrorUtils
import com.hypermarket_android.util.RetrofitUtil
import com.hypermarket_android.util.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_select_language2.*
import java.util.*

class SelectLanguage : BaseActivity() {
    private var languageType: String = "en"
    private lateinit var mviewModel: SelectLanguageViewModel
    private var progressBar: ProgressBar? = null
    val apiInterface: ApiInterface by lazy {
        RetrofitUtil.apiService()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language2)
        progressBar = findViewById(R.id.progressbar)

        initViews()
        initControl()
        getObserver()

    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(SelectLanguageViewModel::class.java)
        btn_change_language.setOnClickListener {
            if(sharedPreference.language == "en"){

                setLocale("en")
                onBackPressed()

            }else if (sharedPreference.language == "ar"){

                setLocale("ar")
                onBackPressed()
            }else{
                setLocale("en")
                onBackPressed()
            }
        }

        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initControl() {
        btn_change_language.setOnClickListener(View.OnClickListener {
            progressBar!!.visibility = View.VISIBLE
            if (sharedPreference.userId != "" && sharedPreference.userId.toInt() > 0) {
                if (languageType == resources.getString(R.string.english_by_default)) {
                    mviewModel.updateLanguage(
                        sharedPreference.accessToken,
                        sharedPreference.userId,
                        "English"
                    )

                } else {
                    mviewModel.updateLanguage(
                        sharedPreference.accessToken,
                        sharedPreference.userId,
                        "Arabic"
                    )
                }
            }else{
                if(languageType == resources.getString(R.string.english_by_default)){
                    sharedPreference.language = "en"
                    setLocale("en")
                }else{
                    sharedPreference.language = "ar"
                    setLocale("ar")
                }
                startActivity(Intent(this,HomeActivity::class.java))
                finishAffinity()
            }
        })
        rgLanguageType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                var selectedId = p1
                println("TANU==" + p1)
                var radioButton: RadioButton = findViewById(selectedId)
                languageType = radioButton.text.toString()


            }
        })
    }
    fun getObserver(){
        mviewModel.mResponseLanguage.observe(this, androidx.lifecycle.Observer {
            progressBar!!.visibility = View.GONE
            if (it.success == 200) {
                showToast(it.message)
                if(languageType == resources.getString(R.string.english_by_default)){
                    sharedPreference.language = "en"
                    setLocale("en")
                }else{
                    sharedPreference.language = "ar"
                    setLocale("ar")
                }
                startActivity(Intent(this,HomeActivity::class.java))
                finishAffinity()
            }else{
              showToast(it.message)
            }
        })
        mviewModel.mError.observe(this, androidx.lifecycle.Observer {
            progressBar!!.visibility = View.GONE
            ErrorUtils.handlerGeneralError(this, it)
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()

        })

    }

    //Set Locale
    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}


