package com.app.reboot.response

import com.app.reboot.entity.*
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class Body {
    var contentImageMediaPath: String? = null
    var contentThubnailImageMediaPath: String? = null
    var user: User? = null
    var category: Category? = null
    var content:Content? = null
    var slider:Slider? = null
    var role:Role? = null
    var token:String = ""

    var userData: UserData? = null

    var loginUser:LoginUser? = null
    var users: MutableList<User>? = null
    var roles: MutableList<Role>? = null
    var categories: MutableList<Category>? = null
    var contents: MutableList<Content>? = null
    var sliders: MutableList<Slider>? = null
    var usersData:MutableList<UserData>? = null
    var categoryNodes: MutableList<CategoryNode>? = null
    var contentData: MutableList<ContentData>? = null

}