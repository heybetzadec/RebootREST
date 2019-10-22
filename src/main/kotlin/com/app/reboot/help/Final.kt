package com.app.reboot.help

import java.awt.Dimension

class Final {
    companion object {
        val TINYBLOB_LENGTH: Long = 255
        val BLOB_LENGTH: Long = 65535
        val MEDIUMBLOB_LENGTH: Long = 16777215

        val urlApp = "http://localhost:8080/"
        val urlREST = "http://localhost:8081/"

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


            fun sliderDimension(type:Int): Dimension {
                    var width = 0
                    var height = 0
                    when (type) {
                            1 -> {
                                    width = Final.sliderImageWidth
                                    height = Final.sliderImageHeigh
                            }
                            2 -> {
                                    width = Final.mainTopBottomWidth
                                    height = Final.mainTopBottomHeigh
                            }
                            3 -> {
                                    width = Final.mainRightWidth
                                    height = Final.mainRightHeigh
                            }
                            4 -> {
                                    width = Final.thubnailWidth
                                    height = Final.thubnailHeigh
                            }
                    }

                    return Dimension(width , height)
            }

    }


}