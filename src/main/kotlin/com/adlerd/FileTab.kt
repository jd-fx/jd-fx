package com.adlerd

import javafx.scene.control.Tab
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.fxmisc.flowless.VirtualizedScrollPane
import java.io.File

class FileTab(file: File) : Tab() {
    val codePane = CodePane()
    lateinit var icon: Image

    init {
        if (file.isFile) {
            this.text = file.name
            icon = when (file.extension) {
                "java" -> Image(
                    this::class.java.getResource("/img/java_file.png").toExternalForm(),
                    ICON_SIZE,
                    ICON_SIZE,
                    true,
                    false
                )
                else -> Image(
                    this::class.java.getResource("/img/folder.png").toExternalForm(),
                    ICON_SIZE,
                    ICON_SIZE,
                    true,
                    false
                )
            }

            this.graphic = ImageView(icon)
            val scrollPane: VirtualizedScrollPane<CodePane> = VirtualizedScrollPane(codePane)

            this.content = scrollPane

            codePane.clearText()
            codePane.readFile(file)
        }
    }

    companion object {
        private const val ICON_SIZE = 14.0
    }
}
