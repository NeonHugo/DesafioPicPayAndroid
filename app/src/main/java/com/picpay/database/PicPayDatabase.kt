package com.picpay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.picpay.application.Constants
import com.picpay.dao.CreditCardDao
import com.picpay.dao.PeopleDao
import com.picpay.entity.CreditCard
import com.picpay.entity.People

/*
* Created By neomatrix on 2019-07-12
*/
@Database(
    entities = [
        CreditCard::class,
        People::class
    ],
    version = 1,
    exportSchema = false
)


abstract class PicPayDatabase : RoomDatabase() {

    abstract fun creditCardDao(): CreditCardDao
    abstract fun peopleDao(): PeopleDao

    companion object {

        @Volatile
        private var INSTANCE: PicPayDatabase? = null

        fun getInstance(context: Context): PicPayDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PicPayDatabase::class.java, Constants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}