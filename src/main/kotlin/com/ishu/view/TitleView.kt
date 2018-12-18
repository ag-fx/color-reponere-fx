package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.MainController
import com.ishu.controller.TitleController
import com.ishu.extensions.toWebColor
import com.ishu.extensions.toggleFullscreenMode
import javafx.application.Platform
import javafx.scene.layout.Pane
import tornadofx.*

class TitleView : View() {
    private val mainController: MainController by inject()
    private val titleController: TitleController by inject()

    override val root = borderpane {
        left = button {
            text = "\uD83E\uDC68"
            addClass(Styles.backButton)
            action { mainController.showPhotoChooserView() }
            visibleWhen(mainController.isBackButtonVisible)
        }
        right = hbox {
            button {
                text = "\uD83D\uDDD6"
                addClass(Styles.maximizeButton)
                action { toggleFullscreenMode() }
            }
            button {
                text = "\uD83D\uDDD9"
                addClass(Styles.exitButton)
                action { Platform.exit() }
            }
        }
        style { backgroundColor = multi("#e0e0e0".toWebColor()) }
        FX.primaryStage.fullScreenProperty().addListener { _, _, isFullscreen ->
            if (!isFullscreen) {
                enableMouseDragEvents(this)
                mainController.showBorders()
            } else {
                disableMouseDragEvents(this)
                mainController.removeBorders()
            }
        }
        enableMouseDragEvents(this)
        setOnMouseClicked { titleController.checkDoubleClick(it) { toggleFullscreenMode() } }
    }

    private fun enableMouseDragEvents(pane: Pane) = with(pane) {
        setOnMousePressed { titleController.calculateDelta(it) }
        setOnMouseDragged { titleController.moveStage(it) }
    }

    private fun disableMouseDragEvents(pane: Pane) = with(pane) {
        onMousePressed = null
        onMouseDragged = null
    }
}
