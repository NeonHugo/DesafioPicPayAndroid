package com.picpay.dao

import com.picpay.base.BaseDao
import com.picpay.entity.CreditCard

/*
* Created By neomatrix on 2019-07-12
*/
interface BaseCreditCardDao : BaseDao<CreditCard> {
    fun upsert(order: CreditCard): Boolean

    fun getCreditCardById(id: String): CreditCard?

    fun getAllCreditCards(): List<CreditCard>

    fun getCreditCardCount(): Int

    fun clearAllData()
}