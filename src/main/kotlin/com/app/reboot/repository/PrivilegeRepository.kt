package com.app.reboot.repository

import com.app.reboot.entity.Privilege
import org.springframework.data.jpa.repository.JpaRepository

interface PrivilegeRepository : JpaRepository<Privilege, Long> {
    fun findByName(name:String): MutableList<Privilege>?
}