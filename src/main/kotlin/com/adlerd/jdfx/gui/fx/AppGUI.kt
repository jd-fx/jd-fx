package com.adlerd.jdfx.gui.fx

import com.adlerd.jdfx.JDFX
import com.adlerd.jdfx.gui.fx.panes.ProjectPane
import com.adlerd.jdfx.gui.fx.tabs.ProjectTab
import com.adlerd.jdfx.util.Decompiler
import com.adlerd.jdfx.util.Loader.loadImg
import com.adlerd.jdfx.util.Loader.loadRes
import com.adlerd.jdfx.util.Logger.debugln
import com.adlerd.jdfx.util.Logger.errorln
import com.adlerd.jdfx.util.Logger.infoln
import com.adlerd.jdfx.util.Logger.warningln
import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.TransferMode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import org.fxmisc.cssfx.CSSFX
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import kotlin.system.exitProcess


class AppGUI : Application() {

    private lateinit var window: Stage

    private val root = VBox()

    private val fileMenu = Menu("File")
    private val openFileItem = MenuItem("Open File...")
    private val closeProject = MenuItem("Close Project")
    private val saveItem = MenuItem("Save")
    private val saveAllItem = MenuItem("Save All Sources")
    private val recentsMenu = Menu("Recent Files")
    private val exitItem = MenuItem("Exit")
    // Edit menu
    private val editMenu = Menu("Edit")
    private val copyItem = MenuItem("Copy")
    private val pasteItem = MenuItem("Paste Log")
    private val selectAllItem = MenuItem("Select all")
    private val findItem = MenuItem("Find...")
    // Navigation menu
    private val navMenu = Menu("Navigation")
    private val openTypeItem = MenuItem("Open Type...")
    private val openTypeHierarchyItem = MenuItem("Open Type Hierarchy...")
    private val goToLineItem = MenuItem("Go to Line...")
    // Search menu
    private val searchMenu = Menu("Search")
    private val searchItem = MenuItem("Search...")
    // Help menu
    private val helpMenu = Menu("Help")
    private val wikiItem = MenuItem("Wiki")
    private val prefItem = MenuItem("Preferences...")
    private val aboutItem = MenuItem("About...")

    private val bodyPane = BorderPane()
    private val projectPane = ProjectPane()


    private lateinit var toolBar: HBox
    private lateinit var bottomBar: HBox
    private lateinit var menuBar: MenuBar

    override fun start(stage: Stage) {

        window = stage

        toolBar = initButtonBar()
        bottomBar = initBottomBar()

        bodyPane.top = toolBar
        bodyPane.center = projectPane
//        bodyPane.bottom = bottomBar
        bodyPane.prefWidthProperty().bind(root.widthProperty())
        bodyPane.prefHeightProperty().bind(root.heightProperty())

        menuBar = initMenuBar()

        root.children.addAll(menuBar, bodyPane)

        val scene = Scene(root, 800.0, 500.0)
        CSSFX.start()
        scene.stylesheets.add(loadRes("style.css"))
        menuBar.isUseSystemMenuBar = true
        window.scene = scene
        window.title = "Java Decompiler"
        window.icons.add(loadImg("img/jd_icon_128.png"))

        // Show window
        window.show()

        // Allow files and folders to be dropped onto the project pane
        projectPane.tabPane.setOnDragOver { event ->
            if (event.gestureSource !== projectPane.tabPane && event.dragboard.hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
            }
            event.consume()
        }

        projectPane.tabPane.setOnDragDropped { event ->
            var droppedFilePath = ""
            val db = event.dragboard
            var success = false
            if (db.hasFiles()) {
                droppedFilePath = db.files[0].path
                success = true
            }
            /* let the source know whether the string was successfully
                 * transferred and used */
            event.isDropCompleted = success
            event.consume()

            decompileFile(File(droppedFilePath))
        }
    }

    /**
     *  Initialize the Button Bar HBox for this application. All action events, looks, and images for this
     *  program's button bar items are initialized here.
     *  @return the fully initialized Button Bar HBox
     */
    private fun initButtonBar(): HBox {
        val buttonBar = HBox()
        buttonBar.id = "buttonBar"
        buttonBar.minHeight = 30.0
        buttonBar.spacing = 2.0
        buttonBar.padding = Insets(2.0)

        val vertSeparator1 = Separator()
        vertSeparator1.orientation = Orientation.VERTICAL

        val vertSeparator2 = Separator()
        vertSeparator2.orientation = Orientation.VERTICAL

        val openButton = Button()
        openButton.id = "openBtn"
        openButton.setOnAction {
            val file = selectFile(stage = window)
            decompileFile(file)
        }
        val openTypeButton = Button()
        openTypeButton.isDisable = true
        openTypeButton.id = "openTypeBtn"
        val searchButton = Button()
        searchButton.isDisable = true
        searchButton.id = "searchBtn"
        val backButton = Button()
        backButton.isDisable = true
        backButton.id = "backBtn"
        val forwardButton = Button()
        forwardButton.isDisable = true
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
     *  Initialize the bottom bar HBox for this application. All action events, looks, and images for this
     *  program's bottom bar items are initialized here.
     *  @return the fully initialized Bottom Bar HBox
     */
    private fun initBottomBar(): HBox {
        val bottomBar = HBox()
        bottomBar.id = "bottomBar"
        bottomBar.minHeight = 30.0
        bottomBar.spacing = 5.0
        bottomBar.padding = Insets(2.0)

        val findLabel = Label("Find:")
        findLabel.id = "findLabel"
        val findTypeBox = ComboBox<String>()
        findTypeBox.items.addAll("id", "title", "text")
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
     *  Initialize the MenuBar for this application. All action events, looks, and images for this
     *  program's menu bar items are initialized here.
     *  @return the fully initialized MenuBar
     */
    private fun initMenuBar(): MenuBar {
        val bar = MenuBar()

        bar.prefWidthProperty().bind(root.widthProperty())
        bar.useSystemMenuBarProperty()

        // File menu
        openFileItem.graphic = MenuIcon("img/open.png")
        openFileItem.setOnAction {
            val file = selectFile(stage = window)
            decompileFile(file)
        }
        closeProject.setOnAction {
            //TODO: Catch NullPointerException when yrying to close a tab that doesn't exist using <Cmd/Ctrl + w>
            this.projectPane.tabPane.tabs.remove(projectPane.selectedTab)
        }
        saveItem.graphic = MenuIcon("img/save.png")
        saveItem.isDisable = true
        saveItem.setOnAction { }
        saveAllItem.isDisable = true
        saveAllItem.graphic = MenuIcon("img/save_all.png")
        saveAllItem.setOnAction { }
        exitItem.setOnAction {
            exitProcess(1)
        }
        fileMenu.items.addAll(
            openFileItem,
            SeparatorMenuItem(),
            closeProject,
            SeparatorMenuItem(),
            saveItem,
            saveAllItem,
            SeparatorMenuItem(),
            recentsMenu,
            SeparatorMenuItem(),
            exitItem
        )

        // Edit menu
        copyItem.isDisable = true
        copyItem.graphic = MenuIcon("img/copy.png")
        copyItem.setOnAction { }
        pasteItem.isDisable = true
        pasteItem.graphic = MenuIcon("img/paste.png")
        pasteItem.setOnAction { }
        selectAllItem.isDisable = true
        selectAllItem.setOnAction { }
        findItem.isDisable = true
        findItem.setOnAction { }
        editMenu.items.addAll(copyItem, pasteItem, SeparatorMenuItem(), selectAllItem, SeparatorMenuItem(), findItem)

        // Navigation menu
        openTypeItem.isDisable = true
        openTypeItem.graphic = MenuIcon("img/open_type.png")
        openTypeItem.setOnAction { }
        openTypeHierarchyItem.isDisable = true
        openTypeHierarchyItem.setOnAction { }
        goToLineItem.isDisable = true
        goToLineItem.setOnAction { }
        navMenu.items.addAll(openTypeItem, openTypeHierarchyItem, goToLineItem)

        // Search menu
        searchItem.isDisable = true
        searchItem.graphic = MenuIcon("img/search_src.png")
        searchItem.setOnAction { }
        searchMenu.items.addAll(searchItem)

        // Help menu
        wikiItem.setOnAction {
            try {
                Desktop.getDesktop().browse(URL("https://github.com/dadler64/jd-fx/wiki").toURI())
            } catch (e: IOException) {
                errorln("Could not open link")
                e.printStackTrace()
            } catch (e: URISyntaxException) {
                errorln("Bad URI")
                e.printStackTrace()
            }
        }
        prefItem.graphic = MenuIcon("img/preferences.png")
        prefItem.setOnAction { }
        aboutItem.setOnAction {
            val dialog = Stage()
            dialog.title = "About - ${JDFX.TITLE} v${JDFX.VERSION}"
            dialog.initModality(Modality.APPLICATION_MODAL)
            dialog.initOwner(window)
            val dialogVbox = VBox(20.0)
            dialogVbox.children.add(Text("JDFX - Copyright Dan Adler 2018-2019"))
            dialogVbox.children.add(Text("JDFX is licenced under the GNU GPL Version 3.0"))
            dialogVbox.children.add(Text("Fernflower.jar - Copyright Jetbrains 2018-2019"))
            dialogVbox.children.add(Text("Fernflower.jar is licenced under the Apache Licence Version 2.0."))
            val dialogScene = Scene(dialogVbox)
            dialog.scene = dialogScene
            dialog.show()
        }
        helpMenu.items.addAll(wikiItem, SeparatorMenuItem(), prefItem, aboutItem)


        bar.menus.addAll(fileMenu, editMenu, navMenu, searchMenu, helpMenu)

        // Initialize accelerators for the menu items
        initAccelerators()

        return bar
    }

    /**
     *  Instantiate accelerators for menu items
     */
    private fun initAccelerators() {
        val system = System.getProperty("os.name")
        infoln("Current System: $system")

        val controlKey = if (system.startsWith(
                prefix = "mac",
                ignoreCase = true
            )
        ) KeyCombination.META_DOWN else KeyCombination.CONTROL_DOWN

        openFileItem.accelerator = KeyCodeCombination(KeyCode.O, controlKey)
        closeProject.accelerator = KeyCodeCombination(KeyCode.W, controlKey)
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

    /**
     *  Open a file to be decompiled by Fernflower
     *  @param stage the main stage of your program
     *  @return the file picked in the dialogue
     */
    private fun selectFile(stage: Stage): File? {
        var jarFile: File? = null

        try {
            val fileChooser = FileChooser()
            fileChooser.title = "Project Location"
            fileChooser.initialDirectory = File(System.getProperty("user.home"))
            fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter(
                    "Java Binaries"
                    , "*.jar"
                    // TODO: Add support for *.class *.ear *.java *.war *.zip
                ),
                FileChooser.ExtensionFilter("All Files", "*.*")
            )
            jarFile = fileChooser.showOpenDialog(stage.owner)
        } catch (e: RuntimeException) {
            errorln("Could not open file or set up project!")
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            infoln("No file picked")
        }

        return jarFile
    }

    /**
     * Function which handles dealing with the decompilation of the inputted jar file
     */
    private fun decompileFile(jarFile: File?) {
        var isAlreadyOpen = false
        if (jarFile != null) {

            val projectName = jarFile.nameWithoutExtension
            val projectDir = File(jarFile.absolutePath.removeSuffix(".${jarFile.extension}"))

            // Prevent the same file from being opened twice
            for (file in openFiles) {
                if (file == projectName) {
                    isAlreadyOpen = true
                }
            }

            if (!isAlreadyOpen) {
                debugln("Project dir path: $projectDir", this::class.java)

                // Decompile Jar on a separate thread
                val decompiler = Decompiler(jarFile, projectDir)
                val decompileThread = Thread(decompiler)
                decompileThread.start()

                this.projectPane.tabPane.tabs.add(ProjectTab(projectDir))
                openFiles.add(projectName)
            } else {
                warningln("Jar \"$projectName.jar\" is already decompiled in another tab!")
            }
        } else {
            infoln("$jarFile could not be selected")
        }

    }

    companion object {
        private const val MENU_ICON_SIZE = 16.0
        val openFiles = ArrayList<String>()
    }

    internal class MenuIcon(imagePath: String) : ImageView(loadImg(imagePath, 16.0))
}
