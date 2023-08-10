package io.github.smmousavi8872.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.smmousavi8872.event.MessageColdEvent
import io.github.smmousavi8872.event.MessageHotEvent
import io.github.smmousavi8872.reactiveflow.CompositeEventJob
import io.github.smmousavi8872.reactiveflow.ReactiveFlow
import io.github.smmousavi8872.reactiveflow.cancelEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val reactiveFlow: ReactiveFlow,
) : AndroidViewModel(application = application) {

    // view states
    var hotRadioSelected = mutableStateOf(true)
    var coldRadioSelected = mutableStateOf(false)
    var messageEditTextInput = mutableStateOf("")

    // when you want to cancel all events observations at the same time
    private val compositeEventJob = CompositeEventJob()

    // when you want to cancel each event independently
    private var messageHotEventJob: Job? = null
    private var messageColdEventJob: Job? = null
    private var onceObservableEvent = MessageColdEvent(message = messageEditTextInput.value)

    fun publishHotEvent() {
        reactiveFlow.publishHotEvent(MessageHotEvent(message = messageEditTextInput.value))
    }

    fun publishColdEvent() {
        onceObservableEvent.message = messageEditTextInput.value
        reactiveFlow.publishColdEvent(onceObservableEvent)
    }

    fun subscribeMessageHotEvent(
        subscribeThread: CoroutineDispatcher = Dispatchers.IO,
        observeThread: CoroutineDispatcher = Dispatchers.Main,
        exception: (Exception) -> Unit = { e -> e.printStackTrace() },
        observeOnce: Boolean = false,
        delay: Long = 0,
        onSubscribe: (MessageHotEvent) -> Unit,
    ) {
        messageHotEventJob = reactiveFlow.onHotEvent(MessageHotEvent::class.java)
            .publishOn(subscribeThread)
            .subscribeOn(observeThread)
            .onException { e -> exception(e) }
            .subscribeOnce(observeOnce)
            .withDelay(millis = delay)
            .subscribe {
                onSubscribe(it)
            }.also { compositeEventJob + it }
    }

    fun subscribeMessageColdEvent(
        subscribeThread: CoroutineDispatcher = Dispatchers.Main,
        observeThread: CoroutineDispatcher = Dispatchers.Main,
        exception: (Exception) -> Unit = { e -> e.printStackTrace() },
        observeOnce: Boolean = false,
        delay: Long = 0,
        onSubscribe: (MessageColdEvent) -> Unit,
    ) {
        messageColdEventJob = reactiveFlow.onColdEvent(MessageColdEvent::class.java)
            .publishOn(subscribeThread)
            .subscribeOn(observeThread)
            .onException { e -> exception(e) }
            .subscribeOnce(observeOnce)
            .withDelay(millis = delay)
            .subscribe {
                onSubscribe(it)
            }.also { compositeEventJob + it }
    }

    override fun onCleared() {
        super.onCleared()
        // cancel all events at once
        compositeEventJob.cancelAll()
        // cancel events independently
        messageColdEventJob?.cancelEvent()
    }
}