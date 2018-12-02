package com.adlerd

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class ProjectTabPane : StackPane() {

    var tabPane: TabPane
    var helpText: VBox

    init {
        tabPane = initTabPane()
        helpText = initText()

        this.children.addAll(helpText, tabPane)
    }

    private fun initText(): VBox {
        val vBox = VBox()

        val noOpenLabel = Label("No files are open...")
        noOpenLabel.font = Font.font("Veranda", FontWeight.BOLD, 24.0)
        noOpenLabel.alignment = Pos.TOP_LEFT

        val instructionsLabel = Label(
            "Open a file with menu \"File > OpenFile...\"\n" +
                    "Open recent files with menu \"File > Recent Files\"\n" +
                    "Drag and drop files from your file manager"
        )
        instructionsLabel.alignment = Pos.CENTER_LEFT

        vBox.alignment = Pos.CENTER
        vBox.setMinSize(VBOX_WIDTH, VBOX_HEIGHT)
        vBox.setPrefSize(VBOX_WIDTH, VBOX_HEIGHT)
        vBox.setMaxSize(VBOX_WIDTH, VBOX_HEIGHT)
        vBox.children.addAll(noOpenLabel, instructionsLabel)
        VBox.setVgrow(noOpenLabel, Priority.NEVER)
        VBox.setVgrow(instructionsLabel, Priority.NEVER)

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
                    tp.tabs.add(ProjectPane(file))
            }
        }

        return tp
    }

    companion object {
        private const val VBOX_WIDTH = 300.0
        private const val VBOX_HEIGHT = 100.0
    }
}
