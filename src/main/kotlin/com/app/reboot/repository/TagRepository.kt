package com.app.reboot.repository

import com.app.reboot.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<Tag, Long>{

//    fun saveAll(tags:MutableList<Tag>?): Int
    fun existsByLink(link:String): Boolean
    fun findByLink(link:String): Tag?

}