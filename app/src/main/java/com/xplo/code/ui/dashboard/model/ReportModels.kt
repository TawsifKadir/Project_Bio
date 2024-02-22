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
    //items.add(ReportRow("Click to see in details", "", null, null))

    return items
}

fun HhForm1?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("State:", form.state?.name, "County: ", form.county?.name))
    items.add(ReportRow("Payam:", form.payam?.name, "Boma: ", form.boma?.name))
    items.add(ReportRow("Lat:", form.lat.toString(), "Lon: ", form.lon.toString()))

    return items
}

fun HhForm2?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Name:", form.getFullName(), "Gender: ", form.gender))
    items.add(ReportRow("Age:", form.age.toString(),"Phone: ", form.phoneNumber))
    items.add(ReportRow("Id No:", form.idNumber, "Id Type: ", form.idNumberType))
    items.add(ReportRow("Phone Number", form.phoneNumber, "Marital Status", form.maritalStatus))
    items.add(ReportRow("Spouse Name", form.getSpouseFullName(), null, null))
    items.add(ReportRow("legal Status", form.legalStatus, null, null))
    items.add(ReportRow("Respondent Relation", form.respondentRlt, "Selection Reason", form.selectionReason))
    items.add(ReportRow("Selection Criteria", form.selectionCriteria, "Main Source Of Income", form.mainSourceOfIncome))
    items.add(ReportRow("Monthly Average Income", form.monthlyAverageIncome.toString(), "Currency", form.currency))

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
    items.add(ReportRow("Do You Read and Write :", form.isReadWrite, null, null))
    items.add(ReportRow("How Many others member can read and write :", form.readWriteNumber.toString(), null, null))

    return items
}

fun HhForm5?.getReportRows(): List<ReportRow> {
    if (this == null) {
        val items = arrayListOf<ReportRow>()
            items.add(ReportRow("Left Thumb:", "False", "Right Thumb: ", "False"))
            items.add(ReportRow("Left Index:","False", "Right Index: ", "False"))
            items.add(ReportRow("Left Middle:", "False", "Right Middle: ", "False"))
            items.add(ReportRow("Left Ring:","False", "Right Ring: ", "False"))
            items.add(ReportRow("Left Little:","False", "Right little: ", "False"))

        return items
    }

    val items = arrayListOf<ReportRow>()
//    //val form = this
//    val finger = this.fingerData
//
//    //if (finger == null) return items
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Thumb:",
//                finger.isAFingerStatus(finger.fingerLT?.fingerPrint),
//                "Right Thumb: ",
//                finger.isAFingerStatus(finger.fingerRT?.fingerPrint)
//            )
//        )
//    }
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Index:",
//                finger.isAFingerStatus(finger.fingerLI?.fingerPrint),
//                "Right Index: ",
//                finger.isAFingerStatus(finger.fingerRI?.fingerPrint)
//            )
//        )
//    }
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Middle:",
//                finger.isAFingerStatus(finger.fingerLM?.fingerPrint),
//                "Right Middle: ",
//                finger.isAFingerStatus(finger.fingerRM?.fingerPrint)
//            )
//        )
//    }
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Ring:",
//                finger.isAFingerStatus(finger.fingerLR?.fingerPrint),
//                "Right Ring: ",
//                finger.isAFingerStatus(finger.fingerRR?.fingerPrint)
//            )
//        )
//    }
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Little:",
//                finger.isAFingerStatus(finger.fingerLL?.fingerPrint),
//                "Right little: ",
//                finger.isAFingerStatus(finger.fingerRL?.fingerPrint)
//            )
//        )
//    }

    return items
}

fun FingerData?.isAFingerStatus(finger: String?): String {
    if (finger == null) return "False"
    return "True"
//    if (this == null) return false.toString()
//    return this.isAFingerCaptured(finger).toString()
}

fun FingerData?.isAFingerCaptured(finger: String?): Boolean {
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
    items.add(ReportRow("Relation:", form.relation.toString(), "Age: ", form.age.toString()))
    items.add(ReportRow("Gender:", form.gender.toString(), "Occupation: ", form.occupation))
    items.add(ReportRow("Can You Read & Write ? :", form.isReadWrite))

    return items
}


fun AlForm1?.getReportRows(): List<ReportRow> {
    if (this == null) return listOf()

    val items = arrayListOf<ReportRow>()
    val form = this

    items.add(ReportRow("Name:", form.getFullName(), null, null))
    items.add(ReportRow("Age:", form.age.toString(), "Gender: ", form.gender))
    items.add(ReportRow("Id:", form.idNumber, "Id Number Type: ", form.idNumberType))
    items.add(ReportRow("Phone Number:", form.phoneNumber))
    items.add(ReportRow("Select Alternate Relation:", form.selectAlternateRlt))

    return items
}

fun AlForm3?.getReportRows(): List<ReportRow> {

    if (this == null) {
        val items = arrayListOf<ReportRow>()
        items.add(ReportRow("Left Thumb:", "False", "Right Thumb: ", "False"))
        items.add(ReportRow("Left Index:","False", "Right Index: ", "False"))
        items.add(ReportRow("Left Middle:", "False", "Right Middle: ", "False"))
        items.add(ReportRow("Left Ring:","False", "Right Ring: ", "False"))
        items.add(ReportRow("Left Little:","False", "Right little: ", "False"))

        return items
    }

    val items = arrayListOf<ReportRow>()
    //val form = this
//    val finger = this.fingerData
//
//    //if (finger == null) return items
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Thumb:",
//                finger.isAFingerStatus(finger.fingerLT?.fingerPrint),
//                "Right Thumb: ",
//                finger.isAFingerStatus(finger.fingerRT?.fingerPrint)
//            )
//        )
//    }
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Index:",
//                finger.isAFingerStatus(finger.fingerLI?.fingerPrint),
//                "Right Index: ",
//                finger.isAFingerStatus(finger.fingerRI?.fingerPrint)
//            )
//        )
//    }
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Middle:",
//                finger.isAFingerStatus(finger.fingerLM?.fingerPrint),
//                "Right Middle: ",
//                finger.isAFingerStatus(finger.fingerRM?.fingerPrint)
//            )
//        )
//    }
//
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Ring:",
//                finger.isAFingerStatus(finger.fingerLR?.fingerPrint),
//                "Right Ring: ",
//                finger.isAFingerStatus(finger.fingerRR?.fingerPrint)
//            )
//        )
//    }
//    if (finger != null) {
//        items.add(
//            ReportRow(
//                "Left Little:",
//                finger.isAFingerStatus(finger.fingerLL?.fingerPrint),
//                "Right little: ",
//                finger.isAFingerStatus(finger.fingerRL?.fingerPrint)
//            )
//        )
//    }

    return items
}