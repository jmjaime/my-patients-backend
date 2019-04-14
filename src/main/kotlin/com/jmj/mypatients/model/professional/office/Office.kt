package com.jmj.mypatients.model.professional.office

data class Office(val id: String, val description: String, val professionalId: String){
    init {
        require( id.isNotBlank()) {"Id can not be blank"}
        require( description.isNotBlank()) {"Name can not be blank"}
        require( professionalId.isNotBlank()) {"Professional Id can not be blank"}
    }
}
