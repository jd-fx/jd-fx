package com.adlerd.jdfx.gui.fx.tabs

import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File

class ImageTab(imageFile: File) : CustomTab() {

    private val borderPane = BorderPane()
    private val controlPane: HBox
    private val imagePane: ImageView
    val image: Image

    init {
        this.text = imageFile.name
        controlPane = initControls()
        controlPane.minWidthProperty().bind(borderPane.widthProperty())
        controlPane.prefWidthProperty().bind(borderPane.widthProperty())
        controlPane.maxWidthProperty().bind(borderPane.widthProperty())

        image = Image(BufferedInputStream(ByteArrayInputStream(imageFile.absoluteFile.readBytes())))

        imagePane = ImageView(image)
        val scrollPane = ScrollPane(imagePane)

        borderPane.top = controlPane
        borderPane.center = scrollPane
//        borderPane.maxWidth = this.tabPane.maxWidth-300.0

        this.content = borderPane
    }

    fun centerImage() {
        val img = imagePane.image
        if (img != null) {
            val w: Double
            val h: Double

            val ratioX = imagePane.fitWidth / img.width
            val ratioY = imagePane.fitHeight / img.height

            val reduceCoeff = if (ratioX >= ratioY) {
                ratioY
            } else {
                ratioX
            }

            w = img.width * reduceCoeff
            h = img.height * reduceCoeff

            imagePane.x = (imagePane.fitWidth - w) / 2
            imagePane.y = (imagePane.fitHeight - h) / 2

        }
    }

    private fun initControls(): HBox {
        val contolPane = HBox()
        contolPane.minHeight = 24.0
        contolPane.prefHeight = 24.0
        contolPane.maxHeight = 24.0

        return contolPane
    }
}
