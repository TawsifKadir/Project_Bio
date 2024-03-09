package com.xplo.code.ui.dashboard.model

//fun HhForm3.getNormalMale(): Int {
//    return mem0NormalMale + mem3NormalMale + mem6NormalMale + mem18NormalMale + mem36NormalMale + mem65NormalMale
//}
//
//fun HhForm3.getDisableMale(): Int {
//    return mem0DisableMale + mem3DisableMale + mem6DisableMale + mem18DisableMale + mem36DisableMale + mem65DisableMale
//}
//
//fun HhForm3.getIllMale(): Int {
//    return mem0IllMale + mem3IllMale + mem6IllMale + mem18IllMale + mem36IllMale + mem65IllMale
//}
//
//fun HhForm3.getNormalFemale(): Int {
//    return mem0NormalFemale + mem3NormalFemale + mem6NormalFemale + mem18NormalFemale + mem36NormalFemale + mem65NormalFemale
//}
//
//fun HhForm3.getDisableFemale(): Int {
//    return mem0DisableFemale + mem3DisableFemale + mem6DisableFemale + mem18DisableFemale + mem36DisableFemale + mem65DisableFemale
//}
//
//fun HhForm3.getIllFemale(): Int {
//    return mem0IllFemale + mem3IllFemale + mem6IllFemale + mem18IllFemale + mem36IllFemale + mem65IllFemale
//}
fun HhForm3.getNormalMale(): Int {
    return this.male0_2.normal + this.male3_5.normal + this.male6_17.normal + this.male18_35.normal + this.male36_64.normal + this.male65p.normal
}

fun HhForm3.getDisableMale(): Int {
    return this.male0_2.disable + this.male3_5.disable + this.male6_17.disable + this.male18_35.disable + this.male36_64.disable + this.male65p.disable
}

fun HhForm3.getIllMale(): Int {
    return this.male0_2.ill + this.male3_5.ill + this.male6_17.ill + this.male18_35.ill + this.male36_64.ill + this.male65p.ill
}

fun HhForm3.getNormalFemale(): Int {
    return this.female0_2.normal + this.female3_5.normal + this.female6_17.normal + this.female18_35.normal + this.female36_64.normal + this.female65p.normal
}

fun HhForm3.getDisableFemale(): Int {
    return this.female0_2.disable + this.female3_5.disable + this.female6_17.disable + this.female18_35.disable + this.female36_64.disable + this.female65p.disable
}

fun HhForm3.getIllFemale(): Int {
    return this.female0_2.ill + this.female3_5.ill + this.female6_17.ill + this.female18_35.ill + this.female36_64.ill + this.female65p.ill
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

fun HhMember.getTotal(): Int {
    return normal + ill + disable
}

fun HhForm3.getTotalMale(): Int{
    return this.getNormalMale() + this.getIllMale() + this.getDisableMale()
}


fun HhForm3.getTotalFemale(): Int{
    return this.getNormalFemale() + this.getIllFemale() + this.getDisableFemale()
}

