package io.github.smmousavi8872.reactiveflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReactiveFlow @Inject constructor() : ReactiveFlowInterface {

    // receives only the events which are fired after observing
    internal val coldEvents = MutableSharedFlow<ColdEventFlow>()

    // receives the most recent fired event, even when observing after event was fired
    internal val hotEvents = MutableSharedFlow<HotEventFlow>(replay = 1)

    override fun <T : ColdEventFlow> onColdEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T> {
        return ReactiveFlowBuilderImpel(
            this,
            ReactiveFlowConfig(
                eventClass,
                asHot = false
            )
        )
    }

    override fun <T : HotEventFlow> onHotEvent(eventClass: Class<T>): ReactiveFlowBuilderImpel<T> {
        return ReactiveFlowBuilderImpel(
            this,
            ReactiveFlowConfig(
                eventClass,
                asHot = true
            )
        )
    }

    override fun publishColdEvent(event: ColdEventFlow) {
        CoroutineScope(Dispatchers.IO).launch {
            coldEvents.emit(event)
        }
    }

    override fun publishHotEvent(event: HotEventFlow) {
        CoroutineScope(Dispatchers.IO).launch {
            hotEvents.emit(event)
        }
    }

    override fun cancelEvent(job: Job?) {
        job?.cancelEvent()
    }
}

fun Job.cancelEvent() = this.cancel(null)

interface EventFlow

open class ColdEventFlow(internal var fired: Boolean = false) :
    EventFlow

open class HotEventFlow(internal var fired: Boolean = false) :
    EventFlow

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

