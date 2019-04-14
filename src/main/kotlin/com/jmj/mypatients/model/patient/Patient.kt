package com.jmj.mypatients.model.patient

data class Patient(val id: String, val name: String){

    init {
        require( id.isNotBlank()) {"Id can not be blank"}
        require( name.isNotBlank()) {"Name can not be blank"}
    }
}
