package com.adlerd.gui

import com.adlerd.gui.panes.ProjectPane
import com.adlerd.gui.tabs.ProjectTab
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
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.fxmisc.cssfx.CSSFX
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.util.concurrent.TimeUnit
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

        // Check if project directory needs to be set up
        //TODO: Check props file for default workspace, otherwise have alert which asks on first run if you want the default "~/.jd_fx/" or pick a custom folder
        if (!projectWorkspace.exists()) {
            projectWorkspace.mkdirs()
            println("INFO: Project workspace HAS BEEN CREATED at \"${projectWorkspace.absolutePath}\"")
        } else {
            println("INFO: Project workspace ALREADY EXISTS at \"${projectWorkspace.absolutePath}\"")
        }

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
            selectFile(stage = window)
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
            selectFile(stage = window)
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
        wikiItem.setOnAction {
            try {
                Desktop.getDesktop().browse(URL("https://github.com/dadler64/jd-fx/wiki").toURI())
            } catch (e: IOException) {
                println("ERROR: Could not open link")
                e.printStackTrace()
            } catch (e: URISyntaxException) {
                println("ERROR: Bad URI")
                e.printStackTrace()
            }
        }
        prefItem.graphic = ImageView(Image(AppGUI::class.java.getResource("/img/preferences.png").toExternalForm(), MENU_ICON_SIZE, MENU_ICON_SIZE, false, true))
        prefItem.setOnAction {  }
        aboutItem.setOnAction {  }
        helpMenu.items.addAll(wikiItem, SeparatorMenuItem(), prefItem, aboutItem)


        bar.menus.addAll(fileMenu, editMenu, navMenu, searchMenu, helpMenu)

        // Initialize accelerators for the menu items
        initAccelerators()

        return bar
    }

    private fun addToRecents(projectName: String, projectDir: File) {
        this.recentsMenu.items.add(0, MenuItem(projectName))
        AppGUI.projectList[projectName] = projectDir
    }

    private fun initAccelerators() {
        val system = System.getProperty("os.name")
        println("Current System: $system")

        val controlKey = if (system.startsWith(prefix = "mac", ignoreCase = true)) KeyCombination.META_DOWN else KeyCombination.CONTROL_DOWN

        openFileItem.accelerator = KeyCodeCombination(KeyCode.O, controlKey)
        closeItem.accelerator = KeyCodeCombination(KeyCode.W, controlKey)
        saveItem.accelerator = KeyCodeCombination(KeyCode.S, controlKey)
        saveAllItem.accelerator = KeyCodeCombination(KeyCode.S, controlKey, KeyCombination.ALT_DOWN)
        exitItem.accelerator = KeyCodeCombination(KeyCode.Q, controlKey)
        copyItem.accelerator = KeyCodeCombination(KeyCode.C, controlKey)
        pasteItem.accelerator = KeyCodeCombination(KeyCode.V, controlKey)
        selectAllItem.accelerator = KeyCodeCombination(KeyCode.A, controlKey)
        findItem.accelerator = KeyCodeCombination(KeyCode.F, controlKey)
        openTypeItem.accelerator = KeyCodeCombination(KeyCode.T, controlKey, KeyCombination.SHIFT_DOWN)
        openTypeHierarchyItem.accelerator = KeyCodeCombination(KeyCode.F4)
        goToLineItem.accelerator = KeyCodeCombination(KeyCode.L, controlKey)
        searchItem.accelerator = KeyCodeCombination(KeyCode.F, controlKey)
        prefItem.accelerator = KeyCodeCombination(KeyCode.COMMA, controlKey)
        aboutItem.accelerator = KeyCodeCombination(KeyCode.F1)
    }

    /*
    private fun openDirectoryLocation(stage: Stage) {
        val project: File

        try {
            val directoryChooser = DirectoryChooser()
            directoryChooser.title = "Project Location"
            project = directoryChooser.showDialog(stage.owner)
            println("PROJECT_PATH: ${project.absolutePath}")
            projectPane.tabPane.tabs.add(ProjectTab(project))
        } catch (e: RuntimeException) {
            // Do nothing...
        } catch (ise: IllegalStateException) {

        }
    }
    */

    private fun selectFile(stage: Stage) {
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
            val jarFile = fileChooser.showOpenDialog(stage.owner)
            val projectName = jarFile.nameWithoutExtension
            val projectDir = File("$projectWorkspace/$projectName")
            // Check if the desired directory exists
            if (!projectDir.exists()) {
                projectDir.delete() // I'm lazy right now so we're deleting the directory
            }
                // Create project directory
                projectDir.mkdir()
                // Command to move to the project directory and unpack the Jar file there
                "jar xf ${jarFile.absolutePath}".runCommand(projectDir, 1)

                this.projectPane.tabPane.tabs.add(ProjectTab(projectDir))
                // Add project to recents list
                addToRecents(projectName, projectDir)
        } catch (e: RuntimeException) {
            println("ERROR: Could not open file or set up project!")
            e.printStackTrace()
        } catch (e: IllegalStateException) {

        }
    }

    private fun String.runCommand(workingDir: File, timeLimit: Long): String {
        return try {
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(timeLimit, TimeUnit.SECONDS)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
            "ERROR"
        }
    }

    companion object {
        private const val MENU_ICON_SIZE = 16.0
        private const val TIMER_LENGTH = 2L
        val projectList = HashMap<String, File>()
    }
}
