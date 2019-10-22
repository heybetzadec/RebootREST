package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "slider_tbl")
class Slider: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var typeId: Int = 1

    @Column(length=255)
    var title: String = ""

    @Column(length = 255)
    var description: String = ""

    @Column(length=255)
    var link: String = ""

    @Column(name="image_name")
    var imageName: String = ""

    @OneToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
    var content: Content? = null

    @Column(name="add_user_id")
    var addUserId:Int = 0

    @Column(name="edit_user_id")
    var editUserId:Int = 0

    @Column(name="content_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var contentDate: Date = Date()

    @Column(name="create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var createDate: Date = Date()

    @Column(name="update_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var updateDate: Date = Date()

    constructor(id: Long?, typeId: Int, title: String, description: String, link: String, imageName: String, addUserId: Int, editUserId: Int, contentDate: Date, createDate: Date, updateDate: Date) {
        this.id = id
        this.typeId = typeId
        this.title = title
        this.description = description
        this.link = link
        this.imageName = imageName
        this.addUserId = addUserId
        this.editUserId = editUserId
        this.contentDate = contentDate
        this.createDate = createDate
        this.updateDate = updateDate
    }
}