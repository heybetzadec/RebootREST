package com.app.reboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RebootApplication

fun main(args: Array<String>) {
	runApplication<RebootApplication>(*args)
}
