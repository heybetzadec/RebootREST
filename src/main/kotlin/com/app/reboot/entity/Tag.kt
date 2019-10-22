package com.app.reboot.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Tag: Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @Column(length=255)
    var name: String = ""

    @Column(length=255)
    var link: String = ""
}