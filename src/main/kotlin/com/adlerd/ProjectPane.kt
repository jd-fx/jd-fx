package com.adlerd

import javafx.scene.control.Tab
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import java.io.File

class ProjectPane(projectPath: File): Tab() {

    val contentPane = HBox()
    val projectTree: TreeView<File> = TreeView()
    val codePane = CodePane()
    val folderImg: Image
    val genericFileImg: Image
    val javaFileImg: Image


    init {
        this.folderImg = Image(
            this::class.java.getResource("/img/package.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.genericFileImg = Image(
            this::class.java.getResource("/img/java_file.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.javaFileImg = Image(
            this::class.java.getResource("/img/java_file.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )

        this.text = projectName(projectPath)
        // Set up project's TreeView and its TreeCells
        projectTree.isEditable = false
        projectTree.root = createTree(projectPath)
        projectTree.setCellFactory {
            object : TreeCell<File>() {
                override fun updateItem(item: File?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item != null) {
                        text = item.name
                        graphic = treeItem.graphic
                    } else {
                        text = ""
                        graphic = null
                    }
                }
            }
        }

        projectTree.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY) {
                if (event.clickCount == 2) {
                    codePane.readFile(projectTree.selectionModel.selectedItem.value)
                }
            }
        }

        codePane.prefWidthProperty().bind(contentPane.prefWidthProperty())

        contentPane.isFillHeight = true
        contentPane.children.addAll(projectTree, codePane)

        this.content = contentPane
    }

    private fun projectName(path: File): String {
        val pathAsList = path.absolutePath.split('/')
        return pathAsList[pathAsList.size - 1]
    }

    private fun createTree(file: File): TreeItem<File> {
        val item = TreeItem(file)
        val childs = file.listFiles()
        if (childs != null) {
            for (child in childs) {
                item.children.add(createTree(child))
            }
            item.setGraphic(ImageView(folderImg))
        } else {
//            if (item.value.name.endsWith(suffix = ".java", ignoreCase = true)) {
//                item.setGraphic(ImageView(ProjectPane::class.java.getResource("img/java_file.png").toExternalForm()))
//            } else if (item.value.name.endsWith(suffix = ".class", ignoreCase = true)) {
//                item.setGraphic(ImageView(ProjectPane::class.java.getResource("img/java_file.png").toExternalForm()))
//            } else {
            item.setGraphic(ImageView(genericFileImg))
//                item.setGraphic(ImageView(ProjectPane::class.java.getResource("/img/generic_file.png").toExternalForm()))
//            }
        }
        return item
    }

    companion object {
        private const val ICON_SIZE = 14.0
    }
}