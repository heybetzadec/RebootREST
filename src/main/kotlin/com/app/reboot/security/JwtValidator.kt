package com.app.reboot.security

import com.app.reboot.config.Final
import com.app.reboot.response.JwtUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
@Component
class JwtValidator {


//    private val secret = "youtube"

    fun validate(token: String): JwtUser? {

        var jwtUser: JwtUser? = null
        try {
            val body:Claims = Jwts.parser()
                    .setSigningKey(Final.jwtSecretKey)
                    .parseClaimsJws(token)
                    .body
            jwtUser = JwtUser()

            jwtUser.username = body.getSubject()
            jwtUser.id = java.lang.Long.parseLong(body["userId"] as String)
            jwtUser.role = (body.get("role") as String)
        } catch (e: Exception) {
            println(e)
        }

        return jwtUser
    }
}
