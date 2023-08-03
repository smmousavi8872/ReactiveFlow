package com.smmousavi.reactiveflow.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.smmousavi.reactiveflow.event.MessageColdEvent
import com.smmousavi.reactiveflow.event.MessageHotEvent
import com.smmousavi.reactiveflow.flow.ReactiveFlow
import com.smmousavi.reactiveflow.ui.layouts.MainActivityLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
@ViewModelScoped
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val reactiveFlow: ReactiveFlow,
) : AndroidViewModel(application = application) {

    // view states
    var hotRadioSelected = mutableStateOf(true)
    var coldRadioSelected = mutableStateOf(false)
    var messageEditTextInput = mutableStateOf("")

    fun publishHotEvent() {
        reactiveFlow.publishHotEvent(MessageHotEvent(messageEditTextInput.value))
    }

    fun publishColdEvent() {
        reactiveFlow.publishColdEvent(MessageColdEvent(messageEditTextInput.value))
    }
}