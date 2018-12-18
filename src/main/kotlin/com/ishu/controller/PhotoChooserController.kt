package com.ishu.controller

import com.ishu.extensions.imgExtensions
import com.ishu.extensions.isImage
import com.ishu.extensions.toUrl
import javafx.scene.image.Image
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.stage.FileChooser
import org.controlsfx.control.Notifications
import tornadofx.*

class PhotoChooserController : Controller() {

    fun chooseImage(onHasImageFile: (image: Image) -> Unit) {
        val filters = arrayOf(FileChooser.ExtensionFilter("images (*.jpg, *.png)", imgExtensions))
        chooseFile("Choose image", filters, FileChooserMode.Single).firstOrNull()?.let {
           onHasImageFile(Image(it.toUrl()))
        }
    }

    fun acceptTransferModes(event: DragEvent, onHasFile: () -> Unit) = with(event) {
        if (dragboard.hasFiles()) {
            acceptTransferModes(TransferMode.COPY)
            onHasFile()
        } else {
            consume()
        }
    }

    fun onCheckDroppedImage(event: DragEvent, onHasImageFile: (image: Image) -> Unit) = with(event) {
        dragboard.files.firstOrNull()?.let {
            if (it.isImage()) {
                isDropCompleted = true
                onHasImageFile(Image(it.toUrl()))
            } else {
                Notifications.create()
                        .title("Warning")
                        .text("The file must have extension .jpg or .png")
                        .showWarning()
                isDropCompleted = false
            }
            consume()
        }
    }
}