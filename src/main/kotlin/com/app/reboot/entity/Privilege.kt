package com.app.reboot.entity

import javax.persistence.*

@Entity
@Table(name = "privilege_tbl")
class Privilege(var name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L

    @ManyToMany(mappedBy = "privileges")
    private var roles: Collection<Role>? = null

}