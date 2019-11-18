package com.app.reboot.config

import com.app.reboot.use.Encode
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.awt.Dimension
import java.util.*
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder


class Final {
    companion object {
//        val TINYBLOB_LENGTH: Long = 255
//        val BLOB_LENGTH: Long = 65535
//        val MEDIUMBLOB_LENGTH: Long = 16777215

        val urlApp = "http://localhost:8080/"
        val urlREST = "http://localhost:8081/"

        val jwtSecretKey = "rebootapp"

        val thubnailWidth = 450
        val thubnailHeigh = 250

        val imageWidth = 778
        val imageHeigh = 437

        val sliderImageWidth = 778
        val sliderImageHeigh = 437

        val mainTopBottomWidth = 382
        val mainTopBottomHeigh = 210

        val mainRightWidth = 382
        val mainRightHeigh = 436

    }


}