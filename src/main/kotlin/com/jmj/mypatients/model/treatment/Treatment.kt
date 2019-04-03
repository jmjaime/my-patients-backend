package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.treatment.account.Account
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.professional.office.Office
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.treatment.session.Session
import java.time.LocalDate

class Treatment(val id: Long, val professionalId: Long, val patientId: Long, val account: Account, val defaultOffice: Office, val derivation: Derivation, private val sessions: MutableList<Session> = mutableListOf()) {

    fun addSession(date: LocalDate, office: Office = defaultOffice, fee: Money, paid: Boolean = false): Session {
        val sessionNumber = sessions.size + 1
        return Session(sessionNumber, date, office, fee, paid).also { sessions.add(it) }
    }

}