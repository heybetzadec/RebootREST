package com.app.reboot.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table
class Tag() : Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false)
    var id: Long? = null

    @Column(length=255)
    var name: String? = null

    @Column(length=255, unique = true)
    var link: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var contents: Set<Content>? = null

    constructor(name: String, link: String) : this() {
        this.name = name
        this.link = link
    }

    constructor(id: Long?, name: String, link: String) : this() {
        this.id = id
        this.name = name
        this.link = link
    }

    constructor(link: String) : this() {
        this.link = link
    }

}