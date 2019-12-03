package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "content_tbl")
class Content :Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(length=255)
    var title: String = ""

    @Column(length=255)
    var link: String = ""

    @Column(name="image_name")
    var imageName: String = ""

    @Column(length=255)
    var embed: String = ""

    @Column(length = 255)
    var description: String = ""

    @Column(length = 255)
    var keyword: String = ""

    @Column(columnDefinition = "TEXT")
    var html: String = ""

    @ManyToMany(fetch = FetchType.LAZY)
//    @JsonIgnore
    var categories: MutableList<Category>? = null

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    @JsonIgnore
    var tags: MutableList<Tag>? = null

    @Column(name="add_user_id")
    var addUserId:Int = 0

    @Column(name="edit_user_id")
    var editUserId:Int = 0

    var visible: Boolean = true

    @Column(name="view_count")
    var viewCount: Int = 0

    @Column(name="create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var createDate: Date = Date()

    @Column(name="update_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var updateDate: Date = Date()


    constructor(id: Long?, title: String, link: String, imageName: String, embed: String, description: String, keyword: String, html: String, addUserId: Int, editUserId: Int, visible: Boolean, viewCount: Int, createDate: Date, updateDate: Date) {
        this.id = id
        this.title = title
        this.link = link
        this.imageName = imageName
        this.embed = embed
        this.description = description
        this.keyword = keyword
        this.html = html
        this.addUserId = addUserId
        this.editUserId = editUserId
        this.visible = visible
        this.viewCount = viewCount
        this.createDate = createDate
        this.updateDate = updateDate
    }

    constructor(id: Long?, title: String, imageName: String, visible: Boolean, viewCount: Int, createDate: Date) {
        this.id = id
        this.title = title
        this.imageName = imageName
        this.visible = visible
        this.viewCount = viewCount
        this.createDate = createDate
    }


}