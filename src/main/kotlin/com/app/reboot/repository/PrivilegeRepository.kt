package com.app.reboot.repository

import com.app.reboot.entity.Privilege
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long>