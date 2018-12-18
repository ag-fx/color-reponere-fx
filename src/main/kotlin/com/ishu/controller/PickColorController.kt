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
        val deltaX = event.x * scale
        val deltaY = event.y * scale
        mousePoint = Point(deltaX, deltaY)
    }

    fun traceColor(imageView: ImageView) = with(imageView) {
        mouseColor.set(image.pixelReader.getColor(mousePoint.x, mousePoint.y))
    }

    fun pickColor(imageView: ImageView, onPicked: (color: Color) -> Unit) = with(imageView) {
        onPicked(image.pixelReader.getColor(mousePoint.x, mousePoint.y))
    }

    fun getColorTracedByMouse(): SimpleObjectProperty<Color?> = mouseColor
}