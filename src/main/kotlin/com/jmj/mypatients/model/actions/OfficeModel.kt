package com.jmj.mypatients.model.actions

import com.jmj.mypatients.model.professional.office.Office

data class OfficeModel(val id: String, val description: String)

fun Office.toModel() = OfficeModel(this.id, this.description)