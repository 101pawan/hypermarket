package com.hypermarket_android.Fragment

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.*
import com.hypermarket_android.base.BaseFragment
import com.hypermarket_android.eventPojos.SelectCartEventPojo
import com.hypermarket_android.ui.editProfile.EditProfileActivity
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.mypoints.MyPointsActivity
import com.hypermarket_android.ui.setting.LogOutViewModel
import com.facebook.login.LoginManager
import com.hypermarket_android.ui.selectLanguage.SelectLanguage
import com.hypermarket_android.util.LocaleHelper
import com.hypermarket_android.util.SharedPreferenceUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialogue_sign_out.*

import kotlinx.android.synthetic.main.fragment_setting.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class SettingFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mviewModel: LogOutViewModel
    private lateinit var sharedPreferences: SharedPreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = SharedPreferenceUtil.getInstance(requireActivity())
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(LogOutViewModel::class.java)
        if (prefs.loginguest == true) {
            img_settings_profile.setImageResource(R.drawable.placeholder_img)
        } else if (prefs.loginStatusSocial == true) {
            if (prefs.profile_image != null && !prefs.profile_image.isEmpty()) {
                Log.e("initviewtrue","initviewtrue")
                Glide.with(this).load(sharedPreferences.profile_image).placeholder(R.drawable.profile_dummy_img).thumbnail(0.1f).into(img_settings_profile)

//                Picasso.get()
//                    .load(prefs.profile_image)
//                    .placeholder(R.drawable.profile_dummy_img)
//                    .fit()
//                    .into(img_settings_profile)
            } else {
                img_settings_profile.setImageResource(R.drawable.placeholder_img)
            }
            tv_settings_name.text = sharedPreferences.full_name
            tv_settings_email.text = sharedPreferences.email
        } else {
            if (prefs.profile_image != null && !prefs.profile_image.isEmpty()) {
                Log.e("initviewnull","initviewnull")
                Glide.with(this).load(sharedPreferences.profile_image).placeholder(R.drawable.profile_dummy_img).thumbnail(0.1f).into(img_settings_profile)

//                Picasso.get()
//                    .load(prefs.profile_image)
//                    .placeholder(R.drawable.profile_dummy_img)
//                    .fit()
//                    .into(img_settings_profile)
                tv_settings_name.text = sharedPreferences.full_name
                tv_settings_mobile_number.text = sharedPreferences.mobile_no
                tv_settings_email.text = sharedPreferences.email
            } else {
                img_settings_profile.setImageResource(R.drawable.placeholder_img)
                tv_settings_name.text = sharedPreferences.full_name
                tv_settings_mobile_number.text = sharedPreferences.mobile_no
                tv_settings_email.text = sharedPreferences.email
            }
        }
        if(prefs.language != null){
            if(prefs.language == "en"){
                setLocale("en")
                LocaleHelper.setLocale(requireContext(), "en")
            } else if(prefs.language == "ar"){
                setLocale("ar")
                LocaleHelper.setLocale(requireContext(), "ar")
            }else{
                setLocale("en")
                LocaleHelper.setLocale(requireContext(), "en")
            }
        }
    }

    override fun initControl() {
        ln_log_out.setOnClickListener(this)
        profile_edit.setOnClickListener(this)
        ll_wish_list.setOnClickListener(this)
        ll_coupons.setOnClickListener(this)
        ll_cart.setOnClickListener(this)
        ll_my_orders.setOnClickListener(this)
        ll_manage_address.setOnClickListener(this)
        ll_support.setOnClickListener(this)
        linearMyPoints.setOnClickListener(this)
        ll_change_language.setOnClickListener(this)
        ll_settings.setOnClickListener(this)
        ll_dowloaded_invoice.setOnClickListener(this)
        ll_wallet.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initControl()
        //  myObserver()
    }



//    private fun myObserver() {
//        mviewModel.mResponseLogOut.observe(activity, Observer {
//
//
//        })
//
//        mviewModel.mError.observe(this, Observer {
//            ErrorUtils.handlerGeneralError(this,it)
//        })
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.linearMyPoints -> {
                activity?.let {
                    val intent = Intent(it, MyPointsActivity::class.java)
                    it.startActivity(intent)
                }
            }
             R.id.ln_log_out -> {
                // mviewModel.getLogOut("kjkjkjk")
                dialogSignout()
            }
            R.id.profile_edit -> {
                activity?.let {
                    val intent = Intent(it, EditProfileActivity::class.java)
                    it.startActivity(intent)
                }
            }
            R.id.ll_wallet -> {
                activity?.let {
                    val intent = Intent(it, WalletActivity::class.java)
                    it.startActivity(intent)
                }
            }
            R.id.ll_wish_list -> {
                activity?.let {
                    val intent = Intent(it, MyWishlistActivity::class.java)
                    it.startActivity(intent)
                }
            }
            R.id.ll_cart -> {
                activity?.let {
                    EventBus.getDefault().postSticky(SelectCartEventPojo(true, false))
                }
            }

            R.id.ll_my_orders -> {
                activity?.let {
                    val intent = Intent(it, MyOrderActivity::class.java)
//                    val intent = Intent(it, OrderItemsActivity::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_change_language -> {
                activity?.let {
                    val intent = Intent(it, SelectLanguage::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_coupons -> {
                activity?.let {
                    val intent = Intent(it, CouponsActivity::class.java)
                    it.startActivity(intent)
                }
            }
            R.id.download_invoice -> {
                activity?.let {
                    val intent = Intent(it, DownloadedInvoiceList::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_manage_address -> {
                activity?.let {
                    val intent = Intent(it, ManageAddressActivity::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_support -> {
                activity?.let {
                    val intent = Intent(it, SupportActivity::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_settings -> {
                activity?.let {
                    val intent = Intent(it, SecondSettingActivity::class.java)
                    it.startActivity(intent)
                }
            }

            R.id.ll_dowloaded_invoice -> {
                activity?.let {
                    val intent = Intent(it, DownloadedInvoiceList::class.java)
                    it.startActivity(intent)
                }
            }

        }

    }

    private fun dialogSignout() {
        var dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialogue_sign_out)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.btnOkSignout.setOnClickListener {
            LoginManager.getInstance().logOut()
            if (prefs.loginguest == true) {
                prefs.loginguest = false
            }
            if (prefs.loginStatusSocial == true) {
                prefs.accessToken = ""
                prefs.loginStatusSocial = false
                prefs.full_name = ""
                prefs.email = ""
                prefs.profile_image = ""
                prefs.isDeliveryBoy = false
                prefs.deliveryBoy = ""
            }
            if (prefs.loginStatus == true) {
                prefs.accessToken = ""
                prefs.loginStatus = false
                prefs.full_name = ""
                prefs.email = ""
                prefs.mobile_no = ""
                prefs.profile_image = ""
                prefs.isDeliveryBoy = false
                prefs.deliveryBoy = ""
            }
            activity?.let {
                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
            dialog.dismiss()
        }
        dialog.btnCancelSignout.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }
    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    override fun onResume() {
        super.onResume()
        Log.i("TANU==","onResume")
        initViews()
    }
    override fun onPause() {
        super.onPause()
        Log.i("TANU==","onPause")

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("TANU==","onActivityCreated")
    }


}
