package com.androiddevs.newsflash.data.shared_preferences

interface PreferencesHelper {

    fun setLoggedIn()

    fun logout()

    fun getLoggedIn():Boolean
}