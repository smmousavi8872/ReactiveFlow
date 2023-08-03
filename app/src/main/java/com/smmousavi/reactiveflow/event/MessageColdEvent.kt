package com.smmousavi.reactiveflow.event

import com.smmousavi.reactiveflow.flow.ColdEventFlow

data class MessageColdEvent(val message: String) : ColdEventFlow()