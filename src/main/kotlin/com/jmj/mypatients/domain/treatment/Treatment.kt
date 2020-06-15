package com.jmj.mypatients.domain.treatment

import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import com.jmj.mypatients.domain.money.Money
import com.jmj.mypatients.domain.patient.Patient
import com.jmj.mypatients.domain.professional.derivation.Derivation
import com.jmj.mypatients.domain.session.Session
import java.time.Instant

data class Treatment(val id: String,
                     val professionalId: String,
                     val patient: Patient,
                     val defaultOfficeId: String,
                     val derivation: Derivation,
                     private var sessions: List<Session> = listOf()) {

    init {
        require(id.isNotBlank()) { "Id can not be blank" }
        require(professionalId.isNotBlank()) { "Professional Id can not be blank" }
        require(professionalId.isNotBlank()) { "Default Office Id can not be blank" }
    }

    fun addSession(date: Instant, officeId: String = defaultOfficeId, fee: Money, paid: Boolean = false): Session {
        val sessionNumber = sessions.size + 1
        return Session(sessionNumber, date, officeId, fee, paid).also {
           sessions = sessions + it
        }
    }

    fun sessions() = sessions.toList()

    fun getSession(sessionNumber: Int) = sessions.firstOrNull { it.number == sessionNumber }
            ?: throw ObjectNotFoundException("Session $sessionNumber not found")

    fun getSessionsNotPaid() = sessions.filter { !it.paid }

    fun markAsPaid(sessionsToPay: List<Session>) {
        check(getSessionsNotPaid().containsAll(sessionsToPay)) {"Invalid sessions to mark as paid"}
        sessions = sessions.map { if (it in sessionsToPay) it.copy(paid=true) else it }
    }

}