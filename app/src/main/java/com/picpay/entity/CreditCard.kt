package com.picpay.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Created By neomatrix on 2019-07-22
*/
@Entity(tableName = "credit_card_tb")
data class CreditCard(
    @PrimaryKey
    @ColumnInfo(name = "credit_card_number")
    var creditCardNumber: String = "",
    var name: String = "",
    @ColumnInfo(name = "expiration_date")
    var expirationDate: String = "",
    var cvv: String = ""
)