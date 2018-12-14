package com.adlerd

import com.adlerd.gui.AppGUI
import javafx.application.Application

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(AppGUI::class.java)
    }
}

