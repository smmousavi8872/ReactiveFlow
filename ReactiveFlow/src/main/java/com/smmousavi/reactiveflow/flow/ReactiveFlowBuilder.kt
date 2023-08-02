package com.smmousavi.reactiveflow.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import java.lang.Exception

interface ReactiveFlowBuilder<T : EventFlow> {

    fun subscribeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T>

    fun observeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T>

    fun onException(onException: (e: Exception) -> Unit): ReactiveFlowBuilderImpel<T>

    fun observeOnce(): ReactiveFlowBuilderImpel<T>

    fun subscribe(eventAction: (T) -> Unit): Job
}