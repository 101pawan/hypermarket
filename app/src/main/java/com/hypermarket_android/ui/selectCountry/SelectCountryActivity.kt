package com.hypermarket_android.ui.selectCountry

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hypermarket_android.R
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.constant.NetworkConstants
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.selectLanguage.ChoseLanguageActivity
import com.hypermarket_android.util.showToast
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.android.synthetic.main.activity_select_language.*
import kotlinx.android.synthetic.main.dialogue_country_code_select.*
import java.io.IOException
import java.util.*


class SelectCountryActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
//    var list_of_items = arrayOf("Select Country","Oman", "UAE","India")
    var list_of_items = arrayOf("Select Country", "UAE","India")
    var country_code ="0"
    private var locationManager : LocationManager? = null
    val PERMISSION_ID = 42
    var lattitude=""
    var longitude =""
    var omanLattitude="21.4735"
    var omanLongitude="55.9754"
    var uaeLattitude ="23.4241"
    var uaeLongitude ="53.8478"
    var indiaLattitude ="20.5937"
    var indiaLongitude ="78.9629"

    private var  countryName:String?=null
    private var  selectedCountry:String?=null
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language)
        initViews()
        initControl()
        statusBackGroundColor()

//        val locale = baseContext.getResources().getConfiguration().locale.getCountry();


    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun statusBackGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.gradiant_back)
    }
    override fun initViews() {

        val aa = ArrayAdapter(this, R.layout.spinner_item, list_of_items)
        aa.setDropDownViewResource(R.layout.spinner_item)
        // Set Adapter to Spinner
        spinner_country!!.setAdapter(aa)
        sharedPreference.firstOpen=true

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        if  (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, locationListener)
            }
        }else{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions()
                return
            }
           // requestPermissions()
            buildAlertMessageNoGps()
        }
    }


    override fun initControl() {
        tv_proceed.setOnClickListener(this)
        spinner_country!!.setOnItemSelectedListener(this)
    }


    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.yes), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
            .setNegativeButton(resources.getString(R.string.no), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    dialog.cancel()
                }
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_proceed ->{
                if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (isValidField()) {
                    if (countryName.equals(selectedCountry) or lattitude.equals("")){
                        nextScreen()
                    }else{
                        dialogCountryCodeSelect(resources.getString(R.string.your_current_location_does_match_with_selected_country_do_you_want_to_continue))

                    }

                    Log.d("lattitude==",lattitude)

                   /* if(!lattitude.equals(omanLattitude)or !lattitude.equals(uaeLattitude) or !lattitude.equals(indiaLattitude)){
                        dialogCountryCodeSelect("Your current location doesn't match with selected country. Do you want to continue?")

                    }else{
                        nextScreen()

                    }*/



              /*      if (lattitude.equals(omanLattitude) && longitude.equals(omanLongitude)) {
                        if (country_code.equals("+971") || country_code.equals("+91")) {
                            dialogCountryCodeSelect("Your current location doesn't match with selected country. Do you want to continue?")
                        } else if (country_code.equals("+968")) {
                            nextScreen()
                        }

                    } else if (lattitude.equals(uaeLattitude) && longitude.equals(uaeLongitude)) {
                        if (country_code.equals("+968") || country_code.equals("+91")) {
                            dialogCountryCodeSelect(
                                "Your current location doesn't match with selected country. Do you want to continue?"
                            )
                        } else if (country_code.equals("+971")) {
                            nextScreen()
                        }
                    } else if (!lattitude.equals(uaeLattitude) && !longitude.equals(uaeLongitude) &&
                        !lattitude.equals(omanLattitude) && !longitude.equals(omanLongitude)
                    ) {
                        if (country_code.equals("+968") || country_code.equals("+971")) {
                            dialogCountryCodeSelect(
                                "Your current location doesn't match with selected country. Do you want to continue?"
                            )
                        } else if (country_code.equals("+91")) {
                            nextScreen()
                        }

                    } else {
                        dialogCountryCodeSelect(
                            "Your current location doesn't match with selected country. Do you want to continue?"
                        )
                    }*/
                }
                }else{
                    buildAlertMessageNoGps()
                }
            }
        }
    }



    private fun dialogCountryCodeSelect(msg:String) {
        var dialog = this?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialogue_country_code_select)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.textView51_msg.setText(msg)
        dialog.setCanceledOnTouchOutside(false)

        dialog.show()
        dialog.btnOk.setOnClickListener {

            this?.let{
                val intent = Intent (it, ChoseLanguageActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
            dialog.dismiss()
        }
        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        val window = dialog.getWindow()
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }


    private fun nextScreen() {
        startActivity(Intent(this, ChoseLanguageActivity::class.java))
        finish()
    }


//    private fun setSpinnerTime() {
//
//        val adapter = ArrayAdapter.createFromResource(
//            context!!,
//            R.array.time, android.R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        timeSpinner.adapter = adapter
//
//        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//                //val text = p0?.getItemAtPosition(p2).toString()
//                //Toast.makeText(p0?.getContext(), text, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
      //  (parent.getCh as TextView).setTextColor(Color.WHITE)

        Log.e("country",spinner_country.selectedItem.toString());
        if (spinner_country.selectedItem.equals("Oman")){

            selectedCountry="Oman"
          country_code = "+968"
            sharedPreference.country_code = country_code
            sharedPreference.country = selectedCountry!!
            sharedPreference.storeId=NetworkConstants.OMAN_STORE_ID
            sharedPreference.storeName=NetworkConstants.OMAN_STORE_NAME
        }else if (spinner_country.selectedItem.equals("UAE")){
            selectedCountry="United Arab Emirates"
            country_code = "+971"
            sharedPreference.country_code = country_code
            sharedPreference.country = selectedCountry!!
            sharedPreference.storeId=NetworkConstants.DUBAI_STORE_ID
            sharedPreference.storeName=NetworkConstants.DUBAI_STORE_NAME

        }else if (spinner_country.selectedItem.equals("India")){
            selectedCountry="India"
            country_code = "+91"
            sharedPreference.country_code = country_code
          //  sharedPreference.country = "India"
            sharedPreference.country = "United Arab Emirates"
            sharedPreference.storeId=NetworkConstants.DUBAI_STORE_ID
            sharedPreference.storeName=NetworkConstants.DUBAI_STORE_NAME

        }

        else{
            selectedCountry=""
            country_code = "0"
            sharedPreference.country_code = country_code
        }


    }

    private fun isValidField(): Boolean {
        if (spinner_country.selectedItem == "Select Country") {
            spinner_country.requestFocus()
            showToast(resources.getString(R.string.please_select_country))
            return false
        }
        return true
    }

    private fun checkLocationPermission(): Boolean {
        var permission = false
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {
                permission = true
            }
            override fun onDenied(
                context: Context?,
                deniedPermissions: java.util.ArrayList<String>?
            ) {
                permission = false
            }
        })
        return permission
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, locationListener)
            }
        }
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            Log.e("location",location.longitude.toString() + ":" + location.latitude+"")
            lattitude = location.latitude.toString()
            longitude = location.longitude.toString()


            try {
                val gcd = Geocoder(applicationContext, Locale.getDefault())
                val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses.size > 0)
                    countryName=addresses.get(0).countryName
                try{
                    sharedPreference.city = addresses.get(0).locality
                }catch (e: Exception){

                }

                //countryName="Oman"
               /* Log.d("name==",countryName!!)
                    Toast.makeText(
                        applicationContext,
                        "name " + addresses.get(0).countryName,
                        Toast.LENGTH_SHORT
                    ).show()*/

                //System.out.println(addresses.get(0).getLocality());
            } catch (e: IOException) {

                e.printStackTrace()
            }

//            lattitude = "21.4735"
//            longitude = "55.9754"
            //showToast(this,"" + location.longitude + ":" + location.latitude+"")




        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}

