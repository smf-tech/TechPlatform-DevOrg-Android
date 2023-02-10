package com.octopusbjsindia.models.forms


data class MultiTextItem(
    val inputType: String,
    val maxLength: Int,
    val name: String,
    val placeHolder: String,
    val requiredErrorText: String,
    val title: String,
    @JvmField
    val validators: List<Validator>,
    val minDate: Long,
    val maxDate: Long,
    var pastAllowedDays: Int,
    val futureAllowedDays: Int
)