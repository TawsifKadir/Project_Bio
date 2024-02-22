package com.xplo.code.core.ext

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */


fun String.removeWhitespaces(): String {
    return this.replace("[\\s-]*".toRegex(), "")
}

@Throws(ParseException::class)
fun String.toDate(format: String): Date? {
    val parser = SimpleDateFormat(format, Locale.getDefault())
    return parser.parse(this)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}


fun CharSequence.isEmailAddress(): Boolean {
    //taken from Android SDK file -> Patterns.java for use outside of Android SDK
    val pattern = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+")

    val regex = pattern.toRegex()

    return regex.matches(this)
}

/*
* https://stackoverflow.com/a/50963795
* */
fun String?.toFlagEmoji(): String {
    if (this == null) {
        return "null"
    }

    // 1. It first checks if the string consists of only 2 characters: ISO 3166-1 alpha-2 two-letter country codes (https://en.wikipedia.org/wiki/Regional_Indicator_Symbol).
    if (this.length != 2) {
        return this
    }

    val countryCodeCaps =
        this.uppercase(Locale.getDefault()) // upper case is important because we are calculating offset
    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    // 2. It then checks if both characters are alphabet
    if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
        return this
    }

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

