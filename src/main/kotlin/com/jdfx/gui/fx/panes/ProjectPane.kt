package com.jdfx.gui.fx.panes

import com.jdfx.gui.fx.tabs.CustomTab
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class ProjectPane : StackPane() {

    var tabPane: TabPane = TabPane()
    var selectedTab: CustomTab? = null
        get() = this.tabPane.selectionModel.selectedItem as CustomTab
        private set
    private var helpText: VBox = VBox()

    init {

        val noOpenLabel = Label("No files are open...")
        noOpenLabel.font = Font.font("Veranda", FontWeight.BOLD, 24.0)
        noOpenLabel.alignment = Pos.CENTER_LEFT

        val instructionsLabel = Label(
            "Open a file with menu \"File > OpenFile...\"\n" +
                    "Open recent files with menu \"File > Recent Files\"\n" +
                    "Drag and drop files from your file manager"
        )
        instructionsLabel.alignment = Pos.CENTER_LEFT

        VBox.setVgrow(noOpenLabel, Priority.SOMETIMES)
        VBox.setVgrow(instructionsLabel, Priority.SOMETIMES)

        helpText.setPrefSize(
            VBOX_WIDTH,
            VBOX_HEIGHT
        )
        helpText.alignment = Pos.CENTER
        helpText.children.addAll(noOpenLabel, instructionsLabel)
        helpText.isDisable = true

        this.children.addAll(helpText, tabPane)
    }

    companion object {
        private const val VBOX_WIDTH = 250.0
        private const val VBOX_HEIGHT = 100.0
    }
}
