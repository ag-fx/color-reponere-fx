package com.ishu.extensions

import com.ishu.model.Point
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.*
import javafx.scene.paint.Color
import java.awt.Desktop
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.abs

val ImageView.scale: Double
    get () = image.width / fitWidth

fun Image.forEachPixelWrite(colorAtPixel: (Int, Int, Color, PixelWriter) -> Unit, onEnd: (WritableImage) -> Unit) {
    val width = width.toInt()
    val height = height.toInt()
    val wImage = WritableImage(width, height)
    for (i in 0 until width) {
        for (j in 0 until height) {
            colorAtPixel(i, j, pixelReader.getColor(i, j), wImage.pixelWriter)
        }
    }
    onEnd(wImage)
}

fun Image.saveToFile(fileName: String, format: String = "png", openOnComplete: Boolean = false) {
    val outputFile = File("$fileName.$format")
    val bImage = SwingFXUtils.fromFXImage(this, null)
    try {
        ImageIO.write(bImage, format, outputFile)
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
    if (openOnComplete) Desktop.getDesktop().open(outputFile)
}

fun Image.cropImage(startPoint: Point, endPoint: Point): Image {
    val x = if (startPoint.x > 0) {
        startPoint.x.toInt()
    } else {
        0
    }
    val y = if (startPoint.y > 0) {
        startPoint.y.toInt()
    } else {
        0
    }
    val min = 1.0
    val maxWidth = width - x
    val maxHeight = height - y
    val pointWidth = abs(endPoint.x - x).checkValue(min, maxWidth)
    val pointHeight = abs(endPoint.y - y).checkValue(min, maxHeight)
    return WritableImage(pixelReader, x, y, pointWidth, pointHeight)
}

fun PixelReader.getColor(x: Double, y: Double): Color = getColor(x.toInt(), y.toInt())

fun String.toWebColor(): Color = Color.web(this)

private fun Double.checkValue(min: Double, max: Double): Int {
    return when {
        this <= 0 -> min.toInt()
        this > max -> max.toInt()
        else -> this.toInt()
    }
}