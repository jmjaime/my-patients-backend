package com.jmj.mypatients

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration
class MyPatientsApplication

fun main(args: Array<String>) {
    runApplication<MyPatientsApplication>(*args)
}
