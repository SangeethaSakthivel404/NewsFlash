package com.androiddevs.newsflash.di.components

import com.androiddevs.newsflash.NewsFlashApplication
import com.androiddevs.newsflash.di.modules.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

        @Component.Builder
        interface Builder {
                @BindsInstance
                fun application(application: NewsFlashApplication): Builder

                fun build(): ApplicationComponent
        }

}