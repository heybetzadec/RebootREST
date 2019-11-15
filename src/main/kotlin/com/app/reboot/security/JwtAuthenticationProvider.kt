package com.app.reboot.security

import com.app.reboot.request.JwtAuthenticationToken
import com.app.reboot.request.JwtUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    private val validator: JwtValidator? = null

    @Throws(AuthenticationException::class)
    override fun additionalAuthenticationChecks(userDetails: UserDetails, usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken) {

    }

    @Throws(AuthenticationException::class)
    override fun retrieveUser(username: String, usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken): UserDetails {

        val jwtAuthenticationToken = usernamePasswordAuthenticationToken as JwtAuthenticationToken
        val token = jwtAuthenticationToken.token

        val jwtUser = token?.let { validator!!.validate(it) } ?: throw RuntimeException("JWT Token is incorrect")

        val grantedAuthorities:MutableList<GrantedAuthority> = AuthorityUtils
                .commaSeparatedStringToAuthorityList(jwtUser.role)
        return JwtUserDetails(jwtUser.username, jwtUser.id,
                token,
                grantedAuthorities)
    }

    override fun supports(aClass: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(aClass)
    }
}
