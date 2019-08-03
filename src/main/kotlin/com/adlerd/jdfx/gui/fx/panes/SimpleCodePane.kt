package com.adlerd.jdfx.gui.fx.panes

import javafx.geometry.Point2D
import javafx.scene.layout.BorderPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import java.io.File
import java.nio.charset.Charset

open class SimpleCodePane : BorderPane() {

    val codeArea = CodeArea()

    init {
        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        codeArea.isEditable = false
//        codeArea.beingUpdatedProperty().addListener { value, oldValue, newValue ->
//            infoln("Being Updated ($value, $oldValue, $newValue)")
//            infoln("Updating: $value")
//        }

        this.center = VirtualizedScrollPane(codeArea)
    }

    fun clearText() {
        this.codeArea.clear()
    }

    fun readFile(file: File) {
        clearText()
        if (file.isFile && file.canRead()) {
            this.codeArea.replaceText(0, 0, file.readText(Charset.defaultCharset()))
        }
    }

    fun scrollTo(xPos: Double, yPos: Double) {
        this.codeArea.scrollToPixel(xPos, yPos)
    }

    fun scrollTo(xPos: Int, yPos: Int) {
        this.codeArea.scrollToPixel(xPos.toDouble(), yPos.toDouble())
    }

    fun scrollTo(point: Point2D) {
        this.codeArea.scrollToPixel(point)
    }

    // TODO: Implement search for word/phrase in the code areas/projects
    fun findString(string: String) {

    }

    companion object {
        val START = Point2D(0.0, 0.0)
    }
}
