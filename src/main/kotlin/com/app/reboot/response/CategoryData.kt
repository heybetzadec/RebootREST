package com.app.reboot.response


class CategoryData(var id: Long?, var name: String, var title: String, var link: String, var visible: Boolean) {
    override fun toString(): String {
        return "CategoryData(id=$id, name='$name', title='$title', link='$link', visible=$visible)"
    }
}