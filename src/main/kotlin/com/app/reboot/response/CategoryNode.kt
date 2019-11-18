package com.app.reboot.response

class CategoryNode(id: Long?, name: String, title: String, link: String, visible: Boolean, var parentCategoryId: Long?) {

    var key:String = "0"
    var data:CategoryData? = null
    var children: MutableList<CategoryNode>? = null


    init {
        this.key = id.toString()
        this.data = CategoryData(id, name, title, link, visible)
    }

    override fun toString(): String {
        return "CategoryNode(key='$key', data=${data.toString()}, parentCategoryId=$parentCategoryId)"
    }


}