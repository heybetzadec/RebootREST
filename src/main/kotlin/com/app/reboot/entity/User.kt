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
    var id: Long = 0

    @Column(length=40)
    var name: String = ""

    @Column(length=40)
    var surname: String = ""

    var age: Int? = null

    @Column(length=255)
    var logo: String = ""

    @Column(length=40)
    var mail: String = ""

    @Column(length=60)
    var password: String = ""

    @Column(length=40)
    var pin: String = ""

    @Column(name = "is_active", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isActive: Boolean = true

    @Column(name = "is_admin", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isAdmin: Boolean = true

    @Column(length=255)
    var note: String = ""

    @Column(name="last_login", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var lastLoginDate: Date = Date()

    @Column(name="create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var createDate: Date = Date()

    @Column(name="update_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var updateDate: Date = Date()

    @OneToOne
    @JoinColumn(name = "rank_id", referencedColumnName = "id")
    private var rank: Rank? = null

    constructor()

    constructor(name: String, surname: String, mail:String, password: String, logo:String) {
        this.name = name
        this.surname = surname
        this.mail = mail
        this.password = password
        this.logo = logo
    }

    override fun toString(): String {
        return "User(id=$id, rank=${rank?.name}, name='$name', surname='$surname', age=$age, logo='$logo', mail='$mail', password='$password', pin='$pin', isActive=$isActive, note='$note', lastLoginDate=$lastLoginDate, createDate=$createDate, updateDate=$updateDate)"
    }

}