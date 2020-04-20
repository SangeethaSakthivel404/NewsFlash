package com.androiddevs.newsflash.di.builders

import com.androiddevs.newsflash.data.shared_preferences.PreferencesHelper
import com.androiddevs.newsflash.data.shared_preferences.PreferencesHelperImpl
import dagger.Binds
import dagger.Module


@Module
abstract class SharedPreferencesBuilder {

    @Binds
    abstract fun bindsSharedPreferenceHelper(preferencesHelperImpl: PreferencesHelperImpl): PreferencesHelper
}