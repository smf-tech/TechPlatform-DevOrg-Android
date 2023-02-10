package com.octopusbjsindia.models.forms

import com.google.gson.annotations.SerializedName


data class MultiTextItem(
    val inputType: String? = null,
    val maxLength: Int? = null,
    val name: String,
    val placeHolder: String? = null,
    val requiredErrorText: String? = null,
    val title: String,
    val minDate: Long? = null,
    val maxDate: Long? = null,
    @SerializedName("minDays")
    var pastAllowedDays: Int? = null,
    @SerializedName("maxDays")
    val futureAllowedDays: Int? = null
)