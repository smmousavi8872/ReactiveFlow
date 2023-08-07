package io.github.smmousavi8872.event

import io.github.smmousavi8872.reactiveflow.HotEventFlow

data class MessageHotEvent(val message: String) : HotEventFlow()