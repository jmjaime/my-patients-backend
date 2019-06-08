package com.jmj.mypatients.domain.professional

data class Professional(val id: String, val name: String){
    init {
        require( id.isNotBlank()) {"Id can not be blank"}
        require( name.isNotBlank()) {"Name can not be blank"}
    }
}