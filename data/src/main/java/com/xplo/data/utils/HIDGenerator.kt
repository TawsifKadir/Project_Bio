package com.xplo.data.utils

import java.util.Random


object HIDGenerator {

    private val _base62chars: CharArray =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray()

    private val _random: Random = Random()

    private fun getBase62(length: Int): String {
        val sb = StringBuilder(length)
        for (i in 0 until length) sb.append(_base62chars[_random.nextInt(62)])
        return sb.toString()
    }

    private fun getBase36(length: Int): String {
        val sb = StringBuilder(length)
        for (i in 0 until length) sb.append(_base62chars[_random.nextInt(36)])
        return sb.toString()
    }

    fun getHID(): String {
        return getBase36(6)
    }


}