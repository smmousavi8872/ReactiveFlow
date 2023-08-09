# ReactiveFlow
[![Maven Central](https://img.shields.io/maven-central/v/io.github.smmousavi8872.reactiveflow/reactive-flow.svg?color=brightgreen)](https://search.maven.org/artifact/io.github.smmousavi8872.reactiveflow/reactive-flow)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
<br/>This is a reactive (publish/subscribe) event-based library to handle sending objects efficiently through the whole app based on `SharedFlow`. You can wrap any type of object into an `EventFlow` type and send them anywhere in your app(other classes, other packages, or even other modules) through a `ReactiveFlow` without needing any
class instantiation.<br/>
All events are sent in the background thread by default though it can be customized to use any other threads for publishing or subscribing to an Event.

# How to Install?
Simply add the following line to the `dependencies` section of your `build.gradle` file:

```
  implementation 'io.github.smmousavi8872.reactiveflow:reactive-flow:1.0.1.6'
```

# How to use?
Take these three easy steps to get your `ReactiveFlow` working:
1. **Inject** the `ReactiveFlow` object through `Hilt` or `Dagger` inside both classes in which you are going to publish and subscribe to your event.
2. **Extend** your Event data class from `ColdEventFlow` or `HotEventFlow` according to your applications(I will explain their differences).
3. **Publish** your Event from the publisher class using the `ReactiveFlow` object.
4. **Subscribe** to your Event in the subscriber class using the `ReactiveFlow` object.

### Cold or Hot?<br/>
You can send two types of **Events** with different functionalities.<br/>
**1. ColdEvent**: These kinds of events are received only when the event is subscribed to before it is published, otherwise, no events would be received.<br/>
**1. HotEvent**: In despite of ColdEvents, HotEvents are received regardless of being subscribed before publishing or after it. In the case of being published before subscribing, the Event will be received as soon as it gets subscribed to.
