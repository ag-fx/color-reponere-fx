package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.DataController
import com.ishu.controller.MainController
import com.ishu.extensions.bindPrimaryStageWidth
import com.ishu.extensions.saveToFile
import com.ishu.extensions.toCaption
import com.ishu.model.ColorSpace
import com.ishu.utils.ColorSpaceUtils
import javafx.beans.value.ChangeListener
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.control.*
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

    private val imageListener: ChangeListener<in Number> = ChangeListener { _, _, newValue ->
        with(dataController) {
            fuzziness = newValue as Double
            valueLabel.text = newValue.formatToTenths()
            runLater { computeData { refreshImage(mainController.imageToShow) } }
        }
    }
    
    private lateinit var imageView: ImageView
    private lateinit var distanceLabel: Label
    private lateinit var valueLabel: Label
    private lateinit var slider: Slider

    private lateinit var pickedColorPicker: ColorPicker
    private lateinit var pickedColorLabel: Label

    private lateinit var colorToWritePicker: ColorPicker
    private lateinit var colorToWriteLabel: Label

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
            distanceLabel = label {}
            hbox {
                alignment = Pos.CENTER
                spacing = 50.0
                pickedColorPicker = colorpicker {
                    setOnAction {
                        showColor(pickedColorLabel, value)
                        showDistance(value, colorToWritePicker.value)
                        dataController.apply {
                            pickedColor = value
                            runLater { computeData { refreshImage(mainController.imageToShow) } }
                        }
                    }
                }
                colorToWritePicker = colorpicker {
                    setOnAction {
                        showColor(colorToWriteLabel, value)
                        showDistance(pickedColorPicker.value, value)
                        dataController.apply {
                            colorToWrite = value
                            runLater { computeData { refreshImage(mainController.imageToShow) } }
                        }
                    }
                }
            }
            hbox {
                alignment = Pos.CENTER
                spacing = 50.0
                pickedColorLabel = label("Picked color:\n")
                colorToWriteLabel = label("Color to write:\n")
            }
            hbox {
                alignment = Pos.CENTER
                spacing = 5.0
                label("Fuzziness:")
                slider = slider(
                        ColorSpaceUtils.MIN_DIFF_SENSITIVITY,
                        ColorSpaceUtils.MAX_DIFF_SENSITIVITY,
                        dataController.fuzziness) {
                    valueProperty().addListener(imageListener)
                }
                valueLabel = label(dataController.fuzziness.formatToTenths())
            }
            addClass(Styles.dataBackground)
            bindPrimaryStageWidth(2, 2)
        }
        contextMenu.items.addAll(contextMenuItems)
        setOnMouseClicked { contextMenu.hide() }
    }

    override fun onDock() = with(dataController) {
        super.onDock()
        pickedColorPicker.value = pickedColor
        colorToWritePicker.value = colorToWrite
        slider.value = fuzziness

        showDistance(pickedColorPicker.value, colorToWritePicker.value)
        showColor(pickedColorLabel, pickedColorPicker.value)
        showColor(colorToWriteLabel, colorToWritePicker.value)

        swapImage(mainController.imageToShow)
    }

    override fun onUndock() {
        super.onUndock()
        distanceLabel.text = ""
        pickedColorLabel.text = "Picked color:\n"
        colorToWriteLabel.text = "Color to write:\n"
        dataController.resetData()
    }

    private fun showColor(label: Label, color: Color) = with(ColorSpace(color)) {
        label.text = "RGB:  ${rgb.toCaption()}\n" +
                "CMY:  ${cmy.toCaption()}\n" +
                "CMYK: ${cmyk.toCaption()}\n" +
                "HSV:  ${hsv.toCaption()}\n" +
                "HSL:  ${hsl.toCaption()}\n" +
                ""
    }

    private fun showDistance(firstColor: Color, secondColor: Color) {
        val distance = ColorSpaceUtils.calcDistance(firstColor, secondColor)
        val distanceAprox = ColorSpaceUtils.calcApproxDistance(firstColor, secondColor)
        distanceLabel.text = "Euclidean:\n" +
                "1) $distance\n" +
                "2) $distanceAprox\n" +
                ""
    }

    private fun Double.formatToTenths(): String = String.format("%.2f", this)
}