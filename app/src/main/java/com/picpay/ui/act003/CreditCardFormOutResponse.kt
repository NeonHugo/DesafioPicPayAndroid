package com.picpay.ui.act003

import android.os.Bundle
import com.picpay.entity.CreditCard

/*
* Created By neomatrix on 2019-07-25
*/
sealed class CreditCardFormOutResponse {

    data class CreditCardSave(
        var creditCard: CreditCard
    ) : CreditCardFormOutResponse()

    data class Failure(
        var errorSaveResouce: Int
    ) : CreditCardFormOutResponse()

    data class CreditCardGet(
        var creditCard: CreditCard?
    ) : CreditCardFormOutResponse()

    data class NavigationTransfer(
        var bundle: Bundle
    ) : CreditCardFormOutResponse()
}