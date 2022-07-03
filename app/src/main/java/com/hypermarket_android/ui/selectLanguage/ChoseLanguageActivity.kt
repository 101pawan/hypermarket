package com.hypermarket_android.ui.selectLanguage

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_select_language2.*
import java.util.*

class ChoseLanguageActivity : BaseActivity() {
    private var languageType: String = "en"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chose_language)
        initViews()
        initControl()

    }

    override fun initViews() {
        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initControl() {
        btn_change_language.setOnClickListener(View.OnClickListener {
            if(languageType == resources.getString(R.string.arabic)){
                sharedPreference.language = "ar"
                setLocale("ar")
            }else{
                sharedPreference.language = "en"
                setLocale("en")
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()

        })
        with(rgLanguageType, {
            rgLanguageType.setOnCheckedChangeListener { _, p1 ->
                var selectedId = p1
                println("TANU==" + p1)
                var radioButton: RadioButton = findViewById(selectedId)
                languageType = radioButton.text.toString()
            }
        })
    }
    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}