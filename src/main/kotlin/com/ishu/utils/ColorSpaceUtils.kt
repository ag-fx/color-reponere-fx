package com.ishu.utils

import javafx.scene.paint.Color
import kotlin.math.pow
import kotlin.math.sqrt

object ColorSpaceUtils {

    fun calcDistance(first: Color, second: Color): Double {
        val rDiff = (second.red - first.red).pow(2)
        val gDiff = (second.green - first.green).pow(2)
        val bDiff = (second.blue - first.blue).pow(2)
        return sqrt(rDiff + gDiff + bDiff)
    }

    fun calcApproxDistance(first: Color, second: Color): Double {
        val rTemp = (first.red + second.red) / 2
        val rDelta = first.red - second.red
        val gDelta = first.green - second.green
        val bDelta = first.blue - second.blue

        val firstFactor = 2.0 * rDelta.pow(2)
        val secondFactor = 4.0 * gDelta.pow(2)
        val thirdFactor = 3.0 * bDelta.pow(2)
        val lastFactor = (rTemp * (rDelta.pow(2) - bDelta.pow(2))) / 256.0
        return firstFactor.plus(secondFactor).plus(thirdFactor).plus(lastFactor)
    }

    const val DIFF_FUZZINESS: Double = 0.15
    const val MIN_DIFF_SENSITIVITY: Double = 0.01
    const val MAX_DIFF_SENSITIVITY: Double = 1.0
}