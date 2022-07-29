package com.hypermarket_android.ui.personalDetailActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.app.zaoplus.utils.ImageUtils
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.constant.NetworkConstants
import com.hypermarket_android.imagePicker.FilePickUtils
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.util.ErrorUtils
import com.hypermarket_android.util.TakeImageUtil
import com.hypermarket_android.util.setImageFromDevice
import com.hypermarket_android.util.showToast
import com.theartofdev.edmodo.cropper.CropImage
//import com.imagepicker.FilePickUtils
import kotlinx.android.synthetic.main.activity_personal_detail.*
import kotlinx.android.synthetic.main.dialogue_image_chooser.*
import java.io.File

class PersonalDetailActivity : BaseActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener {

    var arrayListCity: ArrayList<String> = ArrayList()
    var pathFile :String=""
    var city=""
    var country_id = ""
    private lateinit var mviewModel: CreateProfileViewModel
    var imageType = 1
    var profile_image: File? = null
    var full_name=""
    private val onFileChoose = FilePickUtils.OnFileChoose { fileUri, requestCode ->

        // here you will get captured or selected image<br>
        Log.e("image", "File= " + fileUri)
        when (imageType) {
            1 -> {
                profile_image = File(fileUri)
                sharedPreference.profile_image = profile_image.toString()
                img_profile_img.setImageFromDevice(fileUri)
            }
        }
    }

    var filePickUtils = FilePickUtils(this, onFileChoose)
    //   var location = Location("")
    var lifeCycleCallBackManager = filePickUtils.callBackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_detail)
        initViews()
        initControl()
        myObserver()
    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(CreateProfileViewModel::class.java)
//        var list_of_items = arrayOf(resources.getString(R.string.select_country),"Oman", "UAE")
        var list_of_items = arrayOf(resources.getString(R.string.select_country), "UAE")
        val aa = ArrayAdapter(this, R.layout.spinner_item, list_of_items)
        aa.setDropDownViewResource(R.layout.spinner_item)
        // Set Adapter to Spinner
        spinner_country3!!.setAdapter(aa)
        arrayListCity.add(resources.getString(R.string.select_city))
        val aa2 = ArrayAdapter(this, R.layout.spinner_item, arrayListCity)
        aa2.setDropDownViewResource(R.layout.spinner_item)
        spinner_city!!.setAdapter(aa2)
        et_profile_name?.setText(sharedPreference.full_name)
        et_profile_email?.setText(sharedPreference.email)
        et_profile_mobNo?.setText(sharedPreference.mobile_no)
        tv_country_code_profile_create.setText(sharedPreference.country_code+" -")
        tv_country_code_shipping_profile.setText(sharedPreference.country_code+" -")
        tv_country_code_shiping_alter.setText(sharedPreference.country_code+" -")
        val position: Int = et_profile_buildingName.length()
        val etext: Editable? = et_profile_buildingName.getText()
        Selection.setSelection(etext, position)
        val position2: Int = et_profile_street.length()
        val etext2: Editable? = et_profile_street.getText()
        Selection.setSelection(etext2, position2)
        val position3: Int = et_profile_nearbylocation.length()
        val etext3: Editable? = et_profile_nearbylocation.getText()
        Selection.setSelection(etext3, position3)
        val position4: Int = et_profile_zip_code.length()
        val etext4: Editable? = et_profile_zip_code.getText()
        Selection.setSelection(etext4, position4)
    }
    override fun initControl() {
        tv_save_proceed.setOnClickListener(this)
        spinner_country3!!.setOnItemSelectedListener(this)
        img_profile_img.setOnClickListener(this)
        et_profile_street.setOnClickListener(this)
        et_profile_nearbylocation.setOnClickListener(this)
        et_profile_zip_code.setOnClickListener(this)
    }
    private fun openDialog() {
       var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_image_chooser)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.btnCamera.setOnClickListener {
            takeFromCamera()
            dialog.dismiss()
        }
        dialog.btnGallery.setOnClickListener {
            takeFromGallery()
            dialog.dismiss()
        }
        val window = dialog.getWindow()
        if (window != null) {
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun chooseFromGallery() {
        filePickUtils.requestImageGallery(FilePickUtils.STORAGE_PERMISSION_IMAGE, true, false)
    }


    private fun takeFromCamera(){
        val galleryIntent = Intent(this, TakeImageUtil::class.java)
        galleryIntent.putExtra(TakeImageUtil.EXTRA_DATA, TakeImageUtil.FROM_CAMERA)
        startActivityForResult(galleryIntent, TakeImageUtil.PICK_IMAGE)
    }

    private fun takeFromGallery(){
        val galleryIntent = Intent(this, TakeImageUtil::class.java)
        galleryIntent.putExtra(TakeImageUtil.EXTRA_DATA, TakeImageUtil.FROM_GALLERY)
        startActivityForResult(galleryIntent, TakeImageUtil.PICK_IMAGE)
    }


    private fun takePhotoFromCamera() {
        filePickUtils.requestImageCamera(
            FilePickUtils.CAMERA_PERMISSION,
            true,
            false
        ); // pass false if you dont want to allow image crope
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (lifeCycleCallBackManager != null) {
            lifeCycleCallBackManager.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (lifeCycleCallBackManager != null) {
            lifeCycleCallBackManager.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == TakeImageUtil.PICK_IMAGE) {
            data?.let {
                it.getStringExtra("filePath")?.let { filePath ->
                    CropImage.activity(Uri.fromFile(File(filePath))).start(this)
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            result.uri.path?.let {
                pathFile=it
              //  SharedPreferenceUtil.getInstance(this).uriPath =pathFile
                val imgFile = File(pathFile)
                profile_image = imgFile
                val imagePath = ImageUtils.changeFileSize(imgFile.absolutePath, getString(R.string.app_name),this)
                val bitmap = BitmapFactory.decodeFile(imagePath, BitmapFactory.Options())
                img_profile_img.setImageBitmap(bitmap)
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_save_proceed ->{
                var full_name=  et_profile_name.text.toString()
                var email=  et_profile_email.text.toString()
                var mob_no=   et_profile_mobNo.text.toString()
                var shipping_name=  et_shiping_profileName.text.toString()
                var alternate_mob_no=  et_profile_alternatemobNo.text.toString()
                var building_name=  et_profile_buildingName.text.toString()
                var street_name=  et_profile_street.text.toString()
                var near_by_location=  et_profile_nearbylocation.text.toString()
                var zip_code= /* et_profile_zip_code.text.toString()*/""
                var country =spinner_country3.selectedItem.toString()
                var city = spinner_city.selectedItem.toString()
                Log.e("data_profile",full_name+" "+email+" "+mob_no+" "+shipping_name+" "+alternate_mob_no+" "+building_name+" "+
                street_name+" "+near_by_location+" "+zip_code)
                if (isValid()) {
                    mviewModel.getEditProfile(
                        sharedPreference.userId,
                        full_name,
                        email,
                        profile_image,
                        shipping_name,
                        mob_no,
                        alternate_mob_no,
                        country,
                        city,
                        building_name,
                        street_name,
                        near_by_location,
                        zip_code
                    )
                }
            }

            R.id.img_profile_img ->{
                imageType=1
                openDialog()
            }

            R.id.et_profile_street ->{
                et_profile_buildingName.requestFocus()
                et_profile_buildingName.setSelection(0)
            }
            R.id.et_profile_nearbylocation ->{
                et_profile_street.requestFocus()
                et_profile_street.setSelection(0)
            }
            R.id.et_profile_zip_code ->{
                et_profile_nearbylocation.requestFocus()
                et_profile_nearbylocation.setSelection(0)
            }
        }

    }

    private fun myObserver() {
        mviewModel.mResponseCreateProfile.observe(this, androidx.lifecycle.Observer {
//            if (it.user_data!=null)
//            {
                com.hypermarket_android.util.showToast(this,it.message)
                sharedPreference.loginStatus=true
                sharedPreference.full_name= it.user_data.name.toString()
                sharedPreference.email= it.user_data.email.toString()
                sharedPreference.accessToken = it.user_data.access_token
                sharedPreference.mobile_no= it.user_data.mobile_number.toString()
            if (it.user_data.image!=null) {
                sharedPreference.profile_image = it.user_data.image
            }
              //  sharedPreference.country_code = it.user_data.country_code.toString()
                nextScreen()
          //  }

        })

        mviewModel.mResponseCity.observe(this, androidx.lifecycle.Observer {
            if (it.cities!=null){
                arrayListCity.add(resources.getString(R.string.select_city))
                for (i in it.cities) {
                    arrayListCity.add(i.name)
                }
                val aa2 = ArrayAdapter(this, R.layout.spinner_item, arrayListCity)
                aa2.setDropDownViewResource(R.layout.spinner_item)
                spinner_city!!.setAdapter(aa2)
            }

        })

        mviewModel.mError.observe(this, androidx.lifecycle.Observer {
            ErrorUtils.handlerGeneralError(this,it)

        })
    }

    private fun nextScreen() {
//        startActivity(Intent(this, ChoosePreferredStoreActivity::class.java))
//        finish()
         startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (spinner_country3.selectedItem.equals("Oman")){
            arrayListCity.clear()
            country_id="165"
           // sharedPreference.country_code = "+968"
            mviewModel.hitGetCity("kjkjj",country_id)
        }else if (spinner_country3.selectedItem.equals("UAE")){
            arrayListCity.clear()
            country_id="229"
          //  sharedPreference.country_code = "+971"
            mviewModel.hitGetCity("kjkjj",country_id)
        }
        else{
            arrayListCity.clear()
            arrayListCity.add(resources.getString(R.string.please_select_city))
        }
    }


    fun isValid():Boolean{
        if (et_profile_name.text.toString().trim().isEmpty()){
            showToast(resources.getString(R.string.please_enter_full_name))
            et_profile_name.requestFocus()
            return false
        }

        if (et_profile_name.text.toString().length>35){
            showToast(resources.getString(R.string.full_name_character_limit_exceeded))
            et_profile_name.requestFocus()
            return false
        }

        if (et_profile_mobNo.text.toString().trim().isEmpty()){
            showToast(resources.getString(R.string.please_enter_mobile_number))
            et_profile_mobNo.requestFocus()
            return false
        }




        if (et_profile_email.text.toString().trim().isEmpty()) {
            showToast(resources.getString(R.string.please_enter_emailid))
            et_profile_email.requestFocus()
            return false
        }
        if (!et_profile_email.text.toString().trim().matches(NetworkConstants.EMAIL_PATTERN.toRegex())) {
            showToast(resources.getString(R.string.please_enter_valid_emailid))
            et_profile_email.requestFocus()
            return false
        }
        return true
    }

}