package com.app.reboot.service

import com.app.reboot.entity.Privilege
import com.app.reboot.entity.Role
import com.app.reboot.repository.RoleRepository
import com.app.reboot.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service("userDetailsService")
@Transactional
class MyUserDetailsService : UserDetailsService {

    @Autowired
    private val userRepository: UserRepository? = null

//    @Autowired
//    private val service: IUserService? = null

//    @Autowired
//    private val messages: MessageSource? = null

    @Autowired
    private val roleRepository: RoleRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {

        val findUsers = userRepository?.findByMail(email)
                ?: return org.springframework.security.core.userdetails.User(
                        " ", " ", true, true, true, true,
                        roleRepository?.findByName("ROLE_USER")?.let { getAuthorities(it.get()) })

        val user = findUsers.get()

        return org.springframework.security.core.userdetails.User(
                user.mail, user.password, user.active ?: true, true, true,
                true, user.userRole?.let { getAuthorities(it) })
    }

    private fun getAuthorities(role: Role): Collection<GrantedAuthority> {

        return getGrantedAuthorities(getPrivileges(role))
    }

    private fun getPrivileges(role:Role): List<String> {

        val privileges = ArrayList<String>()
        val collection = ArrayList<Privilege>()
        collection.addAll(role.privileges!!)
        for (item in collection) {
            item.entity?.let { privileges.add(it) }
        }
        return privileges
    }

    private fun getGrantedAuthorities(privileges: List<String>): List<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        for (privilege in privileges) {
            authorities.add(SimpleGrantedAuthority(privilege))
        }
        return authorities
    }
}