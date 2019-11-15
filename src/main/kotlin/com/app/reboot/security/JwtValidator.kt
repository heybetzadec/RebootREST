package com.app.reboot.security

import com.app.reboot.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
@Component
class JwtValidator {


    private val secret = "youtube"

    fun validate(token: String): User? {

        var jwtUser: User? = null
        try {
            val body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
            jwtUser = User()

            jwtUser.name = body.getSubject()
            jwtUser.id = java.lang.Long.parseLong(body["userId"] as String)
            jwtUser.role = (body.get("role") as String)
        } catch (e: Exception) {
            println(e)
        }

        return jwtUser
    }
}
