package com.app.reboot.use

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class Encode {
    fun getHash(o: Any): String? {
        try {
            val mdAlgorithm = MessageDigest.getInstance("MD5")
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(o)
            mdAlgorithm.update(baos.toByteArray())

            val digest = mdAlgorithm.digest()
            val hexString = StringBuffer()

            for (i in digest.indices) {
                var x = Integer.toHexString(0xFF and digest[i].toInt())
                if (x.length < 2) x = "0$x"
                hexString.append(x)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: IOException) {
            return null
        }

    }

    fun md5(input: String?): String? {

        var md5: String? = null

        if (null == input) return null

        try {

            //Create MessageDigest object for MD5
            val digest = MessageDigest.getInstance("MD5")

            //Update input string in message digest
            digest.update(input.toByteArray(), 0, input.length)

            //Converts message digest value in base 16 (hex)
            md5 = BigInteger(1, digest.digest()).toString(16)

        } catch (e: NoSuchAlgorithmException) {

            e.printStackTrace()
        }

        return md5
    }
}