package com.ishu

import com.ishu.view.MainView
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

class ColorReponereFx: App(MainView::class, Styles::class) {

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        super.start(stage)
    }

    init {
        reloadStylesheetsOnFocus()
    }
}