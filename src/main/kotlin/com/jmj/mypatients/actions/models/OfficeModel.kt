package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.professional.office.Office

data class OfficeModel(val id: String, val name: String)

fun Office.toModel() = OfficeModel(this.id, this.name)