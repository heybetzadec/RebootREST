package com.app.reboot.repository

import com.app.reboot.entity.Slider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SliderRepository : JpaRepository<Slider, Long>