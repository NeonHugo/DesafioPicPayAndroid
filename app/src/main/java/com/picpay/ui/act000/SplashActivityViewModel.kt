package com.picpay.ui.act000

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
* Created By neomatrix on 2019-07-19
*/
class SplashActivityViewModel(
    private val splashInteractor: SplashInteractor
) : ViewModel() {

    var response: MutableLiveData<SplashOutResponse> =
        splashInteractor.getResponse()

    fun peopleListUseCase() {
        splashInteractor.peopleSync()
    }
}