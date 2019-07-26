package com.picpay.ui.act001

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.picpay.application.Constants
import com.picpay.entity.People
import com.picpay.repository.CreditCardRepository
import com.picpay.repository.PeopleRepository
import com.picpay.tools.getBackGroundThreadExecutor
import com.picpay.tools.getMainThreadExecutor

/*
* Created By neomatrix on 2019-07-19
*/
class PeopleListInteractor(
    private val creditCardRepository: CreditCardRepository,
    private val peopleRepository: PeopleRepository
) {

    private val executorMainTE = getMainThreadExecutor()
    private val executorBackTE = getBackGroundThreadExecutor()

    private var response = MutableLiveData<PeopleOutResponse>()

    fun getResponse(): MutableLiveData<PeopleOutResponse> {
        return response
    }

    fun peopleListSync() {
        executorBackTE.execute {
            var peoples = peopleRepository.getPeopleList()

            executorMainTE.execute {
                response.value = PeopleOutResponse.Peoples(peoples)
            }
        }
    }

    fun processPeopleSelected(people: People) {
        executorBackTE.execute {
            val mBundle = Bundle()
            mBundle.putLong(Constants.PEOPLE_ID, people.id)

            var ccCount = creditCardRepository.getCreditCardCount()

            executorMainTE.execute {
                if (ccCount == 0) {
                    response.value = PeopleOutResponse.NavigationCreditCardAdd(mBundle)
                } else {
                    response.value = PeopleOutResponse.NavigationTransfer(mBundle)
                }
            }
        }
    }

}