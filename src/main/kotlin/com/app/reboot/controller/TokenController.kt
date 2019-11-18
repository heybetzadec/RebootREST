package com.app.reboot.controller

import com.app.reboot.response.JwtUser
import com.app.reboot.security.JwtGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(private val jwtGenerator: JwtGenerator) {

    @PostMapping
    fun generate(@RequestBody jwtUser: JwtUser): String {

        return jwtGenerator.generate(jwtUser)

    }
}
