package com.jmj.mypatients.model.treatment

import com.jmj.mypatients.model.errors.ObjectNotFoundException
import com.jmj.mypatients.model.money.Money
import com.jmj.mypatients.model.patient.Patient
import com.jmj.mypatients.model.professional.derivation.Derivation
import com.jmj.mypatients.model.treatment.session.Session
import java.time.Instant

data class Treatment(val id: String,
                     val professionalId: String,
                     val patient: Patient,
                     val defaultOfficeId: String,
                     val derivation: Derivation,
                     private val sessions: MutableList<Session> = mutableListOf()) {

    init {
        require(id.isNotBlank()) { "Id can not be blank" }
        require(professionalId.isNotBlank()) { "Professional Id can not be blank" }
        require(professionalId.isNotBlank()) { "Default Office Id can not be blank" }
    }

    fun addSession(date: Instant, officeId: String = defaultOfficeId, fee: Money, paid: Boolean = false): Session {
        val sessionNumber = sessions.size + 1
        return Session(sessionNumber, date, officeId, fee, paid).also {
            sessions.add(it)
        }
    }

    fun sessions() = listOf(sessions)

    fun getSession(sessionNumber: Int) = sessions.firstOrNull { it.number == sessionNumber }
            ?: throw ObjectNotFoundException("Session $sessionNumber not found")

}