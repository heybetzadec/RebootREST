package com.app.reboot.controller

import com.app.reboot.config.Final
import com.app.reboot.entity.User
import com.app.reboot.repository.UserRepository
import com.app.reboot.response.*
import com.app.reboot.security.JwtGenerator
import com.app.reboot.use.Function
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import org.springframework.web.bind.annotation.RequestHeader


@RestController
class UserController (@Autowired private val userRepository : UserRepository, private val jwtGenerator: JwtGenerator){

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

    @RequestMapping(value = ["/user/login"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun saveCategory(@RequestHeader("Authorization") authorizationHeader :String): Response {
        val response = Response()
        val btoa = authorizationHeader.replace("Basic ","")
        val str = Function.aotb(btoa)
        val data = str.split(":")
        val user = User(data[0], data[0], Function.encoder(data[1]))
        val matcher1 = ExampleMatcher.matching().withMatcher("mail", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("password", ExampleMatcher.GenericPropertyMatchers.startsWith()).withIgnorePaths("username").withIgnoreCase()
        val matcher2 = ExampleMatcher.matching().withMatcher("username", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("password", ExampleMatcher.GenericPropertyMatchers.startsWith()).withIgnorePaths("mail").withIgnoreCase()
        val example1 = Example.of<User>(user, matcher1)
        val example2 = Example.of<User>(user, matcher2)
        val thisUser:User
        when {
            userRepository.exists(example1) -> thisUser = userRepository.findOne(example1).get()
            userRepository.exists(example2) -> thisUser = userRepository.findOne(example2).get()
            else -> {
                val error = Problem(404,"İstifadəçi yoxdur!","Not found user!")
                response.problem = error
                response.status = HttpStatus.NOT_FOUND
                return response
            }
        }
        thisUser.password = ""
        thisUser.pin = ""
        val jwtUser = JwtUser(data[0], thisUser.id ?: 0, thisUser.userRole?.name ?: "ADMIN")
        val body = Body()
        body.token = jwtGenerator.generate(jwtUser)
        body.loginUser = LoginUser(thisUser.id!!, thisUser.name ?: "", thisUser.surname ?: "", thisUser.username, thisUser.logo ?: "", thisUser.userRole!!)
        response.status = HttpStatus.OK
        response.body = body
        return response
    }

//    @RequestMapping(path = ["/user/login"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun getMyRequestHeaders( @RequestHeader(value = "Authorization") authorizationHeader: String){
//        val str = authorizationHeader.replace("Basic ","");
//        println("data=${Final.aotb(str)}")
//    }

}