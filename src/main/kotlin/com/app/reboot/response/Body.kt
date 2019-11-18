package com.app.reboot.response

import com.app.reboot.entity.Category
import com.app.reboot.entity.Content
import com.app.reboot.entity.Slider
import com.app.reboot.entity.User

class Body {
    var user: User? = null
    var category: Category? = null
    var content:Content? = null
    var slider:Slider? = null
    var token:String = ""

    var loginUser:LoginUser? = null
    var users: MutableList<User>? = null
    var categories: MutableList<Category>? = null
    var contents: MutableList<Content>? = null
    var sliders: MutableList<Slider>? = null
    var categoryNodes: MutableList<CategoryNode>? = null

}