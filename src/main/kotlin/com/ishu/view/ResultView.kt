package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.DataController
import com.ishu.controller.MainController
import com.ishu.extensions.bindPrimaryStageWidth
import com.ishu.extensions.saveToFile
import com.ishu.utils.ColorSpaceUtils
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.Slider
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import tornadofx.*

class ResultView : View() {
    private val mainController: MainController by inject()
    private val dataController: DataController by inject()

    private val contextMenu: ContextMenu = ContextMenu()
    private val contextMenuItems: List<MenuItem>
        get() {
            val backItem = MenuItem("Back")
            backItem.setOnAction { mainController.showPhotoChooserView() }
            val swapItem = MenuItem("Swap image")
            swapItem.setOnAction { dataController.swapImage(mainController.imageToShow) }
            val saveItem = MenuItem("Save image")
            saveItem.setOnAction { imageView.image.saveToFile("result", openOnComplete = true) }
            return listOf(backItem, swapItem, saveItem)
        }

    private lateinit var imageView: ImageView
    private lateinit var slider: Slider
    private lateinit var scoreLabel: Label
    private lateinit var descriptionLabel: Label
    private lateinit var valueLabel: Label

    override val root = hbox {
        imageView = imageview {
            alignment = Pos.CENTER
            isPreserveRatio = true
            isSmooth = true
            isCache = true
            cacheHint = CacheHint.SPEED
            setOnContextMenuRequested { contextMenu.show(this@imageview, it.screenX, it.screenY) }
            dataController.bindSourceImage(this@imageview, mainController.imageToShow)
            bindPrimaryStageWidth(2)
        }
        vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            scoreLabel = label { style { fontSize = 22.px } }
            descriptionLabel = label { style { fontSize = 18.px } }
            hbox {
                alignment = Pos.CENTER
                spacing = 5.0
                label("Fuzziness:")
                slider = slider(
                        ColorSpaceUtils.MIN_DIFF_SENSITIVITY,
                        ColorSpaceUtils.MAX_DIFF_SENSITIVITY,
                        dataController.getFuzziness()) {
                    valueProperty().addListener { _, _, newFuzziness ->
                        with(dataController) {
                            setFuzziness(newFuzziness as Double)
                            valueLabel.text = newFuzziness.formatToTenths()
                            runLater { refreshImage(mainController.imageToShow) }
                        }
                    }
                }
                valueLabel = label(dataController.getFuzziness().formatToTenths())
            }
            addClass(Styles.dataBackground)
            bindPrimaryStageWidth(2, 2)
        }
        contextMenu.items.addAll(contextMenuItems)
        setOnMouseClicked { contextMenu.hide() }
    }

    override fun onDock() = with(dataController) {
        super.onDock()
        slider.value = getFuzziness()
    }

    override fun onUndock() {
        super.onUndock()
        dataController.resetData()
    }

    private fun showData(firstColor: Color, secondColor: Color) {

    }

    private fun Double.formatToTenths(): String = String.format("%.2f", this)
}