package com.app.reboot.help

import com.app.reboot.entity.*

class Body {
    var user: User? = null
    var category: Category? = null
    var rank:Rank? = null
    var content:Content? = null
    var slider:Slider? = null

    var users: MutableList<User>? = null
    var categories: MutableList<Category>? = null
    var contents: MutableList<Content>? = null
    var sliders: MutableList<Slider>? = null

}