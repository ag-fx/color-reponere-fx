package com.ishu.extensions

fun Array<Int>.toCaption(): String {
    var result = ""
    this.forEachIndexed { index, value ->
        result += if (index != size - 1) "$value, "
        else "$value"
    }
    return result
}

fun IntArray.toCaption(): String {
    var result = ""
    this.forEachIndexed { index, value ->
        result += if (index != size - 1) "$value, "
        else "$value"
    }
    return result
}