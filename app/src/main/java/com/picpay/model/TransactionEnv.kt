package com.picpay.model

/*
* Created By neomatrix on 2019-07-25
*/
class TransactionEnv(
    var card_number: String = "",
    var cvv: String = "",
    var value: Double = 0.0,
    var expiry_date: String = "",
    var destination_user_id: Long = -1L
)