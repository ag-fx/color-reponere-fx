package com.ishu.model

import com.ishu.utils.ColorSpaceUtils
import javafx.scene.paint.Color
import kotlin.math.abs
import kotlin.math.roundToInt

class ColorSpace(color: Color) {

    val rgb = arrayOf(color.red.convertRgb(), color.green.convertRgb(), color.blue.convertRgb())
    val cmy = IntArray(3)
    val cmyk = IntArray(4)
    val hsv = IntArray(3)
    val hsl = IntArray(3)

    init {
        val rTemp = rgb[0] / MAX_RGB
        val gTemp = rgb[1] / MAX_RGB
        val bTemp = rgb[2] / MAX_RGB
        cmy[0] = (1.0 - rTemp).convertPercent()
        cmy[1] = (1.0 - gTemp).convertPercent()
        cmy[2] = (1.0 - bTemp).convertPercent()

        val cMax = ColorSpaceUtils.max(rTemp, gTemp, bTemp)
        val cMin = ColorSpaceUtils.min(rTemp, gTemp, bTemp)
        val cDelta = cMax - cMin

        val blackKey = 1.0 - cMax
        val divider = 1.0 - blackKey

        var cyan = ZERO_VALUE
        var magenta = ZERO_VALUE
        var yellow = ZERO_VALUE
        if (divider != ZERO_VALUE) {
            cyan = (1.0 - rTemp - blackKey) / divider
            magenta = (1.0 - gTemp - blackKey) / divider
            yellow = (1.0 - bTemp - blackKey) / divider
        }
        cmyk[0] = cyan.convertPercent()
        cmyk[1] = magenta.convertPercent()
        cmyk[2] = yellow.convertPercent()
        cmyk[3] = blackKey.convertPercent()

        var hue = ZERO_VALUE
        if (cDelta != ZERO_VALUE) {
            when (cMax) {
                rTemp -> hue = ((gTemp - bTemp) / cDelta).rem(6)
                gTemp -> hue = ((bTemp - rTemp) / cDelta) + 2
                bTemp -> hue = ((rTemp - gTemp) / cDelta) + 4
            }
        }
        var saturation = when (cMax) {
            ZERO_VALUE -> ZERO_VALUE
            else -> cDelta / cMax
        }
        hsv[0] = hue.convertHsv()
        hsv[1] = saturation.convertPercent()
        hsv[2] = cMax.convertPercent()

        val lightness = (cMax + cMin) / 2
        saturation = when (cDelta) {
            ZERO_VALUE -> ZERO_VALUE
            else -> cDelta / (1 - abs(2 * lightness - 1))
        }
        hsl[0] = hsv[0]
        hsl[1] = saturation.convertPercent()
        hsl[2] = lightness.convertPercent()
    }

    private fun Double.convertRgb(): Int = (this * MAX_RGB).roundToInt()
    private fun Double.convertPercent(): Int = (this * ONE_HUNDRED).roundToInt()
    private fun Double.convertHsv(): Int {
        val value = (this * HUE_DEGREE).roundToInt()
        return if (value < 0) value + MAX_DEGREE.toInt() else value
    }

    companion object {
        const val ZERO_VALUE: Double = 0.0
        const val ONE_HUNDRED: Double = 100.0
        const val MAX_RGB: Double = 255.0
        const val HUE_DEGREE: Double = 60.0
        const val MAX_DEGREE: Double = 360.0
    }
}