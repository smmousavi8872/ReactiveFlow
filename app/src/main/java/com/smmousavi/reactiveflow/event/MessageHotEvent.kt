package com.smmousavi.reactiveflow.event

import com.smmousavi.reactiveflow.flow.HotEventFlow

data class MessageHotEvent(val message: String) : HotEventFlow()