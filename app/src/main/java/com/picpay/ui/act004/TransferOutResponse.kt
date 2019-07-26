package com.picpay.ui.act004

import com.picpay.entity.CreditCard
import com.picpay.entity.People
import com.picpay.model.TransactionReponse

/*
* Created By neomatrix on 2019-07-25
*/
sealed class TransferOutResponse {

    data class PeopleGet(
        var people: People?
    ) : TransferOutResponse()

    data class CreditCardGet(
        var creditCard: CreditCard?
    ) : TransferOutResponse()

    data class TransferSend(
        var transaction: TransactionReponse?
    ) : TransferOutResponse()

    data class Failure(
        var errorSendResouce: Int
    ) : TransferOutResponse()

}