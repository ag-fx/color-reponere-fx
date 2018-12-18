package com.ishu.utils

object AppUtils {
    fun getResourcePath(name: String): String = javaClass.getResource(name).toString()
}