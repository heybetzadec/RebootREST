package com.app.reboot.component

import com.app.reboot.entity.Privilege
import com.app.reboot.entity.Role
import com.app.reboot.entity.User
import com.app.reboot.repository.PrivilegeRepository
import com.app.reboot.repository.RoleRepository
import com.app.reboot.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
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


    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        if (alreadySetup)
            return
        val readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE")
        val writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE")

        val adminPrivileges = mutableListOf(
                readPrivilege, writePrivilege)
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges)
        createRoleIfNotFound("ROLE_USER", mutableListOf(readPrivilege))

        val encoded = BCryptPasswordEncoder().encode("blackberryz10")

        val adminRole = roleRepository!!.findByName("ROLE_ADMIN")
        val user = User("Cavad", "Heybətzadə", "hecaheybet", "heybetzadec@gmail.com", encoded, true)
        user.isActive
        user.roles = adminRole
        userRepository!!.save<User>(user)

        alreadySetup = true
    }

    @Transactional
    fun createPrivilegeIfNotFound(name: String): Privilege {

        var privilege = privilegeRepository!!.findByName(name)?.first()
        if (privilege == null) {
            privilege = Privilege(name)
            privilegeRepository.save<Privilege>(privilege)
        }
        return privilege
    }

    @Transactional
    fun createRoleIfNotFound(
            name: String, privileges: Collection<Privilege>): Role {

        var role = roleRepository!!.findByName(name)?.first()
        if (role == null) {
            role = Role(name)
            role.privileges = privileges
            roleRepository.save<Role>(role)
        }
        return role
    }
}