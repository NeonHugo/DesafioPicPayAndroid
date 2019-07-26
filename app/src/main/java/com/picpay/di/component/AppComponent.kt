package com.picpay.di.component

import android.app.Application
import com.picpay.di.AppBase
import com.picpay.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/*
* Created By neomatrix on 3/28/19
*/
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DataBaseModule::class,
        NetworkingModule::class,
        RepositoryModule::class,
        InteractorModule::class,
        ActivitiesModule::class
    ]
)
interface AppComponent : AndroidInjector<AppBase> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: AppBase)
}