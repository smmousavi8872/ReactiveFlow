package io.github.smmousavi8872.reactiveflow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception

class ReactiveFlowBuilderImpel<T : EventFlow> constructor(
    private val reactiveFlow: ReactiveFlow,
    private val configuration: ReactiveFlowConfig<T>,
) : ReactiveFlowBuilder<T> {

    override fun publishOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T> {
        configuration.subscribeOn = dispatcher
        return this
    }

    override fun subscribeOn(dispatcher: CoroutineDispatcher): ReactiveFlowBuilderImpel<T> {
        configuration.observeOn = dispatcher
        return this
    }

    override fun onException(onException: (e: Exception) -> Unit): ReactiveFlowBuilderImpel<T> {
        configuration.onException = onException
        return this
    }

    override fun publishOnce(onceOnly: Boolean): ReactiveFlowBuilderImpel<T> {
        configuration.publishOnce = onceOnly
        return this
    }

    override fun subscribeOnce(onceOnly: Boolean): ReactiveFlowBuilderImpel<T> {
        configuration.subscribeOnce = onceOnly
        return this
    }

    override fun withDelay(millis: Long): ReactiveFlowBuilderImpel<T> {
        configuration.delayMillis = millis
        return this
    }

    override fun subscribe(onSubscribe: (T) -> Unit): Job {
        return runBlocking {
            if (configuration.asHot) {
                subscribeHot(onSubscribe)
            } else {
                subscribeCold(onSubscribe)
            }
        }
    }

    private suspend inline fun <reified T : HotEventFlow> subscribeHot(crossinline onSubscribe: (T) -> Unit): Job {
        return CoroutineScope(configuration.subscribeOn + SupervisorJob()).launch {
            reactiveFlow.hotEvents.asSharedFlow()
                .filter { it.javaClass == configuration.eventClass }
                .catch { configuration.onException }
                .cancellable()
                .collectLatest { event: HotEventFlow ->
                    event as T
                    var mainJob: Job? = null

                    // handle delayed subscription
                    if (configuration.delayMillis > 0) {
                        delay(configuration.delayMillis)
                    }

                    // handle observe once state
                    if (configuration.subscribeOnce) {
                        if (event.fired.not()) {
                            mainJob = CoroutineScope(configuration.observeOn).launch {
                                onSubscribe(event)
                            }
                        }
                    } else {
                        mainJob = CoroutineScope(configuration.observeOn).launch {
                            onSubscribe(event)
                        }
                    }
                    event.fired = true
                    mainJob?.join()
                    mainJob?.cancel()
                }
        }
    }

    private suspend inline fun <reified T : ColdEventFlow> subscribeCold(crossinline onSubscribe: (T) -> Unit): Job {
        return CoroutineScope(configuration.subscribeOn + SupervisorJob()).launch {
            reactiveFlow.coldEvents.asSharedFlow()
                .filter { it.javaClass == configuration.eventClass }
                .catch { configuration.onException }
                .cancellable()
                .collectLatest { event ->
                    event as T
                    var mainJob: Job? = null

                    // handle delayed subscription
                    if (configuration.delayMillis > 0) {
                        delay(configuration.delayMillis)
                    }

                    // handle observe once state
                    if (configuration.subscribeOnce) {
                        if (event.fired.not()) {
                            mainJob = CoroutineScope(configuration.observeOn).launch {
                                onSubscribe(event)
                            }
                        }
                    } else {
                        mainJob = CoroutineScope(configuration.observeOn).launch {
                            onSubscribe(event)
                        }
                    }
                    event.fired = true
                    mainJob?.join()
                    mainJob?.cancel()
                }
        }
    }

    private suspend fun EventFlow.hasPublishedOnce() =
        reactiveFlow.hotEvents.toList(mutableListOf(this))
            .map { event -> event.javaClass }
            .contains(this.javaClass)
}

