package com.app.reboot.response

class Problem {
    var code:Int = 0
    var message:String = ""
    var error:String = ""

    constructor()

    constructor(code: Int, message: String, error: String) {
        this.code = code
        this.message = message
        this.error = error
    }
}

//200 - SUCESS
//404 - RESOURCE NOT FOUND
//400 - BAD REQUEST
//201 - CREATED
//401 - UNAUTHORIZED
//405 - Method Not Allowed
//415 - UNSUPPORTED TYPE - Representation not supported for the resource
//500 - SERVER ERROR