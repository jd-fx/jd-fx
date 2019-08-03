package com.adlerd.jdfx.gui.fx.tabs

import com.adlerd.jdfx.gui.fx.AppGUI
import com.adlerd.jdfx.util.Loader.loadImg
import com.adlerd.jdfx.util.Logger.infoln
import javafx.geometry.Insets
import javafx.scene.control.TabPane
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Paint
import java.io.File

/**
 * Pane which opens the directory, .zip or .jar passed into it -> @param projectLocation. Holds a FileTree of all project
 * files and a TabPane for each file in the file tree to open into.
 */
class ProjectTab(projectLocation: File) : CustomTab() {

    private val contentPane = BorderPane()
    private val projectTree = TreeView<File>()
    private val fileTabPane = TabPane()
    private val packageImg: Image
    private val genericFileImg: Image
    private val javaFileImg: Image
    private val classFileImg: Image
    private val imageFileImg: Image

    init {
        this.packageImg = loadImg("img/folder.png", ICON_SIZE)
        this.genericFileImg = loadImg("img/generic_file.png", ICON_SIZE)
        this.javaFileImg = loadImg("img/small_java_file.png", ICON_SIZE)
        this.classFileImg = loadImg("img/alt/file-class.png", ICON_SIZE)
        this.imageFileImg = loadImg("img/alt/file-image.png", ICON_SIZE)

        this.text = projectLocation.name

        // Make sure the project gets cleared from the open files list when closed
        this.setOnCloseRequest {
            AppGUI.openFiles.remove(this.text)
        }
        // Set up project's TreeView and its TreeCells
        projectTree.isEditable = false
        projectTree.root = createTree(projectLocation)
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

        // Allow for double click in tree pane to open file in a new tab
        projectTree.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY) {
                if (event.clickCount == 2) {
                    val selectedFile = projectTree.selectionModel.selectedItem.value
                    // Prevent folders from being opened in a new FileTab
                    when (selectedFile.isDirectory) {
                        // Change opening procedure based on file type
                        true -> {
                            when (selectedFile.extension.toLowerCase()) {
                                "png" -> infoln("Opening PNG")
                                "jpg" -> infoln("Opening JPG")
                            }
                        }
                        false -> fileTabPane.tabs.add(FileTab(selectedFile))
                    }
                }
            }
        }

        contentPane.left = projectTree
        contentPane.center = fileTabPane
        fileTabPane.background = Background(BackgroundFill(Paint.valueOf("white"), CornerRadii(5.0), Insets.EMPTY))

        this.content = contentPane
    }

    /**
     * Create a tree out of all the files in the hirearchy
     */
    private fun createTree(file: File): TreeItem<File> {
        val item = TreeItem(file)
        val children = file.listFiles()
        if (children != null) {
            for (child in children) {
                item.children.add(createTree(child))
            }
            item.graphic = ImageView(packageImg)
        } else {
            when {
                item.value.name.endsWith(suffix = ".java", ignoreCase = true) -> item.setGraphic(ImageView(javaFileImg))
                item.value.name.endsWith(
                    suffix = ".class",
                    ignoreCase = true
                ) -> item.setGraphic(ImageView(classFileImg))
                item.value.name.endsWith(suffix = ".png", ignoreCase = true) -> item.setGraphic(ImageView(imageFileImg))
                item.value.name.endsWith(suffix = ".jpg", ignoreCase = true) -> item.setGraphic(ImageView(imageFileImg))
                else -> item.setGraphic(ImageView(genericFileImg))
            }
        }
        return item
    }

    companion object {
        private const val ICON_SIZE = 16.0
    }
}
