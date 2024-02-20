package com.xplo.code.ui.testing_lab

import android.util.Log
import com.xplo.code.ui.dashboard.enums.GenderEnm

fun main () {
    println("hello")

    val gender = GenderEnm.valueOf("MALE")
    println(gender)
    val gender2 = GenderEnm.find("Male")
    println(gender2)
}