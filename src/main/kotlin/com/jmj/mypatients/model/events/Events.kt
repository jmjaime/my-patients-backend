package com.jmj.mypatients.model.events

interface MyPatientEvent

data class SessionCreated(val professional: String, val treatment: String, val sessionNumber: Int) : MyPatientEvent {

}