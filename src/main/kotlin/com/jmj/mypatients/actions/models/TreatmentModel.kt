package com.jmj.mypatients.actions.models

import com.jmj.mypatients.domain.professional.office.Office
import com.jmj.mypatients.domain.treatment.Treatment

data class TreatmentSmallModel(val id: String, val patient: String)

data class TreatmentModel(val id: String, val office: OfficeModel, val derivation: DerivationModel, val patient: String)


fun Treatment.toSmallModel() = TreatmentSmallModel(this.id, this.patient.name)

fun Treatment.toModel(defaultOffice: Office) = TreatmentModel(this.id, defaultOffice.toModel(), this.derivation.toModel(), this.patient.name)