package com.xplo.data.model.content

import com.google.gson.annotations.SerializedName


data class FormRqb(

    @SerializedName("id")
    val id: String? = null

)

data class FormRsp(

    @SerializedName("id")
    val id: String? = null

)

data class FormsRqb(

    @SerializedName("beneficiaries")
    val forms: List<FormsRqb>? = null

)

data class FormsRsp(

    @SerializedName("id")
    val id: String? = null

)
