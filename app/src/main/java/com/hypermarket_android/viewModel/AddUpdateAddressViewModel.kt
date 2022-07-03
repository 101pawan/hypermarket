package com.hypermarket_android.viewModel

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.AddUpdateAddressResponse
import com.hypermarket_android.dataModel.CityResponse
import com.hypermarket_android.dataModel.StateResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddUpdateAddressViewModel : BaseViewModel() {
    private lateinit var mDisposable: Disposable

    var addNewAddressResponse = MutableLiveData<AddUpdateAddressResponse>()
    var errorAddNewAddress = MutableLiveData<Throwable>()
    var mProgessAddNewAddress = MutableLiveData<Boolean>()


    var updateAddressResponse = MutableLiveData<AddUpdateAddressResponse>()
    var errorUpdateAddress = MutableLiveData<Throwable>()
    var mProgessUpdateAddress = MutableLiveData<Boolean>()

    var mResponseCity = MutableLiveData<CityResponse>()
    var mResponseStates = MutableLiveData<StateResponse>()
    var errorCity = MutableLiveData<Throwable>()
    var mProgessCity = MutableLiveData<Boolean>()

    //    new address api
    fun hitAddNewAddressApi(
        accessToken: String, name: String, houseNumber: String,
        buildingTower: String, societyLocality: String,
        altCountryCode: String, altMobileNumber: String,
        pincode: String, city: Int,
        district: Int, saveAs: String
    ) {
        mDisposable = apiInterface.addNewAddress(
            accessToken = accessToken,
            name = name,
            houseNumber = houseNumber,
            buildingTower = buildingTower,

            societyLocality = societyLocality,
            altCountryCode = altCountryCode,
            altMobileNumber = altMobileNumber,
            pincode = pincode,
            city = city,
            district = district,
            saveAs = saveAs

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessAddNewAddress.value = true
            }.doOnTerminate {
                mProgessAddNewAddress.value = false
            }
            .subscribe({
                onSuccessAddNewAddress(it)
            },
                {
                    onErrorAddNewAddress(it)
                })
    }

    private fun onSuccessAddNewAddress(it: AddUpdateAddressResponse) {
        addNewAddressResponse.value = it
    }

    private fun onErrorAddNewAddress(it: Throwable) {
        errorAddNewAddress.value = it
    }

    //  update address api
    fun hitUpdateAddressApi(
        accessToken: String, name: String, houseNumber: String,
        buildingTower: String, societyLocality: String,
        altCountryCode: String, altMobileNumber: String,
        pincode: String, city: Int,
        district: Int, saveAs: String, addressId: Int
    ) {
        mDisposable = apiInterface.updateAddress(

            accessToken = accessToken,
            name = name,
            houseNumber = houseNumber,
            buildingTower = buildingTower,
            societyLocality = societyLocality,
            altCountryCode = altCountryCode,
            altMobileNumber = altMobileNumber,
            pincode = pincode,
            city = city,
            district = district,
            saveAs = saveAs,
            addressId = addressId


        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessUpdateAddress.value = true
            }.doOnTerminate {
                mProgessUpdateAddress.value = false
            }
            .subscribe({
                onSuccessUpdateAddress(it)
            },
                {
                    onErrorUpdateAddress(it)
                })
    }


    private fun onSuccessUpdateAddress(it: AddUpdateAddressResponse) {
        updateAddressResponse.value = it
    }

    private fun onErrorUpdateAddress(it: Throwable) {
        errorUpdateAddress.value = it
    }


    fun getCity(accessToken: String, state_id: String) {

        apiInterface.getCityNames(
            accessToken = accessToken,
            state_id = state_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessCity(it)
            }, {
                //mProgess.value = false
                onErrorCity(it)
            })
    }


    fun getStates(accessToken: String, country_id: String) {

        apiInterface.getStates(
            accessToken = accessToken,
            country_id = country_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessStates(it)
            }, {
                //mProgess.value = false
                onErrorCity(it)
            })
    }

  /*  fun getCities(accessToken: String, state_id: String) {

        apiInterface.getCityNames(
            accessToken = accessToken,
            state_id = state_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessCity(it)
            }, {
                //mProgess.value = false
                onErrorCity(it)
            })
    }
*/

    private fun onSuccessCity(it: CityResponse?) {
        mResponseCity.value = it
    }

    private fun onErrorCity(it: Throwable?) {
        errorCity.value = it
    }

    private fun onSuccessStates(it: StateResponse) {
        mResponseStates.value = it
    }




}