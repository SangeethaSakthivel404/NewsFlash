package com.androiddevs.newsflash.di.modules

import android.app.Application
import android.content.Context
import com.androiddevs.newsflash.NewsFlashApplication
import com.androiddevs.newsflash.di.qualifierAnnotations.ApplicationContext
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        SystemServiceModule::class,
        NetworkModule::class, DataModule::class]
)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindApplication(application: NewsFlashApplication): Application

    @Binds
    @Singleton
    @ApplicationContext
    abstract fun bindApplicationContext(application: Application): Context
}