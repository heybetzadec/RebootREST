package com.app.reboot.entity

import javax.persistence.*

@Entity
@Table(name = "role_tbl")
class Role(var name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    var id: Long = 0L
    @ManyToMany(mappedBy = "roles")
    val users: Collection<User>? = null

    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")])
    var privileges: Collection<Privilege>? = null

}