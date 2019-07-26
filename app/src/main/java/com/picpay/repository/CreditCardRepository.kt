package com.picpay.repository

import com.picpay.dao.CreditCardDao
import com.picpay.entity.CreditCard


/*
* Created By neomatrix on 2019-07-22
*/
class CreditCardRepository(private val creditCardDao: CreditCardDao) {

    fun saveCreditCard(creditCard: CreditCard): Boolean {
        return creditCardDao.upsert(creditCard)
    }

    fun getCreditCardById(id: String): CreditCard? {
        return creditCardDao.getCreditCardById(id)
    }

    fun getCreditCardList(): ArrayList<CreditCard> {
        return creditCardDao.getAllCreditCards() as ArrayList<CreditCard>
    }

    fun getCreditCardCount(): Int {
        return creditCardDao.getCreditCardCount()
    }

    fun cleanCreditCards() {
        creditCardDao.clearAllData()
    }

}