package com.app.reboot.controller

import com.app.reboot.config.Final
import com.app.reboot.entity.Role
import com.app.reboot.entity.Tag
import com.app.reboot.entity.User
import com.app.reboot.exception.StorageException
import com.app.reboot.repository.RoleRepository
import com.app.reboot.repository.UserRepository
import com.app.reboot.response.*
import com.app.reboot.security.JwtGenerator
import com.app.reboot.security.JwtValidator
import com.app.reboot.service.StorageService
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
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*


@RestController
class UserController (
        @Autowired private val userRepository : UserRepository,
        @Autowired private val roleRepository : RoleRepository,
        private val jwtGenerator: JwtGenerator
){


    @PersistenceContext
    lateinit var em: EntityManager
    @Autowired
    private lateinit var storageService: StorageService

    @RequestMapping(value = ["/user/get/model"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getModel(): User {
        return User(null, "", "", null, "", true, "", "", "", true, false, "", null, null, Date(), Date())
    }

    @RequestMapping(value = ["/user/login"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun saveRole(@RequestHeader("Authorization") authorizationHeader :String): Response {
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
        thisUser.lastLoginDate = Date()
        userRepository.save(thisUser)
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


    @RequestMapping(value = ["users/get/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getUsers(@PathVariable offset :Int, @PathVariable limit: Int): Response {
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(User::class.java)
        val root = cq.from(User::class.java)
        cq.select(root)

        val orderList = listOf(cb.desc(root.get<Long>("id")))
        cq.orderBy(orderList)
        val query = em.createQuery<User>(cq)

        val users: MutableList<User>
        if (limit == 0) {
            users = query.resultList
        } else {
            users = query.setFirstResult(offset).setMaxResults(limit).resultList
        }
        if (users.size > 0) {
            val body = Body()
            body.users = users
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found users!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["/users"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getUsers(): Response{
        val response = Response()
        val usersData = em.createQuery(
                "select NEW com.app.reboot.response.UserData(id, name, surname,  age, logo, isMan, mail, username, active, note, userRole.name, lastLoginDate, createDate, updateDate) " +
                        "from User u order by id desc", UserData::class.java)
                .resultList
        if (usersData.size > 0) {
            val body = Body()
            body.usersData = usersData
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["/user/get/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getUser(@PathVariable id :Long): Response {
        val response = Response()
        val result = userRepository.findById(id)
        if (result.isEmpty){
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        } else {
            val user = result.get()
            user.password = ""
            val body = Body()
            body.user = user
            response.body = body
        }
        response.status = HttpStatus.OK
        return  response
    }

    @RequestMapping(value = ["secure/user/upload/img"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
    fun uploadLogo(@RequestParam file: MultipartFile, @RequestParam oldImage: String) {
        try {
            storageService.uploadLogoSetSize(file, Final.logoWidth, Final.logoHeigh)
            if (oldImage.isNotEmpty()){
                storageService.removeFile(Final.logoImagePath, oldImage)
            }
        } catch (e: StorageException){
            println("Problem upload file: ${e.message}}")
        }
    }

    @RequestMapping(value = ["secure/user/save"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun addUser(@RequestHeader("Authorisation") authorizationHeader :String, @RequestBody user : User): Response {
        val response = Response()
        val jwtValidator = JwtValidator();
        val token = authorizationHeader.removePrefix("Token ")
        val jwtUser = jwtValidator.validate(token) ?: JwtUser("", 0, "")
        val role:Role = roleRepository.findByName(jwtUser.role).get()
        val privlage = role.privileges?.find { it.entity == "user" }
        if (privlage != null){
            if (privlage.addable ?: false){
                val dbUsers = userRepository.findAll()
                if (user.id == null){
                    val matchingUser = dbUsers.findLast {
                        it.mail.isNotEmpty() && it.username.isNotEmpty() && (it.username == user.username || it.mail == user.mail)
                    }
                    if (matchingUser != null){
                        var error = Problem()
                        if (user.mail == matchingUser.mail){
                            error = Problem(500, "${user.mail} epoçt ünvanı artıq bazada var!","already_have_user_by_mail")
                        } else if (user.username == matchingUser.username){
                            error = Problem(500, "${user.username} istifadəçisi artıq bazada var!","already_have_user_by_username")
                        } 
                        response.problem = error
                        response.status = HttpStatus.NOT_ACCEPTABLE

                        return response
                    }
                }

                try {
                    val body = Body()
                    if (user.id == null){
                        user.addUserId = jwtUser.id
                    } else {
                        user.editUserId = jwtUser.id
                    }
                    userRepository.save(user)
                    body.user = user
                    response.body = body
                    response.status = HttpStatus.OK
                } catch (e:Exception){
                    val error = Problem(500,"Saxlanma zamanı problem yarandı!","db_save_problem")
                    response.problem = error
                    response.status = HttpStatus.NOT_ACCEPTABLE
                }
            } else {
                val error = Problem(500,"İcazəniz yoxdur!","No permission!")
                response.problem = error
                response.status = HttpStatus.METHOD_NOT_ALLOWED
            }
        }

        return response
    }


    @RequestMapping(value = ["/secure/user/remove/id/{id}"], method = [RequestMethod.GET])
    fun removeUser(@PathVariable id:Long): Response {
        val response = Response()
        if (userRepository.existsById(id)){
            response.status = HttpStatus.OK
//            val user = userRepository.findById(id).get()
//            val userData: UserData = UserData(user.id, user.name, user.surname, user.age, user.logo, user.isMan, user.mail, user.username, user.active, user.note, user.userRole?.name ?: "", user.lastLoginDate, user.createDate, user.updateDate)
            userRepository.deleteById(id)
//            val body = Body()
//            body.userData = userData
//            response.body = body
            response.status = HttpStatus.OK
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "Silinməsi istənilən istifadəçi yoxdur","Not found category")
            return response
        }
        return response
    }

}