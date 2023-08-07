package io.github.smmousavi8872.reactiveflow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class ReactiveFlowConfig<T : EventFlow>(

    internal var eventClass: Class<T>,

    internal var subscribeOn: CoroutineDispatcher = Dispatchers.IO,

    internal var observeOn: CoroutineDispatcher = Dispatchers.Main,

    internal var onException: (e: Exception) -> Unit = { e -> e.printStackTrace() },

    internal var asHot: Boolean = false,

    internal var publishOnce: Boolean = false,

    internal var subscribeOnce: Boolean = false,

    internal var delayMillis: Long = 0,
)