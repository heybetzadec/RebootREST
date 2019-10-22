package com.app.reboot.entity

import org.hibernate.annotations.Type
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "role_tbl")
class Role: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @Column(name = "show_dt", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var show: Boolean = false

    @Column(name = "add_dt", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var add: Boolean = false

    @Column(name = "edit_dt", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var edit: Boolean = false

    @Column(name = "delete_dt", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var delete: Boolean = false

}