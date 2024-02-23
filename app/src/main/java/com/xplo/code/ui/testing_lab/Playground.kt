package com.xplo.code.ui.testing_lab

import android.util.Log
import com.kit.integrationmanager.model.GenderEnum

fun main () {
    println("hello")

    val gender = GenderEnum.valueOf("MALE")
    println(gender)
    val gender2 = GenderEnum.find("Male")
    println(gender2)
}