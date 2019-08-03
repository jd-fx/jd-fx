package com.adlerd.jdfx.gui.fx.tabs

import com.adlerd.jdfx.gui.fx.panes.SimpleCodePane
import com.adlerd.jdfx.util.Loader.loadImg
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.File

class FileTab(file: File) : CustomTab() {
    private var codePane = SimpleCodePane()
    lateinit var icon: Image

    init {
        if (file.isFile) {
            this.text = file.name
            icon = when (file.extension) {
                "java" -> loadImg("img/small_java_file.png", ICON_SIZE)
                "png" -> loadImg("img/alt/file-image.png", ICON_SIZE)
                "jpg" -> loadImg("img/alt/file-image.png", ICON_SIZE)
                "calss" -> loadImg("img/file-class.png", ICON_SIZE)
                else -> loadImg("img/small_generic_file.png", ICON_SIZE)
            }
//             Ensure CodePane is clear
            codePane.clearText()
//             Read file into CodePane
            codePane.readFile(file)
            this.graphic = ImageView(icon)
            this.content = codePane
            // Show the beginning of the file when opening
            codePane.scrollTo(SimpleCodePane.START)
        }
    }
}
