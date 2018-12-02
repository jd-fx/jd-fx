package com.adlerd

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import java.io.File

class ProjectPane(projectPath: File): Tab() {

    val contentPane = HBox()
    val projectTree: TreeView<File> = TreeView()
    //    val codePane = CodePane()
    val fileTabPane = TabPane()
    val folderImg: Image
    val genericFileImg: Image
    val javaFileImg: Image
    val classFileImg: Image


    init {
        this.folderImg = Image(
            this::class.java.getResource("/img/package.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.genericFileImg = Image(
            this::class.java.getResource("/img/generic_file.png").toExternalForm(),
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
        this.classFileImg = Image(
            this::class.java.getResource("/img/class_file.png").toExternalForm(),
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
                    fileTabPane.tabs.add(FileTab(projectTree.selectionModel.selectedItem.value))
                }
            }
        }

        contentPane.spacing = 0.0
        contentPane.isFillHeight = true
        contentPane.children.addAll(projectTree, fileTabPane)

        HBox.setHgrow(projectTree, Priority.NEVER)
        HBox.setHgrow(fileTabPane, Priority.ALWAYS)

        fileTabPane.background = Background(BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY))

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
            if (item.value.name.endsWith(suffix = ".java", ignoreCase = true)) {
                item.setGraphic(ImageView(javaFileImg))
            } else if (item.value.name.endsWith(suffix = ".class", ignoreCase = true)) {
                item.setGraphic(ImageView(classFileImg))
            } else {
                item.setGraphic(ImageView(genericFileImg))
            }
        }
        return item
    }

    companion object {
        private const val ICON_SIZE = 14.0
    }
}