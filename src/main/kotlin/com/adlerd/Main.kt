package com.adlerd

import javafx.application.Application

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        if (System.getProperty("os.name").startsWith(prefix = "Mac", ignoreCase = true)) {
            // AquaFx is broken :(
//            AquaFx.style()
        }
        Application.launch(AppGUI::class.java)
    }
}

