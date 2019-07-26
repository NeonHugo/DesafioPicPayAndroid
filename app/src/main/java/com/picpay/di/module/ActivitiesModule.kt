package com.picpay.di.module

import com.picpay.ui.act000.SplashActivity
import com.picpay.ui.act001.PeopleListActivity
import com.picpay.ui.act002.CreditCardAddActivity
import com.picpay.ui.act003.CreditCardFormActivity
import com.picpay.ui.act004.TransferActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun peopleListActivity(): PeopleListActivity

    @ContributesAndroidInjector
    abstract fun creditCardList(): CreditCardAddActivity

    @ContributesAndroidInjector
    abstract fun CreditCardForm(): CreditCardFormActivity

    @ContributesAndroidInjector
    abstract fun transferActivity(): TransferActivity



}