package com.hypermarket_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web.*

import java.util.*

open class WebActivity : BaseActivity(),View.OnClickListener {

    private lateinit var pageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        if (intent != null) {
            title = intent.getStringExtra("titlename")
            pageUrl = intent.getStringExtra("url")!!
        }
        initViews()
    }
    override fun initViews() {
        iv_web_back.setOnClickListener(this)
        tv_web_header.setText(title)
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(pageUrl)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        progressBar_web.visibility = View.VISIBLE
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url!!)
                Log.i("URLHERE",url)
             //   Toast.makeText(this@WebActivity, "URL!"+url, Toast.LENGTH_SHORT).show()
                val statusorder = "success_url"
                if (url!!.indexOf(statusorder) > -1) {
                    Log.i("URLSuccess",statusorder)
                  //  Toast.makeText(this@WebActivity, "URLSuccess!"+statusorder, Toast.LENGTH_SHORT).show()
                }
                if (url!!.indexOf("decline_url") > -1) {

                }
                if (url!!.indexOf("cancel_url") > -1) {

                }
                return false
            }
            override fun onPageFinished(
                view: WebView?,
                url: String?
            ) {
                super.onPageFinished(view, url)
                progressBar_web.visibility = View.GONE
            }
        })

    }
    override fun initControl() {

    }
    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_web_back -> {
                onBackPressed()
            }
        }
    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, fragment!!)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }

}