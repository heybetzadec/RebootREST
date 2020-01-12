package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table
class Role(): Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L

    @Column
    var name: String = ""

    @OneToMany(mappedBy = "userRole")
    @JsonIgnore
    val users: Collection<User>? = null

    @ManyToMany
    @JoinTable(name = "role_privileges", joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")])
//    @JoinTable
    var privileges: Collection<Privilege>? = null


    constructor(name: String) : this() {
        this.name = name
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name', users=$users)"
    }


}