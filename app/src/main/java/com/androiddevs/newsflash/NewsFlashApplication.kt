package com.androiddevs.newsflash

import android.app.Application
import com.androiddevs.newsflash.di.components.DaggerAppComponent


class NewsFlashApplication : Application() {

    val appComponent = DaggerAppComponent.builder()
        .application(this).build()
}