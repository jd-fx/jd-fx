package com.adlerd

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Orientation
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
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.fxmisc.cssfx.CSSFX
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
        CSSFX.start(window)

        toolBar = initButtonBar()
        bottomBar = initBottomBar()

        bodyPane.top = toolBar
        bodyPane.center = projectTabsPane
        bodyPane.bottom = bottomBar
        bodyPane.prefWidthProperty().bind(root.widthProperty())
        bodyPane.prefHeightProperty().bind(root.heightProperty())

        menuBar = initMenuBar()

        root.children.addAll(menuBar, bodyPane)

        val scene = Scene(root, 700.0, 400.0)
        scene.stylesheets.add(AppGUI::class.java.getResource("/style.css").toExternalForm())
        window.scene = scene
        window.title = "Java Decompiler"
        window.icons.add(Image(AppGUI::class.java.getResource("/img/jd_icon_128.png").toExternalForm()))
        window.show()
    }

    /**
     *
     */
    private fun initButtonBar(): HBox {
        val buttonBar = HBox()
        buttonBar.minHeight = 24.0
        buttonBar.prefHeight = 24.0
        buttonBar.maxHeight = 24.0
        buttonBar.spacing = 2.0
        buttonBar.padding = Insets(2.0)

        val vertSeparator1 = Separator()
        vertSeparator1.orientation = Orientation.VERTICAL

        val vertSeparator2 = Separator()
        vertSeparator2.orientation = Orientation.VERTICAL

        val openButton = Button()
        openButton.id = "openBtn"
        openButton.setOnAction {
            openDirectoryLocation(stage = window)
        }
        val openTypeButton = Button()
        openTypeButton.id = "openTypeBtn"
        val searchButton = Button()
        searchButton.id = "searchBtn"
        val backButton = Button()
        backButton.id = "backBtn"
        val forwardButton = Button()
        forwardButton.id = "forwardBtn"

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

    /**
     *
     */
    private fun initBottomBar(): HBox {
        val bottomBar = HBox()
        bottomBar.minHeight = 24.0
        bottomBar.spacing = 5.0
        bottomBar.padding = Insets(2.0)

        val findLabel = Label("Find:")
        findLabel.id = "findLabel"
        val findTypeBox = ComboBox<String>()
        findTypeBox.items.addAll("id", "name", "text")
        findTypeBox.value = findTypeBox.items[0]
        findTypeBox.id = "findBox"
        findTypeBox.isEditable = true
        val nextButton = Button()
        nextButton.id = "nextBtn"
        nextButton.tooltip = Tooltip("Next")
        val prevButton = Button()
        prevButton.id = "prevBtn"
        prevButton.tooltip = Tooltip("Previous")
        val caseCheckBox = CheckBox("Case sensitive")
        caseCheckBox.id = "caseCheckBox"

        bottomBar.children.addAll(findLabel, findTypeBox, nextButton, prevButton, caseCheckBox)
        return bottomBar
    }

    /**
     *
     */
    private fun initMenuBar(): MenuBar {
        val bar = MenuBar()

        bar.prefWidthProperty().bind(root.widthProperty())
        bar.useSystemMenuBarProperty()

        // File menu
        openFileItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/open.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        openFileItem.setOnAction {
            //            openFileLocation(stage = window)
            openDirectoryLocation(stage = window)
        }
        closeItem.setOnAction {  }
        saveItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/save.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        saveItem.setOnAction {  }
        saveAllItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/save_all.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        saveAllItem.setOnAction {  }
        exitItem.setOnAction {
            exitProcess(1)
        }
        fileMenu.items.addAll(openFileItem, SeparatorMenuItem(), closeItem, SeparatorMenuItem(), saveItem, saveAllItem, SeparatorMenuItem(), recentsMenu, SeparatorMenuItem(), exitItem)

        // Edit menu
        copyItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/copy.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        copyItem.setOnAction {  }
        pasteItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/paste.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        pasteItem.setOnAction {  }
        selectAllItem.setOnAction {  }
        findItem.setOnAction {  }
        editMenu.items.addAll(copyItem, pasteItem, SeparatorMenuItem(), selectAllItem, SeparatorMenuItem(), findItem)

        // Navigation menu
        openTypeItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/open_type.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        openTypeItem.setOnAction {  }
        openTypeHierarchyItem.setOnAction {  }
        goToLineItem.setOnAction {  }
        navMenu.items.addAll(openTypeItem, openTypeHierarchyItem, goToLineItem)

        // Search menu
        searchItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/search_src.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        searchItem.setOnAction {  }
        searchMenu.items.addAll(searchItem)

        // Help menu
        wikiItem.setOnAction {  }
        prefItem.graphic = ImageView(
            Image(
                this::class.java.getResource("/img/preferences.png").toExternalForm(),
                ICON_SIZE,
                ICON_SIZE,
                true,
                false
            )
        )
        prefItem.setOnAction {  }
        aboutItem.setOnAction {  }
        helpMenu.items.addAll(wikiItem, SeparatorMenuItem(), prefItem, aboutItem)


        bar.menus.addAll(fileMenu, editMenu, navMenu, searchMenu, helpMenu)

        // Initialize accelerators for the menu items
        initAccelerators()

        return bar
    }

    fun initAccelerators() {
        val system = System.getProperty("os.name")

        if (system.startsWith(prefix = "mac", ignoreCase = true)) {
            openFileItem.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.META_DOWN)
            closeItem.accelerator = KeyCodeCombination(KeyCode.W, KeyCombination.META_DOWN)
            saveItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN)
            saveAllItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN, KeyCombination.ALT_DOWN)
            exitItem.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN)
            copyItem.accelerator = KeyCodeCombination(KeyCode.C, KeyCombination.META_DOWN)
            pasteItem.accelerator = KeyCodeCombination(KeyCode.V, KeyCombination.META_DOWN)
            selectAllItem.accelerator = KeyCodeCombination(KeyCode.A, KeyCombination.META_DOWN)
            findItem.accelerator = KeyCodeCombination(KeyCode.F, KeyCombination.META_DOWN)
            openTypeItem.accelerator =
                    KeyCodeCombination(KeyCode.T, KeyCombination.META_DOWN, KeyCombination.SHIFT_DOWN)
            openTypeHierarchyItem.accelerator = KeyCodeCombination(KeyCode.F4)
            goToLineItem.accelerator = KeyCodeCombination(KeyCode.L, KeyCombination.META_DOWN)
            searchItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN, KeyCombination.SHIFT_DOWN)
            prefItem.accelerator = KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN)
            aboutItem.accelerator = KeyCodeCombination(KeyCode.F1)
        } else {
            openFileItem.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
            closeItem.accelerator = KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
            saveItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)
            saveAllItem.accelerator =
                    KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN)
            exitItem.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
            copyItem.accelerator = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)
            pasteItem.accelerator = KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)
            selectAllItem.accelerator = KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)
            findItem.accelerator = KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN)
            openTypeItem.accelerator =
                    KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
            openTypeHierarchyItem.accelerator = KeyCodeCombination(KeyCode.F4)
            goToLineItem.accelerator = KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN)
            searchItem.accelerator =
                    KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
            prefItem.accelerator = KeyCodeCombination(KeyCode.COMMA, KeyCombination.CONTROL_DOWN)
            aboutItem.accelerator = KeyCodeCombination(KeyCode.F1)
        }
    }

    fun openDirectoryLocation(stage: Stage) {
        val project: File

        try {
            val directoryChooser = DirectoryChooser()
            directoryChooser.title = "Project Location"
            project = directoryChooser.showDialog(stage.owner)
            println("PROJECT_PATH: ${project.absolutePath}")
            projectTabsPane.tabPane.tabs.add(ProjectPane(project))
        } catch (e: RuntimeException) {
            println("ERROR!")
            e.printStackTrace()
            // Do nothing...
        }
    }

    fun openFileLocation(stage: Stage) {
        val project: File

        try {
            val fileChooser = FileChooser()
            fileChooser.title = "Project Location"
            fileChooser.initialDirectory = File(System.getProperty("user.home"))
            fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter(
                    "Java Binaries",
                    "*.class",
                    "*.ear",
                    "*.jar",
                    "*.java",
                    "*.log",
                    "*.war",
                    "*.zip"
                ),
                FileChooser.ExtensionFilter("All Files", "*.*")
            )
            project = fileChooser.showOpenDialog(stage.owner)
            println("PROJECT_PATH: ${project.absolutePath}")
            projectTabsPane.tabPane.tabs.add(ProjectPane(project))
        } catch (e: RuntimeException) {

        }
    }

    companion object {
        private const val ICON_SIZE = 16.0
    }
}
