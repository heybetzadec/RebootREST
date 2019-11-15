package com.app.reboot.repository

import com.app.reboot.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(mail:String): MutableList<User>?

}