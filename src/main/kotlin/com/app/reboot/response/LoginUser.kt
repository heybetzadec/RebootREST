package com.app.reboot.response

class LoginUser {
    var id: Long = 0
    var name: String = ""
    var surname: String = ""
    var username: String = ""
    var logo: String? = ""
    var role: String = ""

    constructor(id: Long, name: String, surname: String, username: String, logo: String?, role: String) {
        this.id = id
        this.name = name
        this.surname = surname
        this.username = username
        this.logo = logo
        this.role = role
    }
}