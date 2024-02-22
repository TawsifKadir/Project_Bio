package com.xplo.code.utils

object EncryptionUtils {

    fun encrypt(text: String, keyValue: Int): String {

        val ch = text.toCharArray()
        for (i in ch.indices) {
            ch[i] = (ch[i] + keyValue)
        }
        val sb = StringBuffer(String(ch))
        return sb.reverse().toString()
    }


    fun decrypt(s: String, keyValue: Int): String {

        val sb = StringBuffer(s).reverse()
        val ch = sb.toString().toCharArray()
        for (i in ch.indices) {
            ch[i] = (ch[i] - keyValue)
        }
        return String(ch)
    }

}