package com.picpay.ui.act001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.entity.People

/*
* Created By neomatrix on 2019-07-19
*/
class PeopleListActivityViewModel(
    private val peopleListInteractor: PeopleListInteractor
) : ViewModel() {

    var response: MutableLiveData<PeopleOutResponse> =
        peopleListInteractor.getResponse()

    fun peopleListUseCase() {
        peopleListInteractor.peopleListSync()
    }

    fun peopleSeletectedUseCase(people: People) {
        peopleListInteractor.processPeopleSelected(people)
    }

}