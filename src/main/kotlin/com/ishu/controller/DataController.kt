package com.ishu.controller

import com.ishu.extensions.forEachPixelWrite
import com.ishu.utils.ColorSpaceUtils
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import tornadofx.*

class DataController : Controller() {

    var pickedColor: Color = DEF_COLOR_TO_WRITE
    var colorToWrite: Color = DEF_COLOR_TO_WRITE
    var fuzziness: Double = ColorSpaceUtils.DIFF_FUZZINESS

    private var isCurrentImageSource = DEF_IS_CURRENT_IMAGE_SOURCE
    private var isDataCalculated = DEF_IS_DATA_CALCULATED

    private lateinit var sourceImage: Image
    private lateinit var resultImage: Image

    fun bindSourceImage(imageView: ImageView, imageToShow: SimpleObjectProperty<Image?>) = with(imageView.imageProperty()) {
        addListener { _, _, newImage ->
            if (newImage != null && !isDataCalculated) {
                sourceImage = newImage
            }
        }
        bind(imageToShow)
    }

    fun computeData(onEnd: () -> Unit) {
        // find similar colors
        // change matched colors with the specific one
        sourceImage.forEachPixelWrite({ x, y, color, pixelWriter ->
            val colorDifference = ColorSpaceUtils.calcApproxDistance(color, pickedColor)
            if (colorDifference <= fuzziness) {
                pixelWriter.setColor(x, y, colorToWrite)
            } else {
                pixelWriter.setColor(x, y, Color.BLACK)
            }
        }, { writableImage -> resultImage = writableImage })
        isDataCalculated = true
        onEnd()
    }

    fun swapImage(imageToShow: SimpleObjectProperty<Image?>) {
        if (isCurrentImageSource) {
            isCurrentImageSource = false
            imageToShow.set(resultImage)
        } else {
            isCurrentImageSource = true
            imageToShow.set(sourceImage)
        }
    }

    fun resetData() {
        fuzziness = ColorSpaceUtils.DIFF_FUZZINESS
        isCurrentImageSource = DEF_IS_CURRENT_IMAGE_SOURCE
        isDataCalculated = DEF_IS_DATA_CALCULATED
    }

    fun refreshImage(imageToShow: SimpleObjectProperty<Image?>) {
        computeData {}
        if (!isCurrentImageSource) imageToShow.set(resultImage)
    }

    companion object {
        private const val DEF_IS_CURRENT_IMAGE_SOURCE: Boolean = true
        private const val DEF_IS_DATA_CALCULATED: Boolean = false
        private val DEF_COLOR_TO_WRITE: Color = Color.WHITE
    }
}