package com.app.reboot.component

import com.app.reboot.config.Final
import com.app.reboot.entity.Category
import com.app.reboot.entity.Privilege
import com.app.reboot.entity.Role
import com.app.reboot.entity.User
import com.app.reboot.repository.CategoryRepository
import com.app.reboot.repository.PrivilegeRepository
import com.app.reboot.repository.RoleRepository
import com.app.reboot.repository.UserRepository
import com.app.reboot.use.Function
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class InitialDataLoader : ApplicationListener<ContextRefreshedEvent> {

    internal var alreadySetup = false

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val roleRepository: RoleRepository? = null

    @Autowired
    private val privilegeRepository: PrivilegeRepository? = null

    @Autowired
    private val categoryRepository : CategoryRepository? =null

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup)
            return
        val readPrivilege = createPrivilegeIfNotFound("FULL_PRIVILEGE")
        val writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE")
        val adminPrivileges = mutableListOf(
                readPrivilege, writePrivilege)
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges)
        createRoleIfNotFound("ROLE_USER", mutableListOf(readPrivilege))
        val encoded = Function.encoder("blackberryz10")
        val adminRole = roleRepository!!.findByName("ROLE_ADMIN").get()
        val user = User("Cavad", "Heybətzadə", "hecaheybet", "heybetzadec@gmail.com", encoded, true)
        try {
            userRepository!!.findByMail(user.mail).get()
        } catch (e: NoSuchElementException) {
            user.isActive
            user.roles = listOf(adminRole)
            userRepository!!.save<User>(user)
        }
        createCategoriesIfNotFound()
        alreadySetup = true
    }

    @Transactional
    fun createPrivilegeIfNotFound(name: String): Privilege {
        return try {
            privilegeRepository!!.findByName(name).get()
        } catch (e: NoSuchElementException) {
            val privilege = Privilege(name)
            privilegeRepository!!.save(privilege)
            privilege
        }
    }

    @Transactional
    fun createRoleIfNotFound(name: String, privileges: Collection<Privilege>): Role {
        return try {
            roleRepository!!.findByName(name).get()
        } catch (e: NoSuchElementException) {
            val role = Role(name)
            role.privileges = privileges
            roleRepository!!.save<Role>(role)
            role
        }
    }

    fun createCategoriesIfNotFound() {
        val categories = mutableListOf<Category>()
        categories.add(Category("Elm","Ən son Elm və Texnologiya Xəbərləri","elm",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Cihazlar","Kompüter və mobil cihaz xəbərləri","cihazlar",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Geyilən","Geyilən Texnologiya Xəbərləri və mobil cihaz xəbərləri","geyilen",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("İnternet","İnternet Xəbərləri","internet",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Kompaniya","Kampaniya Xəbərləri","kompaniya",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Kripto valyuta","Kripto valyuta","kripto",true,"&lt;p&gt;Uzunca bir s&amp;uuml;redir var olsa da Bitcoin&amp;#39;in pop&amp;uuml",""))
        categories.add(Category("Mobil","Mobil Telefon və Tablet Xəbərləri","tablet",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Mobil Tətbiq","Mobil Tətbiq","mobil_tetbiq",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Avtomobil","Avtomobil","avtomobil",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        val matcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
        categories.forEach {
            val example = Example.of<Category>(it, matcher)
            if (!categoryRepository!!.exists(example)){
                categoryRepository.save(it)
            }
        }
    }
}