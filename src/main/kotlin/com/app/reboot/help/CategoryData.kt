package com.app.reboot.help


class CategoryData {
    var id: Long? = null
    var name: String = ""
    var title: String = ""
    var link: String = ""
    var visible: Boolean = true


    constructor(id: Long?, name: String, title: String, link: String, visible: Boolean) {
        this.id = id
        this.name = name
        this.title = title
        this.link = link
        this.visible = visible
    }

    override fun toString(): String {
        return "CategoryData(id=$id, name='$name', title='$title', link='$link', visible=$visible)"
    }


}