# ReactiveFlow
[![Maven Central](https://img.shields.io/maven-central/v/io.github.smmousavi8872.reactiveflow/reactive-flow.svg?color=brightgreen)](https://search.maven.org/artifact/io.github.smmousavi8872.reactiveflow/reactive-flow)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
<br/>This is a reactive (publish/subscribe) event-based library to handle sending objects efficiently through the whole app based on `SharedFlow`. You can wrap any type of object into an `EventFlow` type and send them anywhere in your app(other classes, other packages, or even other modules) through a `ReactiveFlow` without needing any
class instantiation.<br/>
All events are sent in the background thread by default though it can be customized to use any other threads for publishing or subscribing to an Event.

# How to Install?
Simply add the following line to the `dependencies` section of your `build.gradle` file:

```kotlin
implementation 'io.github.smmousavi8872.reactiveflow:reactive-flow:1.0.1.6'
```

# How to use it?
Take these four easy steps to get your `ReactiveFlow` working:
1. **Inject** the `ReactiveFlow` object through `Hilt` or `Dagger` inside both classes in which you are going to publish and subscribe to your event.(It's already provided)

```kotlin
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val reactiveFlow: ReactiveFlow,
) : AndroidViewModel(application = application) {
  ...
}
```

2. **Extend** your Event data class from `ColdEventFlow` or `HotEventFlow` according to your applications(I will explain their differences).

```kotlin
data class MessageHotEvent(val id: Int = 0, var message: String) : HotEventFlow()
```

``` kotlin
data class MessageColdEvent(val id: Int = 0, var message: String) : ColdEventFlow()
```

3. **Publish** your Event from the publisher class using the `ReactiveFlow` object.
```kotlin
// publish a hot event
reactiveFlow.publishHotEvent(MessageHotEvent(message = messageEditTextInput.value))
```
```kotlin
// publish a cold event
reactiveFlow.publishColdEvent(MessageColdEvent(message = messageEditTextInput.value))
```
4. **Subscribe** to your Event in the subscriber class using the `ReactiveFlow` object.
```kotlin
private var messageHotEventJob: Job? = null

...

// subscribe to a hot event
messageHotEventJob = reactiveFlow.onHotEvent(MessageHotEvent::class.java)
            .subscribe { event ->
                // do your hot subscription actions
            }
```

```kotlin
private var messageColdEventJob: Job? = null

...

// subscribe to a cold event
messageColdEventJob = reactiveFlow.onColdEvent(MessageColdEvent::class.java)
            .subscribe { event ->
               // do your cold subscription actions
            }
```

## Cold or Hot?<br/>
You can send two types of **Events** with different functionalities.<br/>
* **ColdEvent**: These kinds of events are received only when the event is subscribed to before it is published, otherwise, no events would be received.<br/>
* **HotEvent**: In despite of ColdEvents, HotEvents are received regardless of being subscribed before publishing or after it. In the case of being published before subscribing, the Event will be received as soon as it gets subscribed to.

> **Note**
**You can NOT use Hot and Cold events interchangeably!** <br/> Cold events should be published and subscribed to, using the methods taking `ColdEventFlow` subtypes, as well as Hot events which should be published and subscribed to using the methods taking `HotEventFlow` subtypes.

## How to cancel a subscription?
Whenever you subscribe to an Event, the `subscribe` method returns a `Job?` object. This is the handle to withdraw an Event(Cold or Hot) subscription.

```kotlin

override fun onCleared() {
        super.onCleared()
        // cancel events independently
        messageColdEventJob?.cancelEvent()
    }

```
Also, there is a situation in which you are faced with multiple number of Events in the same class and you are going to cancel them all at once. In this case, you only need to instantiate a `CompositeEventJob` and add returning `Job` from each event subscribe method to the composite object using `+` operator function, and at the end call `cancelAll()` over it. 

```kotlin
private val compositeEventJob = CompositeEventJob()

...

compositeEventJob + reactiveFlow.onHotEvent(MessageHotEvent::class.java)
            .subscribe { event ->
                // do your hot subscription actions
            }
compositeEventJob + reactiveFlow.onColdEvent(MessageColdEvent::class.java)
            .subscribe { event ->
               // do your cold subscription actions
            }

...

override fun onCleared() {
        super.onCleared()
        // cancel events independently
        compositeEventJob?.cancelAll()
    }

```
## A few things to Customize.
So far so good, but what if you what to change the publisher or subscriber threads?</br>The good news is there are multiple chain methods to add before `subscribe` to handle different functionalities. The following section demonstrates a full example of all chain methods supported by `ReactiveFlow` that can be used to customize the implementation as you need:
```kotlin
messageColdEventJob = reactiveFlow.onColdEvent(MessageColdEvent::class.java)
            .publishOn(Dispatchers.IO) // specify the publisher thread
            .subscribeOn(Dispatchers.Main) // specify the subscriber thread
            .onException { e -> e.printStacktrace() } // handle exception 
            .subscribeOnce(true) // receive a specific event only once.
            .withDelay(millis = 1000) // subscribe with given delay
            .subscribe { event ->
                onSubscribe(event)
            }.also { compositeEventJob + it }

```
