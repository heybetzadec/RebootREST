package com.app.reboot.help

class CategoryNode {

    var key:String = "0"
    var data:CategoryData? = null
    var parentCategoryId:Long? = null
    var children: MutableList<CategoryNode>? = null



    constructor(id: Long?, name: String, title: String, link: String, visible: Boolean, parentCategoryId:Long?) {
        this.key = id.toString()
        this.data = CategoryData(id, name, title, link, visible)
        this.parentCategoryId = parentCategoryId
    }

    override fun toString(): String {
        var s = "CategoryNode(key='$key', data=${data.toString()}, parentCategoryId=$parentCategoryId)"
        return ""
    }


}