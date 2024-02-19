package com.xplo.code.ui.dashboard.enums


enum class Gender(val value: String) {

    SELECT("Select Gender"),
    MALE("Male"),
    FEMALE("Female");

    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}





