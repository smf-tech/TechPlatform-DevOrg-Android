package com.octopusbjsindia.models.forms

import com.octopusbjsindia.models.LocaleData
import java.io.Serializable


data class MultiTextItem(
    val inputType: String? = null,
    val maxLength: Int? = null,
    val name: String,
    val placeHolder: LocaleData? = null,
    val title: LocaleData,
) : Serializable