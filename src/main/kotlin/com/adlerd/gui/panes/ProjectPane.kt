package com.adlerd.gui.panes

import com.adlerd.gui.tabs.CustomTab
import com.adlerd.gui.tabs.ProjectTab
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class ProjectPane : StackPane() {

    var tabPane: TabPane
    var helpText: VBox

    init {
        this.tabPane = initTabPane()
        this.helpText = initText()
        this.helpText.isDisable = true

        this.children.addAll(helpText, tabPane)
    }

    private fun initText(): VBox {
        val vBox = VBox()

        val noOpenLabel = Label("No files are open...")
        noOpenLabel.font = Font.font("Veranda", FontWeight.BOLD, 24.0)
        noOpenLabel.alignment = Pos.CENTER_LEFT

        val instructionsLabel = Label(
            "Open a file with menu \"File > OpenFile...\"\n" +
                    "Open recent files with menu \"File > Recent Files\"\n" +
                    "Drag and drop files from your file manager"
        )
        instructionsLabel.alignment = Pos.CENTER_LEFT

        vBox.alignment = Pos.CENTER
        vBox.children.addAll(noOpenLabel, instructionsLabel)
        VBox.setVgrow(noOpenLabel, Priority.SOMETIMES)
        VBox.setVgrow(instructionsLabel, Priority.SOMETIMES)

        return vBox
    }

    private fun initTabPane(): TabPane {
        val tp = TabPane()

        tp.setOnDragOver { event ->
            if (event.gestureSource != tp && event.dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            }
            event.consume()
        }

        tp.setOnDragDropped { event ->
            val dragBoard = event.dragboard

            if (dragBoard.hasFiles()) {
                for (file in dragBoard.files)
                    tp.tabs.add(ProjectTab(file))
            }
        }

        return tp
    }


    fun selectedTab(): CustomTab {
        return this.tabPane.selectionModel.selectedItem as CustomTab
    }

    companion object {
        private const val VBOX_WIDTH = 250.0
        private const val VBOX_HEIGHT = 100.0
    }
}
