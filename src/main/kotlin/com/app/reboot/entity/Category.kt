package com.app.reboot.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.Where
import java.io.Serializable
import java.util.*
import javax.persistence.*



@Entity
@Table(name = "category_tbl")
class Category: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false)
    var id: Long? = null

    @Column(length=255)
    var name: String = ""

    @Column(length=255)
    var title: String = ""

    @Column(length=255)
    var link: String = ""

    @Column(length = 255)
    var description: String = ""

    @Column(length = 255)
    var keyword: String = ""

    var visible: Boolean = true

    @ManyToMany(fetch = FetchType.LAZY)
    var contents: Set<Content>? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var parentCategory: Category? = null

    @Column(name="create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var createDate: Date = Date()

    @Column(name="update_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    var updateDate: Date = Date()

    @Transient
    var parentId: Long? = null

    @Transient
    var parentName: String? = null

    @Transient
    var parentLink: String? = null

    constructor()

    constructor(name: String, visible:Boolean) {
        this.name = name
        this.visible = visible
    }

    constructor(id: Long?, name: String, title: String, link: String, description: String, keyword: String, visible: Boolean, createDate: Date, updateDate: Date) {
        this.id = id
        this.name = name
        this.title = title
        this.link = link
        this.description = description
        this.keyword = keyword
        this.visible = visible
        this.createDate = createDate
        this.updateDate = updateDate
    }

    constructor(name: String, title: String, link: String, visible:Boolean, description: String,keyword: String = "") {
        this.name = name
        this.title = title
        this.link = link
        this.visible = visible
        this.description = description
        this.keyword = keyword
    }

    constructor(id: Long?, name: String, link: String, parentCategory: Category?) {
        this.id = id
        this.name = name
        this.link = link
        this.parentCategory = parentCategory
    }

    constructor(id: Long?, name: String, link: String) {
        this.id = id
        this.name = name
        this.link = link
    }


    override fun toString(): String {
        return "Category(id=$id, name='$name', title='$title', link='$link', description='$description', keyword='$keyword', visible=$visible, createDate=$createDate, updateDate=$updateDate)"
    }

}