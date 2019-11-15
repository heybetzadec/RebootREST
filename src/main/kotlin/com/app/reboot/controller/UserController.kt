package com.app.reboot.controller

import com.app.reboot.entity.User
import com.app.reboot.request.Body
import com.app.reboot.request.Response
import com.app.reboot.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@RestController
class UserController (@Autowired private val userRepository : UserRepository){

    @PersistenceContext
    lateinit var em: EntityManager

    @RequestMapping(method = [RequestMethod.GET], path = ["/users/load"])
    fun loadDefault(): Response {
        val users = mutableListOf<User>(
                User("Cavad", "Heybətzadə", "hecaheybet", "heybetzadec@gmail.com", "12345678", true),
                User("Toğrul", "İbrahimov", "togrul", "togrul@gmail.com", "12345678", true)
        )
        userRepository.saveAll(users)
        val body = Body()
        body.users = users
        return Response(HttpStatus.OK, body)
    }

//    @RequestMapping(value = ["/user/login"], method = [RequestMethod.POST])
//    @Throws(Exception::class)
//    fun saveCategory(@RequestBody body :String): String {
//        val response = Response()
//
//        return body
//    }

}