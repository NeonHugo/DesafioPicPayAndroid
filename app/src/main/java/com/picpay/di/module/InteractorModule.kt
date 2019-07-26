package com.picpay.di.module

import com.picpay.repository.CreditCardRepository
import com.picpay.repository.PeopleRepository
import com.picpay.ui.act000.SplashInteractor
import com.picpay.ui.act001.PeopleListInteractor
import com.picpay.ui.act003.CreditCardFormInteractor
import com.picpay.ui.act004.TransferInteractor
import dagger.Module
import dagger.Provides

/*
* Created By neomatrix on 2019-07-19
*/
@Module
class InteractorModule {

    @Provides
    fun getSplashInteractor(
        webApi: WebApi,
        peopleRepository: PeopleRepository
    ): SplashInteractor {
        return SplashInteractor(webApi, peopleRepository)
    }

    @Provides
    fun getPeopleListInteractor(
        creditCardRepository: CreditCardRepository,
        peopleRepository: PeopleRepository
    ): PeopleListInteractor {
        return PeopleListInteractor(creditCardRepository, peopleRepository)
    }

    @Provides
    fun getCreditCardFormInteractor(
        creditCardRepository: CreditCardRepository
    ): CreditCardFormInteractor {
        return CreditCardFormInteractor(creditCardRepository)
    }

    @Provides
    fun getTransferInteractor(
        webApi: WebApi,
        peopleRepository: PeopleRepository,
        creditCardRepository: CreditCardRepository
    ): TransferInteractor {
        return TransferInteractor(webApi, peopleRepository, creditCardRepository)
    }

}