package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "role_tbl")
class Role(var name: String): Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L
    @OneToMany(mappedBy = "userRole")
    @JsonIgnore
    val users: Collection<User>? = null

    @ManyToMany
    @JoinTable(name = "role_privileges", joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")])
    var privileges: Collection<Privilege>? = null

}