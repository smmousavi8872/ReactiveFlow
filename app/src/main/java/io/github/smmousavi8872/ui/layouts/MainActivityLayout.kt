package io.github.smmousavi8872.ui.layouts

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.smmousavi8872.R
import io.github.smmousavi8872.ui.viewmodel.MainActivityViewModel

class MainActivityLayout {

    @Composable
    @Preview
    fun MainActivityContent(
        modifier: Modifier = Modifier,
        viewModel: MainActivityViewModel = viewModel(),
        onSendClick: () -> Unit = {},
    ) {
        var hotEventRadioSelected by rememberSaveable {
            viewModel.hotRadioSelected
        }

        var coldEventRadioSelected by rememberSaveable {
            viewModel.coldRadioSelected
        }

        Surface(modifier.fillMaxSize()) {
            Column(modifier.padding(vertical = 32.dp, horizontal = 24.dp)) {
                Text(
                    text = stringResource(R.string.event_message_hint),
                    modifier.padding(bottom = 16.dp)
                )

                ReactiveMessageTextField(modifier = modifier, viewModel = viewModel)

                Row(
                    modifier.padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EventTypeRadioButton(
                        "Hot Event",
                        radioSelected = hotEventRadioSelected,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (!hotEventRadioSelected) {
                            hotEventRadioSelected = true
                            coldEventRadioSelected = false
                        }
                    }
                    EventTypeRadioButton(
                        "Cold Event",
                        radioSelected = coldEventRadioSelected,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (!coldEventRadioSelected) {
                            coldEventRadioSelected = true
                            hotEventRadioSelected = false
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                ElevatedButton(
                    onClick = onSendClick,
                    modifier.fillMaxWidth()
                ) {
                    Text(text = "Send")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReactiveMessageTextField(modifier: Modifier = Modifier, viewModel: MainActivityViewModel) {
        var inputText by rememberSaveable { viewModel.messageEditTextInput }

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = {
                Text(stringResource(R.string.event_flow_hint))
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )
    }

    @Composable
    fun EventTypeRadioButton(
        label: String,
        modifier: Modifier = Modifier,
        radioSelected: Boolean,
        onClick: () -> Unit,
    ) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = radioSelected,
                onClick = onClick,
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                colors = RadioButtonDefaults.colors()
            )
            Text(text = label)
        }
    }

    companion object {
        const val HOT_EVENT_RADIO_BUTTON_KEY = 1
        const val COLD_EVENT_RADIO_BUTTON_KEY = 2
    }
}