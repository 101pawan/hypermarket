package com.hypermarket_android.base

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hypermarket_android.R
import com.hypermarket_android.util.SharedPreferenceUtil

abstract class BaseFragment : Fragment() {

     val sharedPreferenceUtil: SharedPreferenceUtil by lazy {
        SharedPreferenceUtil.getInstance(this.requireActivity())

    }

    lateinit var prefs1: SharedPreferences

    val prefs: SharedPreferenceUtil by lazy {

        SharedPreferenceUtil.getInstance(context!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(requireActivity())
//    }

    abstract fun initViews()
    abstract fun initControl()
}