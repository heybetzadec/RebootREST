package com.app.reboot.response

class JwtUser {
    var username: String = ""
    var id: Long = 0
    var role: String = ""

    constructor(username: String, id: Long, role: String) {
        this.username = username
        this.id = id
        this.role = role
    }

    constructor()


}
