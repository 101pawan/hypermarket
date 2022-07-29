package com.hypermarket_android.ui.mypoints

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.util.ErrorUtil
import com.hypermarket_android.util.ProgressDialogUtils
import com.hypermarket_android.util.showToast
import com.hypermarket_android.viewModel.MyPointsViewModel
import kotlinx.android.synthetic.main.activity_my_points.*


class MyPointsActivity : BaseActivity(), View.OnClickListener {

    lateinit var myPointsViewModel: MyPointsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_points)

        initViews()
        initControl()
    }

    override fun initViews() {
        myPointsViewModel = ViewModelProvider(this).get(MyPointsViewModel::class.java)

        tvReferralCode.text = sharedPreference.referralCode

        hitGetEarnPoints()
        observers()
    }

    fun hitGetEarnPoints(){
        ProgressDialogUtils.getInstance().showProgress(this,true)
        myPointsViewModel.getUserRewardPoint(
            accessToken =  sharedPreference.accessToken,
            user_id =  sharedPreference.userId)
    }

    fun observers() {

        myPointsViewModel.errorResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        myPointsViewModel.earnResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            tvEarnPoints.text=it.earnPoint!!.toString()
        })

        myPointsViewModel.addEarnResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            com.hypermarket_android.util.showToast(this,it.message!!)
            hitGetEarnPoints()
        })

    }

    override fun initControl() {
        btnInviteFriends.setOnClickListener(this)
        btn_back.setOnClickListener(this)
        tvInviteFriends.setOnClickListener(this)
        tvRewardPoints.setOnClickListener(this)
        linearTapCode.setOnClickListener(this)
    }


    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
            showToast(resources.getString(R.string.refferal_code_coppied)+"!!!===>" + clipboard.text)
        } else {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)

        }

    }


    var isClip = false

    @SuppressLint("NewApi")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.linearTapCode -> {
                isClip = true
                showToast(
                    resources.getString(R.string.your_referral_code) + tvReferralCode.text.toString().trim() + " Copied."
                )
                //   setClipboard(this,tvReferralCode.text.toString().trim())
            }

            R.id.btnInviteFriends -> {
                val referralCode = tvReferralCode.text.toString().trim()
                if(referralCode=="NA"){
                    showToast(resources.getString(R.string.referral_code_is_not_available))
                }else{
                    if (isClip) {



                        isClip = false
                        /*  val sendIntent = Intent()
                          sendIntent.action = Intent.ACTION_SEND
                          sendIntent.putExtra(
                              Intent.EXTRA_TEXT,
                              "https:\\com.hypermarket/" + referralCode
                          )
                          sendIntent.type = "text/plain"
                          val receiver = Intent(this, NotifyReceiver::class.java)
                          val pendingIntent = PendingIntent.getBroadcast(
                              this,
                              0,
                              receiver,
                              PendingIntent.FLAG_UPDATE_CURRENT
                          )*/
                        /*  val chooser =
                              Intent.createChooser(sendIntent, null, pendingIntent.intentSender)
                          startActivity(chooser)*/
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, "https:\\com.hypermarket/"+referralCode);
                        startActivity(Intent.createChooser(intent, "Share"))

                        ProgressDialogUtils.getInstance().showProgress(this,true)
                        myPointsViewModel.addUserRewardPoints(
                            accessToken =  sharedPreference.accessToken,
                            user_id =  sharedPreference.userId)

                    } else {
                        showToast(resources.getString(R.string.please_select_first_referral_code))
                    }
                }

            }

            R.id.tvInviteFriends -> {
                tvInviteFriends.background =
                    ContextCompat.getDrawable(this, R.drawable.left_tab_fill)
                tvInviteFriends.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvRewardPoints.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tvRewardPoints.background = null
                btnInviteFriends.visibility = View.VISIBLE
                linearInvite.visibility = View.VISIBLE
                linearEarnPoints.visibility = View.GONE
            }

            R.id.tvRewardPoints -> {
                tvInviteFriends.background = null
                tvInviteFriends.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tvRewardPoints.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvRewardPoints.background =
                    ContextCompat.getDrawable(this, R.drawable.right_tab_fill)
                btnInviteFriends.visibility = View.GONE
                linearEarnPoints.visibility = View.VISIBLE
                linearInvite.visibility = View.GONE
            }


            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}