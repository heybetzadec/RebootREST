package com.app.reboot.repository

import com.app.reboot.entity.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository: JpaRepository<Content, Long>