package com.app.reboot.repository

import com.app.reboot.entity.Slider
import org.springframework.data.jpa.repository.JpaRepository

interface SliderRepository : JpaRepository<Slider, Long>