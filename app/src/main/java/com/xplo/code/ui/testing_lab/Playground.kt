package com.xplo.code.ui.testing_lab

import com.kit.integrationmanager.model.GenderEnum
import com.xplo.code.data_module.utils.HIDGenerator

fun main() {
    println("hello")

    val gender = GenderEnum.valueOf("MALE")
    println(gender)
    val gender2 = GenderEnum.find("Male")
    println(gender2)

    val hid = HIDGenerator.getHID()
    println(hid)
}