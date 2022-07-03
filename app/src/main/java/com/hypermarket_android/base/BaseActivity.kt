package com.hypermarket_android.base

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hypermarket_android.R
import com.hypermarket_android.util.SharedPreferenceUtil

abstract class BaseActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = SharedPreferenceUtil.getInstance(this)
        backGroundColor()
    }

    abstract fun initViews()
    abstract fun initControl()

    private var doubleBackToExitPressedOnce: Boolean = false
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 -> super.onBackPressed()
            isTaskRoot -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.back_press_exit_line), Toast.LENGTH_SHORT)
                    .show()
//                Handler().postDelayed(
//                    { doubleBackToExitPressedOnce = false },
//                    BACK_PRESS_TIME_INTERVAL
//                )
            }
            else -> super.onBackPressed()
        }
    }

}