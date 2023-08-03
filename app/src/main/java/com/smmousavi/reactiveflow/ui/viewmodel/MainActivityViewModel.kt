package com.smmousavi.reactiveflow.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.smmousavi.reactiveflow.flow.ReactiveFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    val reactiveFlow: ReactiveFlow,
) : AndroidViewModel(application = application) {

    // view states
    var hotRadioSelected = mutableStateOf(true)
    var coldRadioSelected = mutableStateOf(false)
    var messageEditTextInput = mutableStateOf("")
}