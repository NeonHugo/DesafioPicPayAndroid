package com.picpay.dao

import androidx.room.Dao
import androidx.room.Query
import com.picpay.application.Constants
import com.picpay.database.PicPayDatabase
import com.picpay.entity.People


/*
* Created By neomatrix on 2019-07-22
*/
@Dao
abstract class PeopleDao : BasePeopleDao {
    var picPayDatabase: PicPayDatabase? = null

    override fun upsert(people: People): Boolean {
        var results = 0L

        try {
            picPayDatabase?.runInTransaction {
                results = update(people).toLong()

                if (results == 0L) {
                    results = insert(people)
                }
            }

        } catch (ex: Exception) {
        }

        return results > 0
    }

    override fun upsert(peoples: List<People>) {
        var results: Boolean

        picPayDatabase?.runInTransaction {
            for (people in peoples) {
                results = upsert(people)

                if (!results) {
                    throw Exception("${Constants.DATABASE_ERROR} / People ${people.id}")
                }
            }
        }
    }

    @Query(
        "SELECT * from people_tb where people_id = :id " +
                " LIMIT 1"
    )
    abstract override fun getPeopleById(id: Long): People?

    @Query("SELECT * from people_tb order by name")
    abstract override fun getAllPeoples(): List<People>

    @Query("SELECT count(*) as total from people_tb")
    abstract override fun getPeopleCount(): Int

    @Query("DELETE FROM people_tb")
    abstract override fun clearAllData()
}