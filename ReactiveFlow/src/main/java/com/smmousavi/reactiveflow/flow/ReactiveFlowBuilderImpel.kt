package com.smmousavi.reactiveflow.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class ReactiveFlowBuilderImpel<T : EventFlow> @Inject constructor(
    private val reactiveFlow: ReactiveFlow,
    private val configuration: ReactiveFlowConfig<T>,
) : ReactiveFlowBuilder<T> {

    override fun subscribeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T> {
        configuration.subscribeOn = dispatcher
        return this
    }

    override fun observeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T> {
        configuration.observeOn = dispatcher
        return this
    }

    override fun onException(onException: (e: Exception) -> Unit): ReactiveFlowBuilderImpel<T> {
        configuration.onException = onException
        return this
    }

    override fun observeOnce(observeOnce: Boolean): ReactiveFlowBuilderImpel<T> {
        configuration.observeOnce = observeOnce
        return this
    }

    override fun withDelay(millis: Long): ReactiveFlowBuilderImpel<T> {
        configuration.delayMillis = millis
        return this
    }

    override fun subscribe(eventAction: (T) -> Unit): Job {
        return runBlocking {
            if (configuration.asHot) {
                subscribeHot(eventAction)
            } else {
                subscribeCold(eventAction)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : HotEventFlow> subscribeHot(eventAction: (T) -> Unit): Job {
        return CoroutineScope(configuration.subscribeOn + SupervisorJob()).launch {
            reactiveFlow.hotEvents.asSharedFlow()
                .filter { it.first.javaClass == configuration.eventClass }
                .catch { configuration.onException }
                .cancellable()
                .collectLatest {
                    val event = it.first as T
                    val fireWrapper = it.second
                    var mainJob: Job? = null

                    if (configuration.delayMillis > 0) delay(configuration.delayMillis)

                    if (configuration.observeOnce) {
                        if (fireWrapper.fired.not()) {
                            mainJob = CoroutineScope(configuration.observeOn).launch {
                                eventAction(event)
                            }
                            fireWrapper.fired = true
                        }
                    } else {
                        mainJob = CoroutineScope(configuration.observeOn).launch {
                            eventAction(event)
                        }
                    }
                    mainJob?.join()
                    mainJob?.cancel()
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : ColdEventFlow> subscribeCold(eventAction: (T) -> Unit): Job {
        return CoroutineScope(configuration.subscribeOn + SupervisorJob()).launch {
            reactiveFlow.coldEvents.asSharedFlow()
                .filter { it.first.javaClass == configuration.eventClass }
                .catch { configuration.onException }
                .cancellable()
                .collectLatest {
                    val event = it.first as T
                    val fireWrapper = it.second
                    var mainJob: Job? = null

                    if (configuration.delayMillis > 0) delay(configuration.delayMillis)

                    if (configuration.observeOnce) {
                        if (fireWrapper.fired.not()) {
                            mainJob = CoroutineScope(configuration.observeOn).launch {
                                eventAction(event)
                            }
                            fireWrapper.fired = true
                        }
                    } else {
                        mainJob = CoroutineScope(configuration.observeOn).launch {
                            eventAction(event)
                        }
                    }
                    mainJob?.join()
                    mainJob?.cancel()
                }
        }
    }
}

