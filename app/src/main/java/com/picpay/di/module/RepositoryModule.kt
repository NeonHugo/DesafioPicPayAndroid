package com.picpay.di.module

import com.picpay.dao.CreditCardDao
import com.picpay.dao.PeopleDao
import com.picpay.repository.CreditCardRepository
import com.picpay.repository.PeopleRepository
import dagger.Module
import dagger.Provides

/*
* Created By neomatrix on 2019-07-22
*/
@Module
class RepositoryModule {

    @Provides
    fun getCreditCardRepository(creditCardDao: CreditCardDao): CreditCardRepository {
        return CreditCardRepository(creditCardDao)
    }

    @Provides
    fun getPeopleRepository(peopleDao: PeopleDao): PeopleRepository {
        return PeopleRepository(peopleDao)
    }

}