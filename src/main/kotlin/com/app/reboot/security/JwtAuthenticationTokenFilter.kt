package com.app.reboot.security

import com.app.reboot.response.JwtAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationTokenFilter : AbstractAuthenticationProcessingFilter("/secure/**") {

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): Authentication {

        val header = httpServletRequest.getHeader("Authorisation")

        println("header = $header")

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

//    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
////        super.doFilter(req, res, chain)
//        val response = res as HttpServletResponse
//        val request = req as HttpServletRequest?
//
//        println("===Authorisation = ${response.getHeader("Authorisation")}")
//        response.setHeader("Access-Control-Allow-Origin", "*")
//        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS")
//        response.setHeader("Access-Control-Allow-Headers", "*")
//        response.setHeader("Access-Control-Allow-Credentials", "true")
//        response.setHeader("Access-Control-Max-Age", "180")
//        chain?.doFilter(req, res)
//    }
}
