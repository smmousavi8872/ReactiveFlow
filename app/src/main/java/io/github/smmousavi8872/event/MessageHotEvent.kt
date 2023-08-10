package io.github.smmousavi8872.event

import io.github.smmousavi8872.reactiveflow.HotEventFlow

data class MessageHotEvent(val id: Int = 0, val message: String) : HotEventFlow()