package com.app.reboot.repository

import com.app.reboot.entity.Rank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RankRepository : JpaRepository<Rank, Long>