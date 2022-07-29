package com.hypermarket_android.ui.personalDetailActivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.CategoriesDataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import java.io.File

class CreateProfileViewModel:BaseViewModel() {

    var mResponseCreateProfile = MutableLiveData<ResponseProfileCreate>()
    var mResponseCity = MutableLiveData<ResponseCity>()
    var mError = MutableLiveData<Throwable>()

    fun getEditProfile(user_id:String,full_name:String,email: String,image: File?,name:String,mobile_number:String,alt_mobile_number:String,country_name:String,city_name:String,
    building_name:String,street:String,near_location:String,zip_code:String){

        apiInterface.editProfile(
            accessToken = "accessToken",
            user_id = RequestBody.create(MediaType.parse("text/plain"), user_id),
            full_name = RequestBody.create(MediaType.parse("text/plain"), full_name),
            email = RequestBody.create(MediaType.parse("text/plain"), email),
            image = image?.let {
                MultipartBody.Part.createFormData(
                    "image", it.name,
                    RequestBody.create(MediaType.parse("image/*"), it)
                )
            },
            name = RequestBody.create(MediaType.parse("text/plain"), name),
            mobile_number = RequestBody.create(MediaType.parse("text/plain"), mobile_number),
            alt_mobile_number = RequestBody.create(MediaType.parse("text/plain"), alt_mobile_number),
            country_name = RequestBody.create(MediaType.parse("text/plain"), country_name),
            city_name = RequestBody.create(MediaType.parse("text/plain"), city_name),
            building_name = RequestBody.create(MediaType.parse("text/plain"), building_name),
            street = RequestBody.create(MediaType.parse("text/plain"), street),
            near_location = RequestBody.create(MediaType.parse("text/plain"), near_location),
            zip_code = RequestBody.create(MediaType.parse("text/plain"), zip_code)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccessEditProfile(it)},{
                onErrorCreateProfile(it)})
    }

    fun setEditProfile(accessToken: String,user_id:String,full_name:String,email: String,image: File?){

        apiInterface.editProfile(
            accessToken = accessToken,
            user_id = RequestBody.create(MediaType.parse("text/plain"), user_id),
            full_name = RequestBody.create(MediaType.parse("text/plain"), full_name),
            email = RequestBody.create(MediaType.parse("text/plain"), email),
            image = image?.let {
                MultipartBody.Part.createFormData(
                    "image", it.name,
                    RequestBody.create(MediaType.parse("image/*"), it)
                )
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccessEditProfile(it)},{
                onErrorCreateProfile(it)})
    }

    fun setEditProfile(accessToken: String,storeId:Int,callback: Callback<CategoriesDataModel>) {
        apiInterface.getCategory(accessToken,storeId).enqueue(callback)
    }

    private fun onErrorCreateProfile(it: Throwable?) {
        Log.e("onErrorCreateProfile",it?.message.toString())
        mError.value=it
    }

    private fun onSuccessEditProfile(it: ResponseProfileCreate?) {
        Log.e("onSuccessEditProfile",it.toString())
        mResponseCreateProfile.value=it
    }


    fun hitGetCity(accessToken:String, country_id:String){

        apiInterface.getCity(
            accessToken = accessToken,
            country_id=country_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessCity(it)},{
                //mProgess.value = false
                onErrorForgetPassword(it)})

    }

    private fun onSuccessCity(it: ResponseCity?) {
        mResponseCity.value =it

    }

    private fun onErrorForgetPassword(it: Throwable?) {
        mError.value = it
    }

}