package io.github.smmousavi8872.event

import io.github.smmousavi8872.reactiveflow.ColdEventFlow


data class MessageColdEvent(val id: Int = 0, var message: String) : ColdEventFlow()