package com.smmousavi.reactiveflow.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class ReactiveFlowConfig<T : EventFlow>(

    var eventClass: Class<T>,

    var subscribeOn: CoroutineDispatcher = Dispatchers.IO,

    var observeOn: CoroutineDispatcher = Dispatchers.Main,

    var onException: (e: Exception) -> Unit = { e -> e.printStackTrace() },

    var asHot: Boolean = false,

    var observeOnce: Boolean = false,

    var delayMillis: Long = 0,
)