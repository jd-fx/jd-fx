package com.adlerd

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import kotlin.system.exitProcess

class AppGUI : Application() {

    lateinit var window: Stage

    val root= VBox()

    val fileMenu = Menu("File")
    val openFileItem = MenuItem("Open File...")
    val closeItem = MenuItem("Close")
    val saveItem = MenuItem("Save")
    val saveAllItem = MenuItem("Save All Sources")
    val recentsMenu = Menu("Recent Files")
    val exitItem = MenuItem("Exit")
    // Edit menu
    val editMenu = Menu("Edit")
    val copyItem = MenuItem("Copy")
    val pasteItem = MenuItem("Paste Log")
    val selectAllItem = MenuItem("Select all")
    val findItem = MenuItem("Find...")
    // Navigation menu
    val navMenu = Menu("Navigation")
    val openTypeItem = MenuItem("Open Type...")
    val openTypeHierarchyItem = MenuItem("Open Type Hierarchy...")
    val goToLineItem = MenuItem("Go to Line...")
    // Search menu
    val searchMenu = Menu("Search")
    val searchItem = MenuItem("Search...")
    // Help menu
    val helpMenu = Menu("Help")
    val wikiItem = MenuItem("Wikipedia")
    val prefItem = MenuItem("Preferences...")
    val aboutItem = MenuItem("About...")

    val bodyPane = BorderPane()
    val projectTabsPane = ProjectTabPane()


    lateinit var toolBar: HBox
    lateinit var bottomBar: HBox

    lateinit var menuBar: MenuBar

    override fun start(stage: Stage) {

        window = stage

        toolBar = initButtonBar()
        bottomBar = initBottomBar()

        bodyPane.top = toolBar
        bodyPane.center = projectTabsPane
        bodyPane.bottom = bottomBar
        bodyPane.prefWidthProperty().bind(root.widthProperty())
        bodyPane.prefHeightProperty().bind(root.heightProperty())

        menuBar = initMenuBar()

        root.children.addAll(menuBar, bodyPane)

        val scene = Scene(root, 600.0, 400.0)
        scene.stylesheets.add(AppGUI::class.java.getResource("/style.css").toExternalForm())
        window.scene = scene
        window.title = "Java Decompiler"
        window.icons.add(Image(AppGUI::class.java.getResource("/img/folder.png").toExternalForm()))
        window.show()
    }

    private fun initButtonBar(): HBox {
        val buttonBar = HBox()
        buttonBar.minHeight = 24.0
        buttonBar.spacing = 2.0
        buttonBar.padding = Insets(2.0)

        val vertSeparator1 = Separator()
        vertSeparator1.orientation = Orientation.VERTICAL

        val vertSeparator2 = Separator()
        vertSeparator2.orientation = Orientation.VERTICAL

        val openButton = Button()
        openButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        openButton.setOnAction {
            openProjectLocation(stage = window)
        }
        val openTypeButton = Button()
        openTypeButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        val searchButton = Button()
        searchButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        val backButton = Button()
        backButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        val forwardButton = Button()
        forwardButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )

        buttonBar.children.addAll(
            openButton,
            vertSeparator1,
            openTypeButton,
            searchButton,
            vertSeparator2,
            backButton,
            forwardButton
        )
        return buttonBar
    }

    private fun initBottomBar(): HBox {
        val bottomBar = HBox()
        bottomBar.minHeight = 24.0
        bottomBar.spacing = 2.0
        bottomBar.padding = Insets(2.0)

        val findLabel = Label("Find:")
        findLabel.alignment = Pos.CENTER
        val findTypeBox = ComboBox<String>()
        findTypeBox.isEditable = true
        val nextButton = Button("Next")
        nextButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        val prevButton = Button("Previous")
        prevButton.graphic = ImageView(
            Image(
                AppGUI::class.java.getResource("/img/folder.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        val caseCheckBox = CheckBox("Case sensitive")

        bottomBar.children.addAll(findLabel, findTypeBox, nextButton, prevButton, caseCheckBox)
        return bottomBar
    }

    private fun initMenuBar(): MenuBar {
        val bar = MenuBar()

        bar.prefWidthProperty().bind(root.widthProperty())
        bar.useSystemMenuBarProperty()

        // File menu
        openFileItem.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
        openFileItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        openFileItem.setOnAction {
            openProjectLocation(stage = window)
        }
        closeItem.accelerator = KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
        closeItem.setOnAction {  }
        saveItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)
        saveItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        saveItem.setOnAction {  }
        saveAllItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN)
        saveAllItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        saveAllItem.setOnAction {  }
        exitItem.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
        exitItem.setOnAction {
            exitProcess(1)
        }
        fileMenu.items.addAll(openFileItem, SeparatorMenuItem(), closeItem, SeparatorMenuItem(), saveItem, saveAllItem, SeparatorMenuItem(), recentsMenu, SeparatorMenuItem(), exitItem)

        // Edit menu
        copyItem.accelerator = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)
        copyItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        copyItem.setOnAction {  }
        pasteItem.accelerator = KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)
        pasteItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        pasteItem.setOnAction {  }
        selectAllItem.accelerator = KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)
        selectAllItem.setOnAction {  }
        findItem.accelerator = KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN)
        findItem.setOnAction {  }
        editMenu.items.addAll(copyItem, pasteItem, SeparatorMenuItem(), selectAllItem, SeparatorMenuItem(), findItem)

        // Navigation menu
        openTypeItem.accelerator = KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
        openTypeItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        openTypeItem.setOnAction {  }
        openTypeHierarchyItem.accelerator = KeyCodeCombination(KeyCode.F4)
        openTypeHierarchyItem.setOnAction {  }
        goToLineItem.accelerator = KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN)
        goToLineItem.setOnAction {  }
        navMenu.items.addAll(openTypeItem, openTypeHierarchyItem, goToLineItem)

        // Search menu
        searchItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
        searchItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        searchItem.setOnAction {  }
        searchMenu.items.addAll(searchItem)

        // Help menu
        wikiItem.setOnAction {  }
        prefItem.accelerator = KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
        prefItem.graphic = ImageView(Image(this::class.java.getResource("/img/folder.png").toExternalForm()))
        prefItem.setOnAction {  }
        aboutItem.accelerator = KeyCodeCombination(KeyCode.F1)
        aboutItem.setOnAction {  }
        helpMenu.items.addAll(wikiItem, SeparatorMenuItem(), prefItem, aboutItem)


        bar.menus.addAll(fileMenu, editMenu, navMenu, searchMenu, helpMenu)
        return bar
    }

    fun openProjectLocation(stage: Stage) {
        val project: File

        try {
            val directoryChooser = DirectoryChooser()
            directoryChooser.title = "Project Location"
            project = directoryChooser.showDialog(stage.owner)
            println("PROJECT_PATH: ${project.absolutePath}")
            projectTabsPane.tabPane.tabs.add(ProjectPane(project))
        } catch (e: RuntimeException) {
            // Do nothing...
        }
    }

    companion object {
        private const val ICON_SIZE = 14.0
    }
}
