package com.xplo.code.core.ext

import android.widget.Button

// button brand

fun Button?.enableBtn() {
    if (this == null) return
    this.isEnabled = true
    this.alpha = 1.toFloat()

}

fun Button?.disableBtn() {
    if (this == null) return
    this.isEnabled = false
    this.alpha = 0.5.toFloat()
}

//fun Button?.enableBtnBrand() {
//    if (this == null) return
//    this.isEnabled = true
//    this.backgroundTintList = ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorBrand))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnBrand))
//}
//
//fun Button?.disableBtnBrand() {
//    if (this == null) return
//    this.isEnabled = false
//    this.backgroundTintList =
//        ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorBrandDisabled))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnBrandDisabled))
//}
//
//fun TextView?.enableTxtBtnBrand() {
//    if (this == null) return
//    this.isEnabled = true
//    //this.backgroundTintList = ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorBrand))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorBrand))
//}
//
//fun TextView?.disableTxtBtnBrand() {
//    if (this == null) return
//    this.isEnabled = false
//    this.setTextColor(this.context.getAttrColor(R.attr.colorBrandDisabled))
//}
//
//// button brand light
//
//fun Button?.enableBtnBrandLight() {
//    if (this == null) return
//    this.isEnabled = true
//    this.backgroundTintList =
//        ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorBrandLight))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnBrand))
//}
//
//fun Button?.disableBtnBrandLight() {
//    if (this == null) return
//    this.isEnabled = false
//    this.backgroundTintList =
//        ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorBrandDisabled))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnBrandDisabled))
//}

//fun TextView?.enableTxtBtnBrandLight() {
//    if (this == null) return
//    this.isEnabled = true
//    this.setTextColor(this.context.getAttrColor(R.attr.colorBrandLight))
//}
//
//fun TextView?.disableTxtBtnBrandLight() {
//    if (this == null) return
//    this.isEnabled = false
//    this.setTextColor(this.context.getAttrColor(R.attr.colorBrandDisabled))
//}

//// button A
//
//fun Button?.enableBtnA() {
//    if (this == null) return
//    this.isEnabled = true
//    this.backgroundTintList = ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorButtonA))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnButtonA))
//}
//
//fun Button?.disableBtnA() {
//    if (this == null) return
//    this.isEnabled = false
//    this.backgroundTintList =
//        ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorButtonADisabled))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnButtonA))
//}
//
//// button B
//
//fun Button?.enableBtnB() {
//    if (this == null) return
//    this.isEnabled = true
//    this.backgroundTintList = ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorButtonB))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnButtonB))
//}
//
//fun Button?.disableBtnB() {
//    if (this == null) return
//    this.isEnabled = false
//    this.backgroundTintList =
//        ColorStateList.valueOf(this.context.getAttrColor(R.attr.colorButtonBDisabled))
//    this.setTextColor(this.context.getAttrColor(R.attr.colorOnButtonB))
//}