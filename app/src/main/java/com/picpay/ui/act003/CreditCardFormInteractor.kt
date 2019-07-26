package com.picpay.ui.act003

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.picpay.R
import com.picpay.application.Constants
import com.picpay.entity.CreditCard
import com.picpay.repository.CreditCardRepository
import com.picpay.tools.getBackGroundThreadExecutor
import com.picpay.tools.getMainThreadExecutor

/*
* Created By neomatrix on 2019-07-25
*/
class CreditCardFormInteractor(
    private val creditCardRepository: CreditCardRepository
) {

    private val executorMainTE = getMainThreadExecutor()
    private val executorBackTE = getBackGroundThreadExecutor()

    private var response = MutableLiveData<CreditCardFormOutResponse>()

    fun getResponse(): MutableLiveData<CreditCardFormOutResponse> {
        return response
    }

    fun saveCreditCard(creditCard: CreditCard) {
        executorBackTE.execute {

            creditCardRepository.cleanCreditCards()

            val saveStatus = creditCardRepository.saveCreditCard(creditCard)

            executorMainTE.execute {
                if (saveStatus) {
                    response.value = CreditCardFormOutResponse.CreditCardSave(creditCard)
                } else {
                    response.value = CreditCardFormOutResponse.Failure(R.string.creditcardaform_error_save)
                }
            }
        }
    }

    fun getCreditCardById(id: String) {
        executorBackTE.execute {
            val creditCard = creditCardRepository.getCreditCardById(id)

            executorMainTE.execute {
                response.value = CreditCardFormOutResponse.CreditCardGet(creditCard)
            }
        }
    }

    fun processCreditCardSelected(people_id: Long) {
        executorBackTE.execute {
            val mBundle = Bundle()
            mBundle.putLong(Constants.PEOPLE_ID, people_id)

            executorMainTE.execute {
                response.value = CreditCardFormOutResponse.NavigationTransfer(mBundle)
            }
        }
    }
}