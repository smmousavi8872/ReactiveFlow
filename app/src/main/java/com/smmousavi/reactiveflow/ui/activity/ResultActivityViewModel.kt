package com.smmousavi.reactiveflow.ui.activity

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.smmousavi.reactiveflow.flow.ReactiveFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultActivityViewModel @Inject constructor(
    application: Application,
    val reactiveFlow: ReactiveFlow,
) : AndroidViewModel(application) {

    var eventMessage = mutableStateOf("Waiting to receive your event...")

}