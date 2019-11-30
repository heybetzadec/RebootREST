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
        val categoryPrivlageAdmin = Privilege("category", true, true, true, true)
        val contentPrivlageAdmin = Privilege("content", true, true, true, true)
        val privlagePrivlageAdmin = Privilege("privlage", true, true, true, true)
        val rolePrivlageAdmin = Privilege("role", true, true, true, true)
        val sliderPrivlageAdmin = Privilege("slider", true, true, true, true)
        val tagPrivlageAdmin = Privilege("tag", true, true, true, true)
        val userPrivlageAdmin = Privilege("user", true, true, true, true)


        val categoryPrivlageEditor = Privilege("category", true, true, true, false)
        val contentPrivlageEditor = Privilege("content", true, true, true, true)
        val privlagePrivlageEditor = Privilege("privlage", false, false, false, false)
        val rolePrivlageEditor = Privilege("role", false, false, false, false)
        val sliderPrivlageEditor = Privilege("slider", true, true, true, true)
        val tagPrivlageEditor = Privilege("tag", true, true, true, true)
        val userPrivlageEditor = Privilege("user", false, false, false, false)

//        val readPrivilege = createPrivilegeIfNotFound("FULL_PRIVILEGE")
//        val writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE")
        val adminPrivileges = mutableListOf(categoryPrivlageAdmin, contentPrivlageAdmin, privlagePrivlageAdmin, rolePrivlageAdmin, sliderPrivlageAdmin, tagPrivlageAdmin, userPrivlageAdmin)
        val editorPrivlages = mutableListOf(categoryPrivlageEditor, contentPrivlageEditor, privlagePrivlageEditor, rolePrivlageEditor, sliderPrivlageEditor, tagPrivlageEditor, userPrivlageEditor)
        if (privilegeRepository!!.count() == 0L) {
            createPrivilegesIfNotFound(adminPrivileges)
            createPrivilegesIfNotFound(editorPrivlages)
        }
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges)
        createRoleIfNotFound("ROLE_EDITOR", editorPrivlages)


        val adminRole = roleRepository!!.findByName("ROLE_ADMIN").get()
        val users = mutableListOf<User>(
                User("Cavad", "Heybətzadə", "hecaheybet", "heybetzadec@gmail.com", Function.encoder("blackberryz10"), true),
                User("Toğrul", "İbrahimov", "togrul", "togrul@gmail.com", Function.encoder("12345678"), true))
        users.forEach {
            try {
                userRepository!!.findByMail(it.mail).get()
            } catch (e: NoSuchElementException) {
                it.isActive
                it.userRole = adminRole
                userRepository!!.save<User>(it)
            }
        }

        createCategoriesIfNotFound()
        alreadySetup = true
    }

    @Transactional
    fun createPrivilegesIfNotFound(privlages: MutableList<Privilege>) {
        privlages.forEach {
            privilegeRepository!!.save(it)
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
            try {
                categoryRepository!!.findByLink(it.link).get()
            } catch (e: NoSuchElementException) {
                categoryRepository!!.save(it)
            }
        }
    }
}