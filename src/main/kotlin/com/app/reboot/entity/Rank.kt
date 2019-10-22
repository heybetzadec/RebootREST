package com.app.reboot.entity
import java.io.Serializable
import javax.persistence.*
import javax.persistence.JoinColumn



@Entity
@Table(name = "rank_tbl")
class Rank: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length=40)
    var name: String = ""

    @OneToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    private val roleToUser: Role? = null

    @OneToOne
    @JoinColumn(name = "menu_role_id", referencedColumnName = "id")
    private val roleToMenu: Role? = null

    @OneToOne
    @JoinColumn(name = "topic_role_id", referencedColumnName = "id")
    private val roleToTopic: Role? = null

    @OneToOne
    @JoinColumn(name = "content_role_id", referencedColumnName = "id")
    private val roleToContent: Role? = null

    @OneToOne
    @JoinColumn(name = "setting_role_id", referencedColumnName = "id")
    private val roleToSetting: Role? = null

    @OneToOne
    @JoinColumn(name = "rank_role_id", referencedColumnName = "id")
    private val roleToRank: Role? = null


}