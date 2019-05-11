package com.jmj.mypatients.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and().httpBasic()
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        val user = mutableMapOf(Pair("admin", MyPatientUser("admin", "{noop}admin",
                        listOf(SimpleGrantedAuthority(Role.ADMIN.name)))))
        return MyPatientsUserDetailsService(user)
    }
}