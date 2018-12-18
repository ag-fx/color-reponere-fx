package com.ishu.controller

import com.ishu.Styles
import com.ishu.view.PhotoChooserView
import com.ishu.view.PickColorView
import com.ishu.view.ResultView
import com.ishu.view.SelectAreaView
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.*

class MainController : Controller() {
    private val photoChooserView: PhotoChooserView by inject()
    private val selectAreaView: SelectAreaView by inject()
    private val pickColorView: PickColorView by inject()
    private val resultView: ResultView by inject()

    private lateinit var currentView: View

    var isBackButtonVisible = SimpleBooleanProperty(DEF_IS_BACK_BUTTON_VISIBLE)
    val backgroundClassProperty: SimpleObjectProperty<CssRule> = SimpleObjectProperty(DEF_BACKGROUND_CLASS_PROPERTY)
    val borderClassProperty: SimpleObjectProperty<CssRule> = SimpleObjectProperty(DEF_BORDER_CLASS_PROPERTY)

    val imageToShow: SimpleObjectProperty<Image?> = SimpleObjectProperty(null)

    fun init(startView: View) {
        currentView = startView
    }

    fun showPhotoChooserView() {
        isBackButtonVisible.set(false)
        backgroundClassProperty.set(Styles.lightBackground)
        changeView(photoChooserView)
    }

    fun showSelectAreaView() {
        isBackButtonVisible.set(true)
        backgroundClassProperty.set(Styles.darkBackground)
        changeView(selectAreaView)
    }

    fun showPickColorView() {
        changeView(pickColorView)
    }

    fun showResultView() {
        changeView(resultView)
    }

    fun showBorders() {
        borderClassProperty.set(DEF_BORDER_CLASS_PROPERTY)
    }

    fun removeBorders() {
        borderClassProperty.set(Styles.fullscreenBorder)
    }

    fun getCurrentView(): View = currentView

    private fun changeView(viewToChange: View) {
        currentView.replaceWith(viewToChange, sizeToScene = true, centerOnScreen = true)
        currentView = viewToChange
    }

    companion object {
        private const val DEF_IS_BACK_BUTTON_VISIBLE: Boolean = false
        private val DEF_BACKGROUND_CLASS_PROPERTY: CssRule = Styles.lightBackground
        private val DEF_BORDER_CLASS_PROPERTY: CssRule = Styles.defaultBorder
    }
}