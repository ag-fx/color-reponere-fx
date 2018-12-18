package com.ishu

import com.ishu.extensions.toWebColor
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import tornadofx.*

class Styles : Stylesheet() {

    companion object {
        val defaultBorder by cssclass()
        val fullscreenBorder by cssclass()

        val lightBackground by cssclass()
        val darkBackground by cssclass()
        val dropBackground by cssclass()
        val dataBackground by cssclass()

        val backButton by cssclass()
        val maximizeButton by cssclass()
        val exitButton by cssclass()

        val defaultLabel by cssclass()
        val defaultButton by cssclass()

        private const val primaryColor = "#78909c"
        private const val acentColor = "#e91e63"
        private const val textColor = "#3f51b5"
        private const val bgColor = "#f5f5f5"
        private const val lightGrayColor = "#eeeeee"
        private const val grayColor = "#757575"
    }

    init {
        defaultBorder {
            borderStyle = multi(BorderStrokeStyle.SOLID)
            borderWidth = multi(box(1.px))
            borderColor = multi(box(acentColor.toWebColor()))
        }

        fullscreenBorder {
            borderStyle = multi(BorderStrokeStyle.NONE)
            borderWidth = multi(box(0.px))
            borderColor = multi(box(Color.TRANSPARENT))
        }

        lightBackground { backgroundColor = multi(bgColor.toWebColor()) }
        darkBackground { backgroundColor = multi(Color.BLACK) }
        dropBackground { backgroundColor = multi(lightGrayColor.toWebColor()) }

        dataBackground {
            backgroundColor = multi(bgColor.toWebColor())
            borderStyle = multi(BorderStrokeStyle.SOLID)
            borderWidth = multi(box(0.px, 0.px, 0.px, 1.px))
            borderColor = multi(box(acentColor.toWebColor()))
        }

        exitButton {
            fontSize = 16.px
            backgroundColor = multi(Color.TRANSPARENT)
            backgroundRadius = multi(box(0.px))
            textFill = grayColor.toWebColor()
            padding = box(0.px, 5.px)
            and(hover) {
                textFill = bgColor.toWebColor()
                backgroundColor = multi(acentColor.toWebColor())
            }
        }

        maximizeButton {
            fontSize = 16.px
            backgroundColor = multi(Color.TRANSPARENT)
            backgroundRadius = multi(box(0.px))
            textFill = grayColor.toWebColor()
            padding = box(0.px, 5.px)
            and(hover) {
                textFill = "#bdbdbd".toWebColor()
                backgroundColor = multi(lightGrayColor.toWebColor())
            }
        }

        backButton {
            fontSize = 16.px
            backgroundColor = multi(Color.TRANSPARENT)
            backgroundRadius = multi(box(0.px))
            textFill = grayColor.toWebColor()
            padding = box(0.px, 5.px)
        }

        defaultLabel {
            fontSize = 24.px
            textFill = primaryColor.toWebColor()
        }

        defaultButton {
            fontSize = 24.px
            padding = box(5.px)
            backgroundColor = multi(textColor.toWebColor())
            textFill = lightGrayColor.toWebColor()
            and(pressed) {
                backgroundColor = multi(textColor.toWebColor())
                scaleY = 0.98
            }
        }
    }
}