package com.octopusbjsindia.models.forms

import com.octopusbjsindia.models.LocaleData
import java.io.Serializable


data class MultiTextItem(
    val inputType: String? = null,
    val maxLength: Int? = null,
    val name: String,
    val placeHolder: String? = null,
    val requiredErrorText: String? = null,
    val title: LocaleData,
    /*val minDate: Long? = null,
    val maxDate: Long? = null,
    @SerializedName("minDays")
    var pastAllowedDays: Int? = null,
    @SerializedName("maxDays")
    val futureAllowedDays: Int? = null*/
) : Serializable