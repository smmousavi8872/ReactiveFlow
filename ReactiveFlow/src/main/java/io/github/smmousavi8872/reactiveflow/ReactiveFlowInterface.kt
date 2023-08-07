package io.github.smmousavi8872.reactiveflow

import kotlinx.coroutines.Job

interface ReactiveFlowInterface {
    fun <T : ColdEventFlow> onColdEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T>

    fun <T : HotEventFlow> onHotEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T>

    fun publishColdEvent(event: ColdEventFlow)

    fun publishHotEvent(event: HotEventFlow)

    fun cancelEvent(job: Job?)
}