package com.pbh.androidbase.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Injectable coroutine dispatcher set used to keep IO and CPU work off callers. */
interface DispatcherProvider {
    /** Dispatcher for CPU-bound work. */
    val default: CoroutineDispatcher

    /** Dispatcher for blocking or IO-bound work. */
    val io: CoroutineDispatcher

    /** Dispatcher for main-thread UI work. */
    val main: CoroutineDispatcher
}

/** Production dispatcher provider backed by Kotlin coroutine defaults. */
class DefaultDispatcherProvider : DispatcherProvider {
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
}
