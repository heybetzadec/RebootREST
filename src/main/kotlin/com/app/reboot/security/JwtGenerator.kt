package com.app.reboot.security

import com.app.reboot.request.Final
import com.app.reboot.request.JwtUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JwtGenerator {


    fun generate(jwtUser: JwtUser): String {


        val claims:Claims = Jwts.claims()
                .setSubject(jwtUser.username)
        claims.put("userId", jwtUser.id.toString())
        claims.put("role", jwtUser.role)


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, Final.jwtSecretKey)
                .compact()
    }
}