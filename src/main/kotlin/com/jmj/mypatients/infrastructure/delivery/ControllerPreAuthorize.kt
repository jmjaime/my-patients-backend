package com.jmj.mypatients.infrastructure.delivery

const val PROFESSIONAL_PRE_AUTHORIZE ="(hasAuthority('PROFESSIONAL') && authentication.principal.professionalId == #professionalId)"