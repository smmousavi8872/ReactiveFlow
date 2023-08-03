package com.smmousavi.reactiveflow.ui.activity

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
import com.smmousavi.reactiveflow.ui.theme.ReactiveFlowTheme
import com.smmousavi.reactiveflow.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReactiveFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityLayout().MainActivityContent(
                        viewModel = viewModel,
                    ) {
                        //onNextClick
                        val eventKey = when {
                            viewModel.hotRadioSelected.value -> {
                                viewModel.publishHotEvent()
                                MainActivityLayout.HOT_EVENT_RADIO_BUTTON_KEY
                            }

                            viewModel.coldRadioSelected.value -> {
                                viewModel.publishColdEvent()
                                MainActivityLayout.COLD_EVENT_RADIO_BUTTON_KEY
                            }

                            else -> {
                                viewModel.publishHotEvent()
                                MainActivityLayout.HOT_EVENT_RADIO_BUTTON_KEY
                            }
                        }

                        startActivity(ResultActivity.newIntent(this, eventKey))
                    }
                }
            }
        }
    }

}