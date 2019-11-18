package com.app.reboot.response

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtAuthenticationToken(var token: String?) : UsernamePasswordAuthenticationToken(null, null) {

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }
}
