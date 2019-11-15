package com.app.reboot.security

import com.app.reboot.request.JwtAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException


class JwtAuthenticationTokenFilter : AbstractAuthenticationProcessingFilter("/rest/**") {

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): Authentication {

        val header = httpServletRequest.getHeader("Authorisation")


        if (header == null || !header.startsWith("Token ")) {
            throw RuntimeException("JWT Token is missing")
        }

        val authenticationToken = header.substring(6)

        val token = JwtAuthenticationToken(authenticationToken)
        return authenticationManager.authenticate(token)
    }


    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain?, authResult: Authentication) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain!!.doFilter(request, response)
    }
}
