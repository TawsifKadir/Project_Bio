package com.xplo.code.ui.dashboard.model


fun HhForm3.getNormalMale(): Int {
    return mem0NormalMale + mem3NormalMale + mem6NormalMale + mem18NormalMale + mem36NormalMale + mem65NormalMale
}

fun HhForm3.getDisableMale(): Int {
    return mem0DisableMale + mem3DisableMale + mem6DisableMale + mem18DisableMale + mem36DisableMale + mem65DisableMale
}

fun HhForm3.getIllMale(): Int {
    return mem0IllMale + mem3IllMale + mem6IllMale + mem18IllMale + mem36IllMale + mem65IllMale
}

fun HhForm3.getNormalFemale(): Int {
    return mem0NormalFemale + mem3NormalFemale + mem6NormalFemale + mem18NormalFemale + mem36NormalFemale + mem65NormalFemale
}

fun HhForm3.getDisableFemale(): Int {
    return mem0DisableFemale + mem3DisableFemale + mem6DisableFemale + mem18DisableFemale + mem36DisableFemale + mem65DisableFemale
}

fun HhForm3.getIllFemale(): Int {
    return mem0IllFemale + mem3IllFemale + mem6IllFemale + mem18IllFemale + mem36IllFemale + mem65IllFemale
}

fun HhForm3.getNormalPerson(): Int {
    return this.getNormalMale() + this.getNormalFemale()
}

fun HhForm3.getDisablePerson(): Int {
    return this.getDisableMale() + this.getDisableFemale()
}

fun HhForm3.getIllPerson(): Int {
    return this.getIllMale() + this.getIllFemale()
}

fun HhForm3.getTotalTable(): Int {
    return this.getNormalPerson() + this.getDisablePerson() + this.getIllPerson()
}
