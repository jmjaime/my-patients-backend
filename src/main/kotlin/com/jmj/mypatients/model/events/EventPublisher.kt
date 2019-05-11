package com.jmj.mypatients.model.events

import com.google.common.eventbus.EventBus

interface EventPublisher {

    fun publish(myPatientEvent: MyPatientEvent)
}

interface EventSubscriber {

    fun subscribe(subscriber: Any)
}


class MyPatientsEventBus(private val eventBus: EventBus) : EventPublisher, EventSubscriber {

    override fun subscribe(subscriber: Any) {
        eventBus.register(subscriber)
    }

    override fun publish(myPatientEvent: MyPatientEvent) {
        eventBus.post(myPatientEvent)
    }

}