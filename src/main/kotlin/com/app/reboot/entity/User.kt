package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.Type
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table
class User:Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(length=40)
    var name: String? = null

    @Column(length=40)
    var surname: String? = null

    var age: Int? = null

    @Column(length=255)
    var logo: String? = null

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isMan:Boolean? = null

    @Column(length=60)
    var mail: String = ""

    @Column(length=60)
    var username: String = ""

    @Column(length=255)
    var password: String = ""

    @Column(length=40)
    var pin: String? = null

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var active: Boolean? = null

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var tokenExpired: Boolean? = null

    @Column(length=255)
    var note: String? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
//    @JoinTable(joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
//    @JoinTable
    var userRole: Role? = null

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var lastLoginDate: Date? = null

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var createDate: Date? = null

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var updateDate: Date? = null

    @Column
    var addUserId:Long? = null

    @Column
    var editUserId:Long? = null

    constructor()

    constructor(name: String, surname: String, username: String, mail: String, password: String, active: Boolean) {
        this.name = name
        this.surname = surname
        this.username = username
        this.mail = mail
        this.password = password
        this.active = active
        this.updateDate = Date()
        this.createDate = Date()
    }

    constructor(mail: String) {
        this.mail = mail
    }

    constructor(mail: String, username: String, password: String) {
        this.mail = mail
        this.username = username
        this.password = password
    }

    constructor(id: Long?, name: String?, surname: String?, age: Int?, logo: String?, isMan: Boolean?, mail: String, username: String, password: String, active: Boolean?, tokenExpired: Boolean?, note: String?, userRole: Role?, lastLoginDate: Date?, createDate: Date?, updateDate: Date?) {
        this.id = id
        this.name = name
        this.surname = surname
        this.age = age
        this.logo = logo
        this.isMan = isMan
        this.mail = mail
        this.username = username
        this.password = password
        this.active = active
        this.tokenExpired = tokenExpired
        this.note = note
        this.userRole = userRole
        this.lastLoginDate = lastLoginDate
        this.createDate = createDate
        this.updateDate = updateDate
    }


    override fun toString(): String {
        return "User(id=$id, name='$name', surname='$surname', age=$age, logo='$logo', mail='$mail', password='$password', pin='$pin', active=$active, note='$note', lastLoginDate=$lastLoginDate, createDate=$createDate, updateDate=$updateDate, role = ${userRole.toString()})"
    }

}