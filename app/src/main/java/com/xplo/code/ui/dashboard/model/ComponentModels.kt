package com.xplo.code.ui.dashboard.model

data class CheckboxItem(
    var id: Int = 0,        // id pos idx are same, starts from 0
    var title: String? = null,
    var isChecked: Boolean = false
)