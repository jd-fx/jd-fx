package com.adlerd

import javafx.application.Application
import org.fxmisc.cssfx.CSSFX

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        CSSFX.start()
        if (System.getProperty("os.name").startsWith(prefix = "Mac", ignoreCase = true)) {
            // AquaFx is broken :(
//            AquaFx.style()
        }
        Application.launch(AppGUI::class.java)
    }
}

