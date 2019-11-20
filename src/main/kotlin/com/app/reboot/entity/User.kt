package com.app.reboot.entity

import org.hibernate.annotations.Type
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_tbl")
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

    @Column(name = "is_man", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isMan = null

    @Column(length=60)
    var mail: String = ""

    @Column(length=60)
    var username: String = ""

    @Column(length=255)
    var password: String = ""

    @Column(length=40)
    var pin: String? = null

    @Column(name = "is_active", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isActive: Boolean? = null

    @Column(name = "token_expired", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var tokenExpired: Boolean? = null

    @Column(length=255)
    var note: String? = null

    @ManyToOne
    @JoinTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    var userRole: Role? = null

    @Column(name="last_login", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var lastLoginDate: Date? = null

    @Column(name="create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var createDate: Date? = null

    @Column(name="update_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var updateDate: Date? = null

    constructor()

    constructor(name: String, surname: String, username: String, mail: String, password: String, isActive: Boolean) {
        this.name = name
        this.surname = surname
        this.username = username
        this.mail = mail
        this.password = password
        this.isActive = isActive
    }

    constructor(mail: String) {
        this.mail = mail
    }

    constructor(mail: String, username: String, password: String) {
        this.mail = mail
        this.username = username
        this.password = password
    }


    override fun toString(): String {
        return "User(id=$id, name='$name', surname='$surname', age=$age, logo='$logo', mail='$mail', password='$password', pin='$pin', isActive=$isActive, note='$note', lastLoginDate=$lastLoginDate, createDate=$createDate, updateDate=$updateDate)"
    }

}