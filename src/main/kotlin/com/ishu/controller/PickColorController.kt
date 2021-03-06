package com.ishu.controller

import com.ishu.extensions.getColor
import com.ishu.model.Point
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import tornadofx.*

class PickColorController : Controller() {
    private var mousePoint: Point = Point(0.0, 0.0)
    private var mouseColor: SimpleObjectProperty<Color?> = SimpleObjectProperty(null)

    fun calculateDelta(event: MouseEvent, imageView: ImageView) = with(imageView) {
        val scale = image.width / fitWidth
        val imageX = event.x * scale
        val imageY = event.y * scale
        mousePoint = Point(imageX, imageY)
    }

    fun traceColor(imageView: ImageView) = mouseColor.set(imageView.image.pixelReader.getColor(mousePoint.x, mousePoint.y))

    fun pickColor(imageView: ImageView, onPicked: (color: Color) -> Unit) = onPicked(imageView.image.pixelReader.getColor(mousePoint.x, mousePoint.y))

    fun getColorTracedByMouse(): SimpleObjectProperty<Color?> = mouseColor
}