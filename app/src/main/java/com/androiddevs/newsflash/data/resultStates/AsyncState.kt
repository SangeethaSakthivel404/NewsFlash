package com.androiddevs.newsflash.data.resultStates

sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val error: Throwable) : AsyncState()
}