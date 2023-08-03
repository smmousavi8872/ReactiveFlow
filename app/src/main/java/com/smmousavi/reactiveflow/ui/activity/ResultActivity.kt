package com.smmousavi.reactiveflow.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.smmousavi.reactiveflow.event.MessageColdEvent
import com.smmousavi.reactiveflow.event.MessageHotEvent
import com.smmousavi.reactiveflow.ui.layouts.MainActivityLayout
import com.smmousavi.reactiveflow.ui.layouts.ResultActivityLayout
import com.smmousavi.reactiveflow.ui.theme.ReactiveFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

@AndroidEntryPoint
class ResultActivity : ComponentActivity() {

    private val viewModel: ResultActivityViewModel by viewModels()

    private var _eventType: Int? = null
    private val eventType get() = _eventType!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEventTypeExtra()

        when (eventType) {
            MainActivityLayout.HOT_EVENT_RADIO_BUTTON_KEY -> subscribeHotEvent()
            MainActivityLayout.COLD_EVENT_RADIO_BUTTON_KEY -> subscribeColdEvent()
        }

        setContent {
            ReactiveFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultActivityLayout().ResultActivityContent(viewModel = viewModel)
                }
            }
        }
    }

    private fun subscribeHotEvent(
        subscribeThread: CoroutineDispatcher = Dispatchers.IO,
        observeThread: CoroutineDispatcher = Dispatchers.Main,
        exception: (Exception) -> Unit = { e -> e.printStackTrace() },
        observeOnce: Boolean = false,
        delay: Long = 0,
    ) {
        viewModel.reactiveFlow.onHotEvent(MessageHotEvent::class.java)
            .subscribeOn(subscribeThread)
            .observeOn(observeThread)
            .onException { e -> exception(e) }
            .observeOnce(observeOnce)
            .withDelay(millis = delay)
            .subscribe {
                viewModel.eventMessage.value = it.message
            }
    }

    private fun subscribeColdEvent(
        subscribeThread: CoroutineDispatcher = Dispatchers.IO,
        observeThread: CoroutineDispatcher = Dispatchers.Main,
        exception: (Exception) -> Unit = { e -> e.printStackTrace() },
        observeOnce: Boolean = false,
        delay: Long = 0,
    ) {
        viewModel.reactiveFlow.onColdEvent(MessageColdEvent::class.java)
            .subscribeOn(subscribeThread)
            .observeOn(observeThread)
            .onException { e -> exception(e) }
            .observeOnce(observeOnce)
            .withDelay(millis = delay)
            .subscribe {
                viewModel.eventMessage.value = it.message
            }
    }

    private fun setEventTypeExtra() {
        _eventType = intent.extras?.takeIf { it.containsKey(EXTRA_RADIO_BUTTON_ID_KEY) }
            ?.getInt(EXTRA_RADIO_BUTTON_ID_KEY)
    }

    companion object {
        private const val EXTRA_RADIO_BUTTON_ID_KEY = "ExtraRadioButtonId"

        fun newIntent(origin: Context, radioId: Int) =
            Intent(origin, ResultActivity::class.java).also {
                it.putExtra(EXTRA_RADIO_BUTTON_ID_KEY, radioId)
            }
    }
}