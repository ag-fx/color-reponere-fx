package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.DataController
import com.ishu.controller.MainController
import com.ishu.controller.PickColorController
import com.ishu.extensions.bindPrimaryStageHeight
import com.ishu.extensions.bindPrimaryStageWidth
import com.ishu.extensions.bindPrimaryStageWidthMax
import com.ishu.extensions.setMouseCursor
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.Cursor
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*

class PickColorView : View() {
    private val mainController: MainController by inject()
    private val dataController: DataController by inject()
    private val pickColorController: PickColorController by inject()

    override val root = hbox {
        imageview {
            alignment = Pos.CENTER
            isPreserveRatio = true
            isSmooth = true
            isCache = true
            cacheHint = CacheHint.SPEED
            setOnMouseEntered { setMouseCursor(Cursor.HAND) }
            setOnMouseExited { setMouseCursor(Cursor.DEFAULT) }
            setOnMouseMoved {
                pickColorController.calculateDelta(it, this)
                pickColorController.traceColor(this)
            }
            setOnMouseClicked {
                setMouseCursor(Cursor.DEFAULT)
                pickColorController.pickColor(this) { pickedColor ->
                    dataController.pickedColor = pickedColor
                    runLater {
                        dataController.computeData {
                            mainController.showResultView()
                        }
                    }
                }
            }
            dataController.bindSourceImage(this@imageview, mainController.imageToShow)
            bindPrimaryStageWidth(2)
        }
        vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            label("Pick color").addClass(Styles.defaultLabel)
            pane {
                pickColorController.getColorTracedByMouse().addListener { _, _, newColor ->
                    newColor?.let { background = Background(BackgroundFill(it, CornerRadii.EMPTY, insets(0.0))) }
                }
                background = Background(BackgroundFill(Color.BLACK, CornerRadii.EMPTY, insets(0.0)))
                border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
                bindPrimaryStageWidthMax(DISPLAY_COLOR_SCALE)
                bindPrimaryStageHeight(DISPLAY_COLOR_SCALE * 2)
            }
            bindPrimaryStageWidth(2, 2)
            addClass(Styles.dataBackground)
        }
    }

    companion object {
        const val DISPLAY_COLOR_SCALE = 7
    }
}