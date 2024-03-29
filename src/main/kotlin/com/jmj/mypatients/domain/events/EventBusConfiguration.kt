package com.jmj.mypatients.domain.events

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
class EventBusConfiguration {

    @Bean
    fun executor(): Executor = Executors.newCachedThreadPool()

    @Bean
    fun eventBus(executor: Executor): EventBus = AsyncEventBus(executor)

    @Bean
    fun myPatientsEventBus(eventBus: EventBus) = MyPatientsEventBus(eventBus)
}