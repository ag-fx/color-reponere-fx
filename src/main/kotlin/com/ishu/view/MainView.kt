package com.ishu.view

import com.ishu.Styles
import com.ishu.controller.MainController
import com.ishu.extensions.screenHeight
import com.ishu.extensions.screenWidth
import tornadofx.*

class MainView : View("color-reponere-fx") {
    private val mainController: MainController by inject()

    private val titleView: TitleView by inject()
    private val photoChooserView: PhotoChooserView by inject()

    init {
        // init start view
        mainController.init(photoChooserView)
    }

    override val root = borderpane {
        top { this += titleView }
        center { this += mainController.getCurrentView() }
        bindClass(mainController.backgroundClassProperty)
        bindClass(mainController.borderClassProperty)
        addClass(Styles.defaultBorder)
        setPrefSize(screenWidth / WINDOW_SCALE, screenHeight / WINDOW_SCALE)
    }

    companion object {
        private const val WINDOW_SCALE: Double = 1.5
    }
}