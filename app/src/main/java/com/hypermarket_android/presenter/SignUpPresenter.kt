package com.hypermarket_android.presenter

class SignUpPresenter(signUpViewInterface: SignUpViewInterface) {

    /*var signUpViewInterface: SignUpViewInterface?=null


    init {
         this.signUpViewInterface=signUpViewInterface
    }


    fun SignupObservable(jsonObject: JsonObject?): Observable<signUp?>? {
        return NetworkClient.getRetrofit().create(ApiInterface::class.java)
            .UsersSignup(jsonObject)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun SignupObserver(): DisposableObserver<signUp?>? {
        return object : DisposableObserver<signUp?>() {
            override fun onNext(signUp: signUp) {
                signUpViewInterface?.signUp(signUp)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                signUpViewInterface?.displayError("Error something")
                signUpViewInterface?.hideProgressBar()
            }

            override fun onComplete() {
                Log.e("msg", "Completed")
                signUpViewInterface?.hideProgressBar()
            }
        }
    }



    override fun signUpuser(jsonObject: JsonObject) {
        SignupObservable(jsonObject)!!.subscribeWith<DisposableObserver<signUp?>?>(SignupObserver())
    */
//}

}