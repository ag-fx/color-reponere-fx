package com.ishu.extensions

import javafx.scene.Cursor
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.stage.Screen
import tornadofx.*

val screenWidth: Double
    get() = Screen.getPrimary().visualBounds.width

val screenHeight: Double
    get() = Screen.getPrimary().visualBounds.height

fun toggleFullscreenMode() {
    FX.primaryStage.isFullScreen = !FX.primaryStage.isFullScreen
}

fun setMouseCursor(cursor: Cursor) {
    FX.primaryStage.scene.cursor = cursor
}

fun ImageView.bindPrimaryStageWidth(scale: Int = 1, int: Int = 0) =
        fitWidthProperty().bind((FX.primaryStage.widthProperty() / scale) - int)

fun Pane.bindPrimaryStageWidth(scale: Int = 1, int: Int = 0) =
        prefWidthProperty().bind((FX.primaryStage.widthProperty() / scale) - int)

fun Pane.bindPrimaryStageHeight(scale: Int = 1, int: Int = 0) =
        prefHeightProperty().bind((FX.primaryStage.widthProperty() / scale) - int)

fun Pane.bindPrimaryStageWidthMax(scale: Int = 1, int: Int = 0) =
        maxWidthProperty().bind((FX.primaryStage.widthProperty() / scale) - int)