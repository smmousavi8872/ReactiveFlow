package io.github.smmousavi8872.event

import io.github.smmousavi8872.reactiveflow.ColdEventFlow


data class MessageColdEvent(var message: String) : ColdEventFlow()