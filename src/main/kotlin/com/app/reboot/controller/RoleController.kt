package com.app.reboot.controller

import com.app.reboot.entity.Role
import com.app.reboot.repository.RoleRepository
import com.app.reboot.response.Body
import com.app.reboot.response.Problem
import com.app.reboot.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RestController
class RoleController  (@Autowired private val roleRepository : RoleRepository){

    @PersistenceContext
    lateinit var em: EntityManager


    @RequestMapping(value = ["/roles/get/select/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getCategoriesForSelect(@PathVariable id :Long): Response {
        val response = Response()
        val roles: MutableList<Role> = em.createQuery(
                "select c " +
                        "from Role c WHERE c.id != :id  order by id desc", Role::class.java).setParameter("id", id).resultList
        if (roles.size > 0) {
            val body = Body()
            response.body = body
            response.body?.roles = roles
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return response
    }

}