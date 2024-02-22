package com.xplo.code.utils


/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object EtoB {
    private val bNum = charArrayOf(
        '০', '১', '২', '৩', '৪', '৫', '৬',
        '৭', '৮', '৯'
    )
    private val pun = charArrayOf('.', ',', '!')

    fun getBangla(s: String): String {
        val arr = s.toCharArray()
        val sb = StringBuilder()
        for (x in arr) {
            if (isPunctuation(x)) {
                sb.append(x)
            } else if (Character.isDigit(x)) {
                sb.append(numEToB(x))
            }
        }
        return sb.toString()
    }

    private fun isPunctuation(c: Char): Boolean {
        if (pun.contains(c)) return true
        return false
    }

    private fun numEToB(c: Char): Char {
        val conC: Char = when (c) {
            '0' -> bNum[0]
            '1' -> bNum[1]
            '2' -> bNum[2]
            '3' -> bNum[3]
            '4' -> bNum[4]
            '5' -> bNum[5]
            '6' -> bNum[6]
            '7' -> bNum[7]
            '8' -> bNum[8]
            '9' -> bNum[9]
            else -> c
        }
        return conC
    }
}