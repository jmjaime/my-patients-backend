package com.jmj.mypatients.infrastructure.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class MyPatientUser(username: String, password: String,
                    authorities: Collection<GrantedAuthority>, val professionalId: String? = null) : User(username, password, authorities) {

    init {
        check(validProfessional(authorities)) { "Professional Role should be linked to a Professional, authorities: $authorities" }
    }

    private fun validProfessional(authorities: Collection<GrantedAuthority>) =
            !authorities.contains(SimpleGrantedAuthority(Role.PROFESSIONAL.name)) || professionalId != null
}

enum class Role {
    ADMIN,
    PROFESSIONAL
}