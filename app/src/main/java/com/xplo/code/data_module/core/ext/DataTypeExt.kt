package com.xplo.code.data_module.core.ext

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

fun Double?.isNull(): Boolean {
    return this != null
}

fun Int?.plusOne(): Int {
    if (this == null) return 0
    return this.plus(1)
}

fun Boolean?.toBool(): Boolean {
    if (this == null) return false
    return this
}
