package com.ishu.controller

import com.ishu.extensions.cropImage
import com.ishu.extensions.scale
import com.ishu.model.Point
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import tornadofx.*

class SelectAreaController : Controller() {
    private var imagePoint: Point = Point(0.0, 0.0)

    private lateinit var imageStartPoint: Point
    private lateinit var mouseStartPoint: Point

    fun traceStartPoint(event: MouseEvent, imageView: ImageView, onTrace: (Point) -> Unit) = with(event) {
        val imageX = x * imageView.scale
        val imageY = y * imageView.scale
        imageStartPoint = Point(imageX, imageY)

        val mouseX = sceneX
        val mouseY = sceneY - DEF_MOUSE_SIZE
        mouseStartPoint = Point(mouseX, mouseY)
        onTrace(mouseStartPoint)
    }

    fun tracePoint(event: MouseEvent, imageView: ImageView, onTrace: (Point, Point) -> Unit) = with(event) {
        val imageX = x * imageView.scale
        val imageY = y * imageView.scale
        imagePoint = Point(imageX, imageY)

        var mouseStartPointX = mouseStartPoint.x
        var mouseStartPointY = mouseStartPoint.y
        var mousePointX = sceneX
        var mousePointY = sceneY - DEF_MOUSE_SIZE

        if (imagePoint.x - imageStartPoint.x < 0) {
            mouseStartPointX = sceneX
            mousePointX = mouseStartPoint.x
        }
        if (imagePoint.y - imageStartPoint.y < 0) {
            mouseStartPointY = sceneY - DEF_MOUSE_SIZE
            mousePointY = mouseStartPoint.y
        }

        val resultStartPoint = Point(mouseStartPointX, mouseStartPointY)
        val resultEndPoint = Point(mousePointX, mousePointY)
        onTrace(resultStartPoint, resultEndPoint)
    }

    fun selectArea(imageView: ImageView, onSelected: (image: Image) -> Unit) {
        var imageStartPointX = imageStartPoint.x
        var imageStartPointY = imageStartPoint.y
        var imagePointX = imagePoint.x
        var imagePointY = imagePoint.y

        if (imagePoint.x - imageStartPoint.x < 0) {
            imageStartPointX = imagePoint.x
            imagePointX = imageStartPoint.x
        }
        if (imagePoint.y - imageStartPoint.y < 0) {
            imageStartPointY = imagePoint.y
            imagePointY = imageStartPoint.y
        }

        val resultStartPoint = Point(imageStartPointX, imageStartPointY)
        val resultEndPoint = Point(imagePointX, imagePointY)
        onSelected(imageView.image.cropImage(resultStartPoint, resultEndPoint))
    }

    companion object {
        const val DEF_MOUSE_SIZE = 32.0
    }
}