package com.picpay.model

import com.picpay.entity.People
import java.io.Serializable

/*
* Created By neomatrix on 2019-07-25
*/
class TransactionReponse(
    var id: Long = -1L,
    var timestamp: Long = -1L,
    var value: Double = 0.0,
    var destination_user: People = People(),
    var success: Boolean = false,
    var status: String = "",
    var creditCardNumber: String = ""
) : Serializable