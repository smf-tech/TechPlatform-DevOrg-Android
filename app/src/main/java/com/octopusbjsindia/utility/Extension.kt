package com.octopusbjsindia.utility

fun String.getFormattedHeightValue(): String {
    return if (Constants.HeightMasterData.heightMap.containsKey(this)) {
        Constants.HeightMasterData.heightMap[this] ?: this
    } else this
}