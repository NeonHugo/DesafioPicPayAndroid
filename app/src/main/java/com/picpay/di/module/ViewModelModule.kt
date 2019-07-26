package com.picpay.di.module

import androidx.lifecycle.ViewModel
import com.picpay.ui.act000.SplashActivityViewModel
import com.picpay.ui.act000.SplashInteractor
import com.picpay.ui.act001.PeopleListActivityViewModel
import com.picpay.ui.act001.PeopleListInteractor
import com.picpay.ui.act003.CreditCardFormActivityViewModel
import com.picpay.ui.act003.CreditCardFormInteractor
import com.picpay.ui.act004.TransferActivityViewModel
import com.picpay.ui.act004.TransferInteractor
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

/*
* Created By neomatrix on 2019-07-19
*/
@Module
class ViewModelModule {

    @MustBeDocumented
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    fun splashActivityViewModel(splashInteractor: SplashInteractor): ViewModel {
        return SplashActivityViewModel(splashInteractor)
    }

    @Provides
    @IntoMap
    @ViewModelKey(PeopleListActivityViewModel::class)
    fun peopleListActivityViewModel(peopleListInteractor: PeopleListInteractor): ViewModel {
        return PeopleListActivityViewModel(peopleListInteractor)
    }

    @Provides
    @IntoMap
    @ViewModelKey(CreditCardFormActivityViewModel::class)
    fun creditCardFormActivityViewModel(creditCardFormInteractor: CreditCardFormInteractor): ViewModel {
        return CreditCardFormActivityViewModel(creditCardFormInteractor)
    }

    @Provides
    @IntoMap
    @ViewModelKey(TransferActivityViewModel::class)
    fun transferActivityViewModel(transferInteractor: TransferInteractor): ViewModel {
        return TransferActivityViewModel(transferInteractor)
    }

}