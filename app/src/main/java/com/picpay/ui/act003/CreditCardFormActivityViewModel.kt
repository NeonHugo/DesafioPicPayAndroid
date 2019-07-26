package com.picpay.ui.act003

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.entity.CreditCard

/*
* Created By neomatrix on 2019-07-19
*/
class CreditCardFormActivityViewModel(
    private val creditCardFormInteractor: CreditCardFormInteractor
) : ViewModel() {

    var response: MutableLiveData<CreditCardFormOutResponse> =
        creditCardFormInteractor.getResponse()

    fun saveCreditCardUseCase(creditCard: CreditCard) {
        creditCardFormInteractor.saveCreditCard(creditCard)
    }

    fun getCreditCardUseCase(id: String) {
        creditCardFormInteractor.getCreditCardById(id)
    }

    fun creditCardSelectedUseCase(people_id: Long) {
        creditCardFormInteractor.processCreditCardSelected(people_id)
    }
}