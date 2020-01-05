package com.app.reboot.response

import com.app.reboot.entity.Category
import com.app.reboot.entity.Tag
import java.util.*

class ContentData(
        var id: Long?,
        var title: String,
        var link: String,
        var imageName: String,
        var categories: MutableList<Category>?,
        var tags: MutableList<Tag>?,
        var addUserId: Long?,
        var editUserId: Long?,
        var visible: Boolean,
        var viewCount: Int,
        var createDate: Date?,
        var updateDate: Date?)