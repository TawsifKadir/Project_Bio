package com.xplo.code.data.event

enum class ButtonAction {
    BACK, NEXT, SUBMIT
}

data class EventButtonAction(
    var parent: String? = null,
    var buttonAction: ButtonAction? = null
)
