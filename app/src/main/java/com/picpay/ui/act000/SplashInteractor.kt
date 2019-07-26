package com.picpay.ui.act000

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.picpay.di.module.WebApi
import com.picpay.entity.People
import com.picpay.repository.PeopleRepository
import com.picpay.tools.UIStatusType
import com.picpay.tools.getBackGroundThreadExecutor
import com.picpay.tools.getMainThreadExecutor

/*
* Created By neomatrix on 2019-07-12
*/
class SplashInteractor(private val webApi: WebApi, private val peopleRepository: PeopleRepository) {

    private val executorMainTE = getMainThreadExecutor()
    private val executorBackTE = getBackGroundThreadExecutor()

    private var response = MutableLiveData<SplashOutResponse>()

    fun getResponse(): MutableLiveData<SplashOutResponse> {
        return response
    }

    fun peopleSync() {
        executorBackTE.execute {
            var peoples: ArrayList<People> = arrayListOf()

            try {
                val responseWeb = webApi.getPeoples().execute()

                if (responseWeb.isSuccessful) {
                    val gson = Gson()

                    val itemType = object : TypeToken<java.util.ArrayList<People>>() {}.type
                    peoples =
                        gson.fromJson(
                            responseWeb.body()?.string(),
                            itemType
                        )

                    peopleRepository.processPeopleList(peoples)
                } else {
                }

            } catch (ex: Exception) {
                peoples = peopleRepository.getPeopleList()
            }

            executorMainTE.execute {
                if (peoples.size > 0) {
                    response.value = SplashOutResponse.Navigation(Bundle())
                } else {
                    response.value = SplashOutResponse.Failure(UIStatusType.ERROR)
                }

            }
        }
    }
}