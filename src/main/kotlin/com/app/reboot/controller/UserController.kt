package com.app.reboot.controller

import com.app.reboot.entity.User
import com.app.reboot.help.Body
import com.app.reboot.help.Problem
import com.app.reboot.help.Response
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

//    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/loadusers"))
//    public fun loadDefault(): Response {
//        val user:User = User("Cavad","Heybətzadə", "heybetzadec@gmail.com", "12345", "logo.jpg")
////        userRepository.save(user)
//        val body = Body()
//        body.user = user
//        val response = Response(HttpStatus.OK, Problem(0,"sehv var","NullPointerException"), body)
//        return response
//    }
//
//    @RequestMapping(value = ["/getusers/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
//    @Throws(Exception::class)
//    fun getUsers(@PathVariable offset :Int, @PathVariable limit: Int): Response {
//        val response = Response()
//        val cb = em.criteriaBuilder
//        val cq = cb.createQuery(User::class.java)
//        val root = cq.from(User::class.java)
//        cq.select(root)
////        cq.where(
////                cb.equal(root.get<Long>("mail"), "heybetzadec@gmail.com")
////        )
//        val query = em.createQuery<User>(cq)
//        var users = mutableListOf<User>()
//        if (limit == 0) {
//            users = query.resultList
//        } else {
//            users = query.setFirstResult(offset).setMaxResults(limit).resultList
//        }
//        if (users.size > 0) {
//            val body = Body()
//            body.users = users
//            response.body = body
//            response.status = HttpStatus.OK
//        } else {
//            val error = Problem(404,"Məlumat yoxdur!","Not found users!")
//            response.problem = error
//            response.status = HttpStatus.NOT_ACCEPTABLE
//        }
//        return  response
//    }
//
//    @RequestMapping(value = ["/adduser"], method = [RequestMethod.POST])
//    @Throws(Exception::class)
//    fun addUser(@RequestBody user :User): Response {
//        val response = Response()
//        val cb = em.criteriaBuilder
//        val cq = cb.createQuery(User::class.java)
//        val root = cq.from(User::class.java)
//        cq.select(root)
//        cq.where(
//                cb.equal(root.get<Long>("mail"), user.mail)
//        )
//        val query = em.createQuery<User>(cq)
//
//        if(query.resultList.size == 0){
//            userRepository.save(user)
//            val body = Body()
//            body.user = user
//            response.body = body
//            response.status = HttpStatus.OK
//        } else {
//            val error = Problem(503,"Epoçt ünvanı bənzərsiz olmalıdır!","${user.mail} alredy exist!")
//            response.problem = error
//            response.status = HttpStatus.NOT_ACCEPTABLE
//        }
//        return response
//    }
//
//    @RequestMapping(value = ["/edituser"], method = [RequestMethod.POST])
//    @Throws(Exception::class)
//    fun editUser(@RequestBody user :User): Response {
//        val response = Response()
//
//        if(userRepository.existsById(user.id)){
//            userRepository.save(user)
//            val body = Body()
//            body.user = user
//            response.body = body
//            response.status = HttpStatus.OK
//        } else {
//            val error = Problem(503,"Redaktəsi istənilən istifadəçisi yoxdur.","${user.name} not found!")
//            response.problem = error
//            response.status = HttpStatus.NOT_ACCEPTABLE
//        }
//        return response
//    }
//
//    @RequestMapping(value = ["/removeuser"], method = [RequestMethod.DELETE])
//    fun removeUser(@RequestBody user :User): Response {
//        val response = Response()
//        if (userRepository.existsById(user.id)){
//            userRepository.delete(user)
//            response.status = HttpStatus.OK
//            response.body = Body()
//            response.body?.user = user
//        } else {
//            response.status = HttpStatus.NOT_FOUND
//            response.problem = Problem(404, "${user.name} ${user.surname} istifadəçisi yoxdur.","Not found user")
//        }
//        return response
//    }
//
//    @RequestMapping(value = ["/removeuser/id/{id}"], method = [RequestMethod.DELETE])
//    fun removeUser(@PathVariable id:Long): Response {
//        val response = Response()
//        if (userRepository.existsById(id)){
//            response.status = HttpStatus.OK
//            response.body = Body()
//            response.body?.user = userRepository.findById(id).get()
//            userRepository.deleteById(id)
//
//        } else {
//            response.status = HttpStatus.NOT_FOUND
//            response.problem = Problem(404, "Silinməsi istənilən istifadəçisi yoxdur","Not found user")
//        }
//        return response
//    }


}