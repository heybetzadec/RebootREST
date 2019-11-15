package com.app.reboot.repository

import com.app.reboot.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name:String): MutableList<Role>?
}