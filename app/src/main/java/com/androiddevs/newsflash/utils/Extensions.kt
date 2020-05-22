package com.androiddevs.newsflash.utils

import android.content.Context


fun Any.getClassTag(): String = this.javaClass.simpleName

fun Any.getMethodTag(): String =
    getClassTag() + object : Any() {}.javaClass.enclosingMethod?.name


fun Float.toDp(context: Context): Float {
    return context.resources.displayMetrics.density * this
}

