package com.jmj.mypatients.infrastructure.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MyPatientsUserDetailsService(private val users: Map<String, MyPatientUser>) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails = users[username!!]
            ?: throw UsernameNotFoundException(username)

}