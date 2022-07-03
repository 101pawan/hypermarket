package com.hypermarket_android.ui.editProfile

import android.Manifest
import android.R.attr
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.ui.personalDetailActivity.CreateProfileViewModel
import com.hypermarket_android.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.img_profile_img
import kotlinx.android.synthetic.main.activity_personal_detail.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : BaseActivity(), View.OnClickListener {
//    var list_of_items = arrayOf("Oman", "UAE")
    var list_of_items = arrayOf("UAE")
    private lateinit var f: File
    private lateinit var photoURI: Uri
    private lateinit var mCurrentPhotoPath: String
    private lateinit var mviewModel: CreateProfileViewModel
    var profileImage: File? = null
    private lateinit var sharedPreferences: SharedPreferenceUtil

    //   var location = Location("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        initControl()
        initViews()
        sharedPreferences = SharedPreferenceUtil.getInstance(this)
    }

    override fun initViews() {
        mviewModel = ViewModelProviders.of(this).get(CreateProfileViewModel::class.java)
        edit_profile_full_name.setText(sharedPreference.full_name)
        edit_profile_email.setText(sharedPreference.email)
        if (sharedPreference.loginStatus==true) {
            edit_profile_mobile_no.setText("(" + sharedPreference.country_code + ")" + sharedPreference.mobile_no)
        }
        val aa = ArrayAdapter(this, R.layout.spinner_item_edit_profile, list_of_items)
        aa.setDropDownViewResource(R.layout.spinner_item_edit_profile)
        // Set Adapter to Spinner
        spinner_edit_profile!!.setAdapter(aa)
       mviewModel.mResponseCreateProfile.observe(this, androidx.lifecycle.Observer {
           progress.visibility = GONE
           Log.e("gotthe response","got the response")
           sharedPreferences.accessToken = it.user_data.access_token
           sharedPreference.full_name = it.user_data.name
           sharedPreference.email = it.user_data.email
           sharedPreference.mobile_no = it.user_data.mobile_number
           finish()
       })
    }

    override fun initControl() {
        img_edit_profile_back.setOnClickListener(this)
        img_profile_img.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }
    fun selectImage(){
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery","Delete photo", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@EditProfileActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                openCamera()
                dialog.dismiss()
            } else if (options[item] == "Choose from Gallery") {
                openGallery()
                dialog.dismiss()
            }else if(options[item] == "Delete photo"){
                profileImage = null
                sharedPreference.profile_image = ""
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }
    private fun checkPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE),
            101
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode ==
            106) {
            f =  File(mCurrentPhotoPath);
            sharedPreference.profile_image = f.toString()
            img_profile_img.setImageURI(Uri.parse(mCurrentPhotoPath))
        }else if (resultCode == RESULT_OK && requestCode == 107){
            val selectedImg = data!!.data
            profileImage = File(getRealPathFromUri(selectedImg))
            sharedPreference.profile_image = profileImage.toString()
            img_profile_img.setImageURI(selectedImg)
        }

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_edit_profile_back -> {
                finish()
                return
                // startActivity(Intent(this, HomeActivity::class.java))
                // finish()
            }
            R.id.img_profile_img -> {
                if (!checkPermission()) {
                    requestPermission()
                } else {
                    selectImage()
                }
                return
            }
            R.id.btn_submit -> {
                progress.visibility = VISIBLE
                var full_name = edit_profile_full_name.text.toString()
                var email = edit_profile_email.text.toString()
                var mob_no = edit_profile_mobile_no.text.toString()
                Log.e(
                    "data_profile",
                    full_name + " " + email + " " + mob_no + " " +" == "+profileImage
                )
                    mviewModel.setEditProfile(
                        sharedPreferences.accessToken,
                        sharedPreference.userId,
                        full_name,
                        email,
                        profileImage
                    )
                    return

            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) !=
            null
        ) {
            // Create the File where the photo should go
            //f = null
            try {
                f = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (f != null) {
                photoURI = FileProvider.getUriForFile(
                    this,
                    "com.hypermarket_android.provider",
                    f
                )
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                startActivityForResult(
                    takePictureIntent,
                    106
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }
    private fun openGallery(){
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ),
            107
        )

    }
    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(
                contentUri!!, proj, null,
                null, null
            )
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }
}