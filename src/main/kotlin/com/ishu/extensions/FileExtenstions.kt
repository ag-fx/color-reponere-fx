package com.ishu.extensions

import java.io.File

val imgExtensions: List<String> = listOf("*.jpg", "*.jpeg", "*.png", "*.PNG")

fun File.toUrl(): String = this.toURI().toURL().toString()

fun File.isImage(): Boolean {
    imgExtensions.forEach {
        if (this.extension == it.removeRange(0, 2)) return true
    }
    return false
}