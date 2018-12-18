package com.ishu.controller

import com.ishu.model.Point
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.*

class TitleController : Controller() {
    private var mousePoint: Point = Point(0.0, 0.0)

    fun calculateDelta(event: MouseEvent) = with(event) {
        val deltaX = primaryStage.x - screenX
        val deltaY = primaryStage.y - screenY
        mousePoint = Point(deltaX, deltaY)
    }

    fun moveStage(event: MouseEvent) = with(event) {
        primaryStage.x = screenX + mousePoint.x
        primaryStage.y = screenY + mousePoint.y
    }

    fun checkDoubleClick(event: MouseEvent, onSuccessCheck: () -> Unit) = with(event) {
        if ((button == MouseButton.PRIMARY) && (clickCount == 2)) {
            onSuccessCheck()
        }
    }
}