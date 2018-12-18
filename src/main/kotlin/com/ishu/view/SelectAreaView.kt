package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.DataController
import com.ishu.controller.MainController
import com.ishu.controller.SelectAreaController
import com.ishu.extensions.bindPrimaryStageWidth
import com.ishu.extensions.setMouseCursor
import com.ishu.model.Point
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.StrokeLineCap
import tornadofx.*

class SelectAreaView : View() {
    private val mainController: MainController by inject()
    private val dataController: DataController by inject()
    private val selectAreaController: SelectAreaController by inject()

    private lateinit var selectArea: Rectangle

    override val root = hbox {
        borderpane {
            center = imageview {
                alignment = Pos.CENTER
                isPreserveRatio = true
                isSmooth = true
                isCache = true
                cacheHint = CacheHint.SPEED
                setOnMouseEntered { setMouseCursor(Cursor.CROSSHAIR) }
                setOnMouseExited { setMouseCursor(Cursor.DEFAULT) }
                setOnMousePressed {
                    setMouseCursor(Cursor.CROSSHAIR)
                    if (it.isPrimaryButtonDown) {
                        selectAreaController.traceStartPoint(it, this) { startPoint ->
                            selectArea.draw(startPoint, Point(startPoint.x + 1, startPoint.y + 1))
                        }
                    }
                }
                setOnMouseDragged {
                    selectAreaController.tracePoint(it, this) { startPoint, endPoint ->
                        selectArea.draw(startPoint, endPoint)
                    }
                }
                setOnMouseReleased {
                    setMouseCursor(Cursor.DEFAULT)
                    selectArea.reset()
                    selectAreaController.selectArea(this) { croppedImage ->
                        mainController.imageToShow.set(croppedImage)
                        mainController.showPickColorView()
                    }
                }
                dataController.bindSourceImage(this@imageview, mainController.imageToShow)
                bindPrimaryStageWidth(2)
            }
            rectangle(0, 0, 0, 0) {
                selectArea = this
                stroke = Color.ALICEBLUE
                strokeWidth = 1.0
                strokeLineCap = StrokeLineCap.ROUND
                fill = Color.LIGHTBLUE.deriveColor(
                        0.0,
                        1.2,
                        1.0,
                        0.3
                )
                this@borderpane.children.add(this)
            }
            bindPrimaryStageWidth(2)
        }
        vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            label("Select area to crop image").addClass(Styles.defaultLabel)
            button("Skip") {
                action { mainController.showPickColorView() }
                addClass(Styles.defaultButton)
            }
            bindPrimaryStageWidth(2, 2)
            addClass(Styles.dataBackground)
        }
    }

    private fun Rectangle.draw(startPoint: Point, endPoint: Point) {
        x = startPoint.x
        y = startPoint.y
        width = endPoint.x - startPoint.x
        height = endPoint.y - startPoint.y
    }

    private fun Rectangle.reset() {
        x = 0.0
        y = 0.0
        width = 0.0
        height = 0.0
    }
}