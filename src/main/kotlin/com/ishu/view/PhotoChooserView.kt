package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.MainController
import com.ishu.controller.ImageChooserController
import com.ishu.extensions.bindPrimaryStageWidth
import com.ishu.utils.AppUtils
import javafx.geometry.Pos
import javafx.scene.image.Image
import tornadofx.*

class PhotoChooserView : View() {
    private val mainController: MainController by inject()
    private val imageChooserController: ImageChooserController by inject()

    override val root = vbox {
        alignment = Pos.CENTER
        spacing = 5.0
        imageview {
            runAsync {
                image = Image(AppUtils.getResourcePath("/image.png"))
                isPreserveRatio = true
                isSmooth = true
                bindPrimaryStageWidth(5)
            }
        }
        label("Drag image here").addClass(Styles.defaultLabel)
        label("or").addClass(Styles.defaultLabel)
        button("Choose file") {
            action {
                imageChooserController.chooseImage {
                    mainController.imageToShow.set(it)
                    mainController.showSelectAreaView()
                }
            }
            addClass(Styles.defaultButton)
        }
        setOnDragOver { imageChooserController.acceptTransferModes(it) { addClass(Styles.dropBackground) } }
        setOnDragExited { removeClass(Styles.dropBackground) }
        setOnDragDropped { event ->
            imageChooserController.onCheckDroppedImage(event) {
                mainController.imageToShow.set(it)
                mainController.showSelectAreaView()
            }
        }
    }
}
