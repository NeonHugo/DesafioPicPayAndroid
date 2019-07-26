package com.picpay.ui.act004

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.picpay.R
import com.picpay.di.module.WebApi
import com.picpay.entity.CreditCard
import com.picpay.model.TransactionEnv
import com.picpay.model.TransactionRec
import com.picpay.repository.CreditCardRepository
import com.picpay.repository.PeopleRepository
import com.picpay.tools.getBackGroundThreadExecutor
import com.picpay.tools.getMainThreadExecutor
import okhttp3.RequestBody

/*
* Created By neomatrix on 2019-07-25
*/
class TransferInteractor(
    private val webApi: WebApi,
    private val peopleRepository: PeopleRepository,
    private val creditCardRepository: CreditCardRepository
) {

    private val executorMainTE = getMainThreadExecutor()
    private val executorBackTE = getBackGroundThreadExecutor()

    private var response = MutableLiveData<TransferOutResponse>()

    fun getResponse(): MutableLiveData<TransferOutResponse> {
        return response
    }

    fun getPeopleById(id: Long) {
        executorBackTE.execute {
            val people = peopleRepository.getPeopleById(id)

            executorMainTE.execute {
                response.value = TransferOutResponse.PeopleGet(people)
            }
        }
    }

    fun getCreditCard() {
        executorBackTE.execute {
            val creditCardList = creditCardRepository.getCreditCardList()
            var creditCard: CreditCard? = creditCardList[0]

            executorMainTE.execute {
                response.value = TransferOutResponse.CreditCardGet(creditCard)
            }
        }
    }

    fun transactionSend(env: TransactionEnv) {
        executorBackTE.execute {
            var transaction: TransactionRec? = null

            try {

                val gson = Gson()
                val body = RequestBody.create(
                    okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(env)
                )

                val responseWeb = webApi.execTransaction(body).execute()

                if (responseWeb.isSuccessful) {
                    transaction =
                        gson.fromJson(
                            responseWeb.body()?.string(),
                            TransactionRec::class.java
                        )
                } else {
                }

            } catch (ex: Exception) {
                var resposta = ex.message
                var i = 10
            }

            executorMainTE.execute {
                if (transaction != null) {
                    response.value = TransferOutResponse.TransferSend(transaction.transaction)
                } else {
                    response.value = TransferOutResponse.Failure(R.string.transfer_error_send)
                }
            }
        }
    }
}