package com.picpay.dao

import androidx.room.Dao
import androidx.room.Query
import com.picpay.database.PicPayDatabase
import com.picpay.entity.CreditCard

/*
* Created By neomatrix on 2019-07-22
*/
@Dao
abstract class CreditCardDao : BaseCreditCardDao {
    var picPayDatabase: PicPayDatabase? = null

    override fun upsert(creditCard: CreditCard): Boolean {
        var results = 0L

        try {
            picPayDatabase?.runInTransaction {
                results = update(creditCard).toLong()

                if (results == 0L) {
                    results = insert(creditCard)
                }
            }

        } catch (ex: Exception) {
        }

        return results > 0
    }

    @Query(
        "SELECT * from credit_card_tb where lower(credit_card_number) = lower(:id) " +
                " LIMIT 1"
    )
    abstract override fun getCreditCardById(id: String): CreditCard?

    @Query("SELECT * from credit_card_tb order by credit_card_number")
    abstract override fun getAllCreditCards(): List<CreditCard>

    @Query("SELECT count(*) as total from credit_card_tb")
    abstract override fun getCreditCardCount(): Int

    @Query("DELETE FROM credit_card_tb")
    abstract override fun clearAllData()
}