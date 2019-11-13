@file:Suppress("MemberVisibilityCanBePrivate")

package com.androiddevs.newsflash.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.androiddevs.newsflash.di.components.ActivityComponent

abstract class BaseActivity : AppCompatActivity() {

    private var mActivityComponent: ActivityComponent? = null

}