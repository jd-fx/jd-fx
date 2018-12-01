package com.adlerd

import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import java.io.File

class FileTab(file: File) : Tab() {
    val scrollPane = ScrollPane()
    val codePane = CodePane()

    init {
        if (file.isFile) {
            this.text = file.name
            codePane.prefWidthProperty().bind(scrollPane.widthProperty())
            codePane.prefHeightProperty().bind(scrollPane.heightProperty())
            scrollPane.content = codePane
            this.content = scrollPane

            codePane.clearText()
            codePane.readFile(file)
        }
    }
}