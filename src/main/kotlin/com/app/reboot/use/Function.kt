package com.app.reboot.use

import com.app.reboot.config.Final
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.awt.Dimension
import java.util.*

class Function {
    companion object {
        fun sliderDimension(type: Int): Dimension {
            var width = 0
            var height = 0
            when (type) {
                1 -> {
                    width = Final.sliderImageWidth
                    height = Final.sliderImageHeigh
                }
                2, 3 -> {
                    width = Final.mainTopBottomWidth
                    height = Final.mainTopBottomHeigh
                }
                4 -> {
                    width = Final.mainRightWidth
                    height = Final.mainRightHeigh
                }
            }

            return Dimension(width, height)
        }

        fun encoderCrypt(code: String): String {
            return BCryptPasswordEncoder().encode(code)
        }

        fun encoder(code: Any): String {
            val e = Encode()
            return e.md5(e.getHash(code)) ?: "nonconvertable"
        }

        fun aotb(encoded:String): String {
            return String(Base64.getDecoder().decode(encoded))
        }

        fun btoa(s:String): String {
            return String(Base64.getEncoder().encode(s.toByteArray()));
        }
    }
}