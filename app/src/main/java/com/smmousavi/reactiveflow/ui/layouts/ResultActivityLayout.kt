package com.smmousavi.reactiveflow.ui.layouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smmousavi.reactiveflow.ui.viewmodel.ResultActivityViewModel

class ResultActivityLayout {

    @Composable
    @Preview
    fun ResultActivityContent(
        modifier: Modifier = Modifier,
        viewModel: ResultActivityViewModel = viewModel(),
    ) {
        Surface(modifier.fillMaxSize()) {
            val eventMessage by rememberSaveable {
                viewModel.eventMessage
            }

            Text(text = eventMessage, Modifier.padding(vertical = 24.dp, horizontal = 16.dp))
        }
    }
}