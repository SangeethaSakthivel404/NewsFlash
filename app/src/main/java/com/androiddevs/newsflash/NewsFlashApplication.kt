
package com.androiddevs.newsflash

import android.app.Application
import com.androiddevs.newsflash.di.components.DaggerApplicationComponent


class NewsFlashApplication : Application() {
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()
}