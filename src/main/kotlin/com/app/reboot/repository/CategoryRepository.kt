package com.app.reboot.repository

import com.app.reboot.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByLink(link:String): Optional<Category>

}