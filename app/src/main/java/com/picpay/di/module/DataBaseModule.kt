package com.picpay.di.module

import android.content.Context
import com.picpay.dao.CreditCardDao
import com.picpay.dao.PeopleDao
import com.picpay.database.PicPayDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
* Created By neomatrix on 2019-07-12
*/
@Module
class DataBaseModule {

    // DataBase
    @Singleton
    @Provides
    fun providePicPayDatabase(context: Context): PicPayDatabase {
        return PicPayDatabase.getInstance(context)
    }

    // Dao
    @Singleton
    @Provides
    fun provideCreditCardDao(picPayDatabase: PicPayDatabase): CreditCardDao {
        var creditCardDao = picPayDatabase.creditCardDao()
        creditCardDao.picPayDatabase = picPayDatabase

        return creditCardDao
    }

    @Singleton
    @Provides
    fun providePeopleDao(picPayDatabase: PicPayDatabase): PeopleDao {
        var peopleDao = picPayDatabase.peopleDao()
        peopleDao.picPayDatabase = picPayDatabase

        return peopleDao
    }

}