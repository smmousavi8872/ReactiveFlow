package io.github.smmousavi8872.ui.activity

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
import dagger.hilt.android.AndroidEntryPoint
import io.github.smmousavi8872.ui.layouts.MainActivityLayout
import io.github.smmousavi8872.ui.layouts.ResultActivityLayout
import io.github.smmousavi8872.ui.theme.ReactiveFlowTheme
import io.github.smmousavi8872.ui.viewmodel.ResultActivityViewModel

@AndroidEntryPoint
class ResultActivity : ComponentActivity() {

    private val viewModel: ResultActivityViewModel by viewModels()

    private var _eventType: Int? = null
    private val eventType get() = _eventType!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtras()

        when (eventType) {
            MainActivityLayout.HOT_EVENT_RADIO_BUTTON_KEY -> viewModel.subscribeMessageHotEvent(
                delay = 2000
            ) { event ->
                viewModel.eventMessage.value = event.message
            }

            MainActivityLayout.COLD_EVENT_RADIO_BUTTON_KEY -> viewModel.subscribeMessageColdEvent { event ->
                viewModel.eventMessage.value = event.message
            }
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


    private fun getExtras() {
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