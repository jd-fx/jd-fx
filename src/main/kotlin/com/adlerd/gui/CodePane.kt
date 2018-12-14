package com.adlerd.gui

import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import java.io.File
import java.nio.charset.Charset

class CodePane: CodeArea() {
    private var fileSavedExternal = false

    init {
        this.paragraphGraphicFactory = LineNumberFactory.get(this)

        this.beingUpdatedProperty().addListener { value, oldValue, NewValue ->
            println("INFO: Being Updated ($value)")
        }
    }

    fun clearText() {
        this.clear()
    }

    fun readFile(file: File) {
        clearText()
        if (file.isFile && file.canRead()) {
            this.appendText(file.readText(Charset.defaultCharset()))
        }
    }
}
