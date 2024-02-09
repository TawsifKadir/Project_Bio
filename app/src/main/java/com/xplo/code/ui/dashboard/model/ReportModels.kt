package com.xplo.code.ui.dashboard.model

import com.xplo.code.core.ext.toBool


data class ReportRow(
    var title: String? = null,
    var value: String? = null,
    var title2: String? = null,
    var value2: String? = null
)

fun HouseholdForm?.getReportRowsAltSummary(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Alternate Added:", form.alternates.size.toString(), null, null))
    items.add(ReportRow("Click to see in details", "", null, null))

    return items
}

fun HhForm1?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("State:", form.stateName, "County: ", form.countryName))
    items.add(ReportRow("Payam:", form.payamName, "Boma: ", form.bomaName))

    return items
}

fun HhForm2?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Name:", form.getFullName(), null, null))
    items.add(ReportRow("Age:", form.age, "Gender: ", form.gender))
    items.add(ReportRow("Id:", form.idNumber, "Phone: ", form.phoneNumber))

    return items
}

fun HhForm3?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Total:", form.householdSize.toString(), null, null))
    items.add(
        ReportRow(
            "Normal Male:",
            form.getNormalMale().toString(),
            "Normal Female:",
            form.getNormalFemale().toString(),
        )
    )
    items.add(
        ReportRow(
            "Disable Male:",
            form.getDisableMale().toString(),
            "Disable Female:",
            form.getDisableFemale().toString(),
        )
    )
    items.add(
        ReportRow(
            "Ill Male:",
            form.getIllMale().toString(),
            "Ill Female:",
            form.getIllFemale().toString(),
        )
    )
    items.add(ReportRow("Total Input:", form.getTotalTable().toString(), null, null))

    return items
}

fun HhForm5?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this
    val finger = this.finger

    if (finger == null) return items

    items.add(
        ReportRow(
            "Left Thumb:",
            finger.isAFingerStatus(finger.fingerLT),
            "Right Thumb: ",
            finger.isAFingerStatus(finger.fingerRT)
        )
    )

    items.add(
        ReportRow(
            "Left Index:",
            finger.isAFingerStatus(finger.fingerLI),
            "Right Index: ",
            finger.isAFingerStatus(finger.fingerRI)
        )
    )


    return items
}

fun Finger?.isAFingerStatus(finger: String?): String {
    if (this == null) return false.toString()
    return this.isAFingerCaptured(finger).toString()
}

fun Finger?.isAFingerCaptured(finger: String?): Boolean {
    if (this == null) return false
    if (finger?.isNotEmpty().toBool()) return true
    return false
}


fun HhForm6?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    val nominees = form.nominees

    var idx = 1
    for (item in nominees) {
        items.add(ReportRow(item.getNomineeHeader(idx)))
        items.addAll(item.getNomineeItemRows())
        idx++
    }

    return items
}

fun Nominee?.getNomineeItemRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Name:", form.getFullName(), null, null))
    items.add(ReportRow("Age:", form.age.toString(), "Gender: ", form.gender))

    return items
}