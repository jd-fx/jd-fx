package com.adlerd

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URLConnection

/**
 * Pane which opens the directory (planned to eventually be a compressed file like .zip or .jar) passed into
 * it -> @param projectPath. Holds a FileTree of all project files and a TabPane for each file in the file
 * tree to open into.
 */
class ProjectPane(projectPath: File): Tab() {

    //    private val contentPane = HBox()
    private val contentPane = SplitPane()
    private val projectTree = TreeView<File>()
    private val fileTabPane = TabPane()
    private val packageImg: Image
    private val genericFileImg: Image
    private val javaFileImg: Image
    private val classFileImg: Image

    private val File.fileType: String
        get() {
            var mimeType = ""
            val stream = BufferedInputStream(ByteArrayInputStream(this.readBytes()))
            // Test for mime type
            try {
                mimeType = URLConnection.guessContentTypeFromStream(stream)
                stream.close()
            } catch (e: IllegalStateException) {

            } finally {
                stream.close()
            }
            return mimeType
        }

    init {
        this.packageImg = Image(
            javaClass.classLoader.getResource("img/small_folder.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.genericFileImg = Image(
            javaClass.classLoader.getResource("img/small_generic_file.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.javaFileImg = Image(
            javaClass.classLoader.getResource("img/small_java_file.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )
        this.classFileImg = Image(
            javaClass.classLoader.getResource("img/small_class_file.png").toExternalForm(),
            ICON_SIZE,
            ICON_SIZE,
            true,
            false
        )

        this.text = projectPath.name
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

        // Allow for double click in tree pane to open file in a new tab
        projectTree.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY) {
                if (event.clickCount == 2) {
                    val selectedFile = projectTree.selectionModel.selectedItem.value

                    if (selectedFile.isFile) {
                        // Get the mime value before the '/'
                        //     (Ex. text/plain -> return text)
                        when (selectedFile.fileType.split("/")[0]) {
                            "text" -> fileTabPane.tabs.add(FileTab(selectedFile))
                            "image" -> fileTabPane.tabs.add(ImageTab(selectedFile))

                            else -> println("ERROR! Cannot open $selectedFile...")
                        }
                    }
                }
            }
        }

        contentPane.orientation = Orientation.HORIZONTAL
        contentPane.setDividerPosition(1, 150.0)
        contentPane.items.addAll(projectTree, fileTabPane)

        HBox.setHgrow(projectTree, Priority.NEVER)
        HBox.setHgrow(fileTabPane, Priority.ALWAYS)

        fileTabPane.background = Background(BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY))

        this.content = contentPane
    }

    private fun createTree(file: File): TreeItem<File> {
        val item = TreeItem(file)
        val childs = file.listFiles()
        if (childs != null) {
            for (child in childs) {
                item.children.add(createTree(child))
            }
            item.setGraphic(ImageView(packageImg))
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
        private const val ICON_SIZE = 16.0
    }
}
