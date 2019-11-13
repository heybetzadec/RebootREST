package com.app.reboot.help

import org.springframework.http.HttpStatus
import java.util.*

class Response {

    var status:HttpStatus? = null
    var problem:Problem? = null
    var body:Body? = null
    var date:Date? =  null

    constructor(){
        this.date = Date()
    }

    constructor(status:HttpStatus?) {
        this.status = status
        this.date = Date()
    }

    constructor(status:HttpStatus?, problem: Problem?, body: Body?) {
        this.status = status
        this.problem = problem
        this.body = body
        this.date = Date()
    }

    constructor(status: HttpStatus?, body: Body?) {
        this.status = status
        this.body = body
        this.date = Date()
    }

}