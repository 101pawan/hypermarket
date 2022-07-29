package com.hypermarket_android.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hypermarket_android.R
import com.hypermarket_android.activity.ManageAddressActivity.Companion.DATA_UPDATE_ADDRESS
import com.hypermarket_android.base.BaseActivity
import com.hypermarket_android.dataModel.CityResponse
import com.hypermarket_android.dataModel.ManageAddressResponse
import com.hypermarket_android.dataModel.StateResponse
import com.hypermarket_android.util.*
import com.hypermarket_android.viewModel.AddUpdateAddressViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_address.*


// add address update address
class
AddUpdateAddressActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {
    companion object {
        const val TEXT_HOME =  "Home"
        const val TEXT_OFFICE = "Office"
        const val TEXT_OTHER = "Other"
    }

    private lateinit var mAddAddressViewModel: AddUpdateAddressViewModel
    val cityList = ArrayList<CityResponse.City>()
    val mDistrictList = ArrayList<District>()
    var mSelectedCity: CityResponse.City? = null
    var mSelectedDistrict: StateResponse.State? = null
    var mDataToUpadate: ManageAddressResponse.UserAddres? = null

    // home , office, other
    private var saveAsType: String? = "Home"

    private var cityId = 0
    private var StateId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddAddressViewModel = ViewModelProvider(this).get(AddUpdateAddressViewModel::class.java)
        setContentView(R.layout.activity_add_address)
        iv_actionbar_back.setOnClickListener { onBackPressed() }
        if (intent != null && intent.hasExtra(DATA_UPDATE_ADDRESS)) {
            mDataToUpadate = Gson().fromJson(
                intent.getStringExtra(DATA_UPDATE_ADDRESS),
                ManageAddressResponse.UserAddres::class.java
            )
        }
        when(sharedPreference.country_code){
            "+968"->{
                ccd_add_address.setCountryForPhoneCode(968)
            }
            "+971"->{
                ccd_add_address.setCountryForPhoneCode(971)
            }
        }
        ccd_add_address.setTypeFace(ResourcesCompat.getFont(this,R.font.montserrat_regular))
        updateViews(mDataToUpadate)
        initControl()
        grpAddress.setOnCheckedChangeListener(this)
    }
    private fun updateViews(updateDATA: ManageAddressResponse.UserAddres?) {
        // UAE district
        /*if (sharedPreference.country.equals(Constants.UNITED_ARAB_EMIRATES, true)) {
            addUaeDistrict(mDistrictList)
        }
        // oman district
        else {
            addOmanDistrict(mDistrictList)
        }

*/
        if (updateDATA != null) {
            updateDATA.apply {
                cityId = city
                StateId = district
                et_name_add_address.setText(name ?: "")
                et_house_number.setText(house_number)
                et_building.setText(building_tower)
                et_society.setText(society_locality)
                ccd_add_address.setCountryForPhoneCode(
                    sharedPreference.country_code.substring(
                        1,
                        sharedPreference.country_code.length
                    ).toInt()
                )
                et_alt_mobile_number.setText(alt_mobile_number)
               // et_pincode.setText(pincode)
                saveAsType = save_as
                if (save_as == TEXT_HOME) {
                    rb_home.isChecked = true
                    rb_office.isChecked = false
                    rb_other.isChecked = false


                } else if (save_as == TEXT_OFFICE) {
                    rb_office.isChecked = true
                    rb_home.isChecked = false
                    rb_other.isChecked = false
                    // other
                } else {
                    rb_other.isChecked = true
                    rb_home.isChecked = false
                    rb_office.isChecked = false
                }
            /*mSelectedDistrict?.districtId
                mSelectedCity?.id
                saveAsType*/
            }
        }

        /*229 UAE
          165 Oman */
        if (Helper.isNetworkAvailable(this!!)) {
            if (sharedPreference.country.equals(Constants.UNITED_ARAB_EMIRATES, true)) {
                // UAE
                /* mAddAddressViewModel.getCity(sharedPreference.accessToken, "229")*/
                mAddAddressViewModel.getStates(sharedPreference.accessToken, "229")
            } else {
                //OMAN
                /*  mAddAddressViewModel.getCity(sharedPreference.accessToken, "165")*/
                mAddAddressViewModel.getStates(sharedPreference.accessToken, "165")
            }
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.message_no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        tv_save_address.setOnClickListener {
            val isAddressValidated = validateAddressDetails(
                et_name_add_address.text.toString(),
                et_house_number.text.toString(),
                et_building.text.toString(), et_society.text.toString(),
                ccd_add_address.selectedCountryCodeWithPlus.toString(),
                et_alt_mobile_number.text.toString(), et_pincode.text.toString(),
                mSelectedDistrict, mSelectedCity, saveAsType.toString()
            )
            if (isAddressValidated) {
                var cityId = 0
                if (mSelectedCity != null){
                    cityId = mSelectedDistrict?.id!!
                }
                if (mDataToUpadate != null) {
                    mAddAddressViewModel.hitUpdateAddressApi(
                        sharedPreference.accessToken,
                        et_name_add_address.text.toString(),
                        et_house_number.text.toString(),
                        et_building.text.toString(),
                        et_society.text.toString(),
                        ccd_add_address.selectedCountryCodeWithPlus.toString(),
                        et_alt_mobile_number.text.toString(),
                       "",
                        cityId,
                        mSelectedDistrict?.id!!,
                        saveAsType.toString(),
                        mDataToUpadate?.id!!
                    )
                }else {
                    mAddAddressViewModel.hitAddNewAddressApi(
                        sharedPreference.accessToken,
                        et_name_add_address.text.toString(),
                        et_house_number.text.toString(),
                        et_building.text.toString(),
                        et_society.text.toString(),
                        ccd_add_address.selectedCountryCodeWithPlus.toString(),
                        et_alt_mobile_number.text.toString(),
                       "",
                        cityId,
                        mSelectedDistrict?.id!!,
                        saveAsType.toString()
                    )

                }

            }

        }
    }
    private fun validateAddressDetails(
        name: String,
        houseNumber: String,
        building: String,
        society: String,
        defaultCountryCodeWithPlus: String,
        mobileNo: String,
        pincode: String,
        mSelectedDistrict: StateResponse.State?,
        mSelectedCity: CityResponse.City?,
        saveAsType: String?
    ): Boolean {

        if (TextUtils.isEmpty(name)) {
            showToast(resources.getString(R.string.please_enter_full_name))
            return false
        }
        if (TextUtils.isEmpty(houseNumber)) {
            showToast(resources.getString(R.string.please_enter_building_number))
            return false
        }

        if (TextUtils.isEmpty(building)) {
            showToast(resources.getString(R.string.please_enter_bank_id))

            return false
        }
        if (TextUtils.isEmpty(society)) {
            showToast(resources.getString(R.string.please_enter_society_number))
            return false
        }
        if (TextUtils.isEmpty(mobileNo)) {
            showToast(resources.getString(R.string.please_enter_mobile_number))

            return false
        }
      /*  if (TextUtils.isEmpty(pincode)) {
            showToast("Please enter pincode")

            return false
        }
*/
        if (mSelectedDistrict == null) {
            showToast(resources.getString(R.string.please_select_district))
            return false
        }
        if (spinner_city.isVisible && mSelectedCity == null) {
            showToast(resources.getString(R.string.please_select_city))
            return false
        }
        if (TextUtils.isEmpty(saveAsType)) {
            showToast(resources.getString(R.string.please_select_address_type))
            return false
        }
        return true
    }
    override fun initControl() {
        cityObservable()
        addNewAddressObservable()
        updateAddressObservable()
    }
    private fun addNewAddressObservable() {
        mAddAddressViewModel.addNewAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            setResult(RESULT_OK)
            finish()
        })
        mAddAddressViewModel.errorAddNewAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })
        mAddAddressViewModel.mProgessAddNewAddress.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }
    private fun updateAddressObservable() {
        mAddAddressViewModel.updateAddressResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            setResult(RESULT_OK)
            finish()
        })
        mAddAddressViewModel.errorUpdateAddress.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })
        mAddAddressViewModel.mProgessUpdateAddress.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }
    private fun cityObservable() {
        mAddAddressViewModel.mResponseCity.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
//            add a header
            cityList.clear()
            cityList.add(CityResponse.City(-1, 0, "Select City", -1))
//            add all cities
            cityList.addAll(it.data)
            if (it.data.isEmpty()){
                spinner_city.visibility=View.GONE
                ivCity.visibility=View.GONE
            }else{
                spinner_city.visibility=View.VISIBLE
                ivCity.visibility=View.VISIBLE
            }
            setCitySpinner(spinner_city, cityList)
             /*   if(cityId>0){
                    var citySpinnerPosition = 0
                    for (i in 0..cityList.size) {
                        if (cityId == cityList[i].id) {
                            citySpinnerPosition = i
                            spinner_city.setSelection(citySpinnerPosition)
                            mSelectedCity = cityList[citySpinnerPosition]
                        }
                    }
            }*/
        })

        mAddAddressViewModel.mResponseStates.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            var districtList: ArrayList<StateResponse.State> = ArrayList()
            districtList.add(StateResponse.State(0, 0, resources.getString(R.string.select_district)))
            for (item in it.data) {
                districtList.add(item)
            }
            setDistrictSpinner(spinner_district, districtList)
        })
        mAddAddressViewModel.errorCity.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            spinner_city.visibility=View.GONE
            ivCity.visibility=View.GONE
            // ErrorUtil.handlerGeneralError(this, it)
        })
        mAddAddressViewModel.mProgessCity.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })
    }
    private fun setCitySpinner(
        spinner_city: AppCompatSpinner,
        cityList: ArrayList<CityResponse.City>
    ) {
        SpinnerUtil.setSpinner(spinner_city, cityList.map { it.name }, this)
        /*if (dataToUpadate != null) {
            val cityName = getCityNameFromCityId(dataToUpadate.city)
            SpinnerUtil.setItemAtPosition(spinner_city, cityList.map { it.name }, cityName)
        }*/
        spinner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("no", "change")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                if (pos == 0) {
                    mSelectedCity = null
                } else {
                    mSelectedCity = cityList[pos]
                 //   showToast(mSelectedCity?.name!!)
                }
            }
        }
        /*var citySpinnerPosition = 0;
        for (i in 0..cityList.size) {
            if (cityId == cityList[i].id) {
                citySpinnerPosition = i
                spinner_city.setSelection(citySpinnerPosition)
            }
        }*/
    }
    override fun initViews() {
    }
    private fun setDistrictSpinner(
        spinner_district: AppCompatSpinner,
        districtList: List<StateResponse.State>
    ) {
        var districtName: ArrayList<String> = ArrayList();
        for (item in districtList) {
            districtName.add(item.state_name)
        }
      /*  val dataAdapter = ArrayAdapter(this, R.layout.sort_by_layout, districtName)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)
        // attaching data adapter to spinner
        spinner_district.adapter = dataAdapter*/
        SpinnerUtil.setSpinner(spinner_district, districtName, this)
//        SpinnerUtil.setItemAtPosition(spinner_city , list ,"hi")

        /* var districtSpinnerPosition = 0;
         for (i in 0..districtList.size) {
             if (StateId == districtList[i].id) {
                 districtSpinnerPosition = i
             }
         }
 */
        spinner_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("no", "change")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                if (pos == 0) {
                    mSelectedDistrict = null
                } else {
                    mSelectedDistrict = districtList[pos]
                    mAddAddressViewModel.getCity(
                        sharedPreference.accessToken,
                        districtList[pos].id.toString()
                    )
                  //  showToast(districtList[pos].state_name)
                }
            }
        }
        var districtSpinnerPosition = 0;
        for (i in 0..districtList.size) {
            if (StateId == districtList[i].id) {
                districtSpinnerPosition = i
                spinner_district.setSelection(districtSpinnerPosition)
            }

        }
        //  spinner_district.setSelection(districtSpinnerPosition)
        rb_home.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isPressed) {
                if (b) {
                    saveAsType = TEXT_HOME
                }
            }
        }
        rb_office.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isPressed) {
                if (b) {
                    saveAsType = TEXT_OFFICE
                }
            }
        }
        rb_other.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isPressed) {
                if (b) {
                    saveAsType = TEXT_OTHER
                }
            }
        }
    }
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val position=group!!.checkedRadioButtonId
        val button = findViewById<RadioButton>(position)
        saveAsType=button.text.toString()
    }
}

data class District(val districtName: String, val districtId: Int)
