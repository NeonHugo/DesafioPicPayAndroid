package com.picpay.ui.act004

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.model.TransactionEnv

/*
* Created By neomatrix on 2019-07-19
*/
class TransferActivityViewModel(
    private val transferInteractor: TransferInteractor
) : ViewModel() {

    var response: MutableLiveData<TransferOutResponse> =
        transferInteractor.getResponse()

    fun getPeopleUseCase(id: Long) {
        transferInteractor.getPeopleById(id)
    }

    fun getCreditCardUseCase() {
        transferInteractor.getCreditCard()
    }

    fun saveCreditCardUseCase(env: TransactionEnv) {
        transferInteractor.transactionSend(env)
    }


}