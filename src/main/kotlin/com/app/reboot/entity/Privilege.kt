package com.app.reboot.entity

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "privilege_tbl")
class Privilege(var name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var entity:String? = null

    @Column(name = "read", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var read:Boolean? = null

    @Column(name = "add", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var add:Boolean? = null

    @Column(name = "edit", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var edit:Boolean? = null

    @Column(name = "remove", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var remove:Boolean? = null

    @ManyToMany(mappedBy = "privileges")
    private var roles: Collection<Role>? = null


}