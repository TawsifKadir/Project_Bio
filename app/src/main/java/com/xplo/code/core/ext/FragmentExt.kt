package com.xplo.code.core.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

fun FragmentManager.isFragmentVisible(tag: String): Boolean {
    return findFragmentByTag(tag)?.isVisible == true
}

//fun FragmentActivity.replaceFragment(
//    @IdRes containerId: Int,
//    fragment: Fragment,
//    addToBackStack: Boolean = true,
//    tag: String = fragment::class.java.simpleName,
//    @AnimationType animateType: Int = SLIDE_TO_LEFT
//) {
//    supportFragmentManager.transact({
//        if (addToBackStack) {
//            addToBackStack(tag)
//        }
//        replace(containerId, fragment, tag)
//    }, animateType = animateType)
//}
//
//fun FragmentActivity.addFragment(
//    @IdRes containerId: Int,
//    fragment: Fragment,
//    addToBackStack: Boolean = true,
//    tag: String = fragment::class.java.simpleName,
//    @AnimationType animateType: Int = SLIDE_TO_LEFT
//) {
//    supportFragmentManager.transact({
//        if (addToBackStack) {
//            addToBackStack(tag)
//        }
//        add(containerId, fragment, tag)
//    }, animateType = animateType)
//}
