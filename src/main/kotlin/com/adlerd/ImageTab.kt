package com.adlerd

import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File

class ImageTab(imageFile: File) : Tab() {

    val borderPane = BorderPane()
    val controlPane: HBox
    val imagePane: ImageView
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

    private fun initControls(): HBox {
        val contolPane = HBox()
        contolPane.minHeight = 24.0
        contolPane.prefHeight = 24.0
        contolPane.maxHeight = 24.0

        return contolPane
    }
}
