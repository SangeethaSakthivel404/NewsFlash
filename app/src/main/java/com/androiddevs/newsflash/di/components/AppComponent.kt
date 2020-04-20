package com.androiddevs.newsflash.di.components

import android.app.Application
import com.androiddevs.newsflash.di.builders.SharedPreferencesBuilder
import com.androiddevs.newsflash.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [SharedPreferencesBuilder::class, AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}