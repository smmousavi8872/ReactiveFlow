package com.smmousavi.reactiveflow.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReactiveFlow @Inject constructor() {

    internal val coldEvents = MutableSharedFlow<Pair<ColdEventFlow, FireWrapper>>()
    internal val hotEvents = MutableSharedFlow<Pair<HotEventFlow, FireWrapper>>(1)

    fun <T : ColdEventFlow> onColdEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T> {
        return ReactiveFlowBuilderImpel(this, ReactiveFlowConfig(eventClass, asHot = false))
    }

    fun <T : HotEventFlow> onHotEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T> {
        return ReactiveFlowBuilderImpel(this, ReactiveFlowConfig(eventClass, asHot = true))
    }

    fun publishColdEvent(event: ColdEventFlow) {
        CoroutineScope(Dispatchers.IO).launch {
            coldEvents.emit(Pair(event, FireWrapper(false)))
        }
    }

    fun publishHotEvent(event: HotEventFlow) {
        CoroutineScope(Dispatchers.IO).launch {
            hotEvents.emit(Pair(event, FireWrapper(false)))
        }
    }

    fun cancelEvent(job: Job?) {
        job?.cancelEvent()
    }
}

fun Job.cancelEvent() = this.cancel(null)


internal data class FireWrapper(var fired: Boolean = false)


open class EventFlow


open class ColdEventFlow : EventFlow()


open class HotEventFlow : EventFlow()


class CompositeEventJob {

    private val compositeJobs = hashSetOf<Job>()

    operator fun plus(job: Job) {
        compositeJobs.add(job)
    }

    fun cancelAll() {
        for (job in compositeJobs) {
            job.cancelEvent()
        }
    }
}

