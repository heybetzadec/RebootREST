package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "privilege_tbl")
class Privilege : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var entity:String? = null

    @Column(name = "readable", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var readable:Boolean? = null

    @Column(name = "addable", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var addable:Boolean? = null

    @Column(name = "editable", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var editable:Boolean? = null

    @Column(name = "removable", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var removable:Boolean? = null

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private var roles: Collection<Role>? = null

    constructor()

    constructor(entity: String?, readable: Boolean?, addable: Boolean?, editable: Boolean?, removable: Boolean?) {
        this.entity = entity
        this.readable = readable
        this.addable = addable
        this.editable = editable
        this.removable = removable
    }


}