package com.app.reboot.response

import java.util.*

class UserData {
    var id: Long? = null

    var name: String? = null

    var surname: String? = null

    var age: Int? = null

    var logo: String? = null

    var isMan:Boolean? = null

    var mail: String = ""

    var username: String = ""

    var active: Boolean? = null

    var note: String? = null

    var roleName: String? = null

    var lastLoginDate: Date? = null

    var createDate: Date? = null

    var updateDate: Date? = null

    constructor(id: Long?, name: String?, surname: String?, age: Int?, logo: String?, isMan: Boolean?, mail: String, username: String, active: Boolean?, note: String?, roleName: String?, lastLoginDate: Date?, createDate: Date?, updateDate: Date?) {
        this.id = id
        this.name = name
        this.surname = surname
        this.age = age
        this.logo = logo
        this.isMan = isMan
        this.mail = mail
        this.username = username
        this.active = active
        this.note = note
        this.roleName = roleName
        this.lastLoginDate = lastLoginDate
        this.createDate = createDate
        this.updateDate = updateDate
    }
}