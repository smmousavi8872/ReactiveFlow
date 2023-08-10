package io.github.smmousavi8872.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.smmousavi8872.ui.layouts.MainActivityLayout
import io.github.smmousavi8872.ui.theme.ReactiveFlowTheme
import io.github.smmousavi8872.ui.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // subscribe to a events before publishing them
        viewModel.subscribeMessageColdEvent {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.subscribeMessageHotEvent(delay = 2000) {
            Toast.makeText(this, it.message + "from MainActivity", Toast.LENGTH_SHORT)
                .show()
        }

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
                        when {
                            viewModel.hotRadioSelected.value -> {
                                viewModel.publishHotEvent()
                                startResultActivity()
                            }

                            viewModel.coldRadioSelected.value -> {
                                viewModel.publishColdEvent()
                            }

                            else -> {
                                viewModel.publishHotEvent()
                                startResultActivity()

                            }
                        }
                    }
                }
            }
        }
    }

    private fun startResultActivity() = startActivity(
        ResultActivity.newIntent(
            this,
            MainActivityLayout.HOT_EVENT_RADIO_BUTTON_KEY
        )
    )

}