package com.adlerd

import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory

class CodePane: CodeArea() {
    private var fileSavedExternal = false

    init {
        this.paragraphGraphicFactory = LineNumberFactory.get(this)

        this.beingUpdatedProperty().addListener { value, oldValue, NewValue ->
            println("INFO: Being Updated ($value)")
        }
    }
}
