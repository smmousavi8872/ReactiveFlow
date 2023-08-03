package com.smmousavi.reactiveflow.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import java.lang.Exception

interface ReactiveFlowBuilder<T : EventFlow> {

    fun publishOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T>

    fun subscribeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T>

    fun onException(onException: (e: Exception) -> Unit): ReactiveFlowBuilderImpel<T>

    fun publishOnce(onceOnly: Boolean): ReactiveFlowBuilderImpel<T>

    fun subscribeOnce(onceOnly: Boolean): ReactiveFlowBuilderImpel<T>

    fun withDelay(millis: Long): ReactiveFlowBuilderImpel<T>

    fun subscribe(onSubscribe: (T) -> Unit): Job
}