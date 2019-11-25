package com.jdfx.gui.tabs

import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.Tab

abstract class CustomTab : Tab() {

    init {
        this.contextMenu = initContextMenu()
    }

    private fun initContextMenu(): ContextMenu {

        val close = MenuItem("Close")
        close.setOnAction {
            this.tabPane.tabs.remove(this)
        }
        val closeOthers = MenuItem("Close Others")
        closeOthers.setOnAction {
            // TODO: Implement "Close Others" option
            for (tab in this.tabPane.tabs) {
                if (tab != this) {
                    this.tabPane.tabs.remove(tab)
                }
            }
        }
        val closeAll = MenuItem("Close All")
        closeAll.setOnAction {
            this.tabPane.tabs.clear()
        }

        val selectNextTab = MenuItem("Select Next Tab")
        selectNextTab.setOnAction {
            val pos = this.tabPane.tabs.indexOf(this)
            val size = this.tabPane.tabs.size

            if (pos == size - 1) {
                this.tabPane.selectionModel.select(0)
            } else {
                this.tabPane.selectionModel.select(pos + 1)
            }
        }
        val selectPrevTab = MenuItem("Select Previous Tab")
        selectPrevTab.setOnAction {
            val pos = this.tabPane.tabs.indexOf(this)
            val size = this.tabPane.tabs.size

            if (pos == 0) {
                this.tabPane.selectionModel.select(size - 1)
            } else {
                this.tabPane.selectionModel.select(pos - 1)
            }
        }

        return ContextMenu(close, closeOthers, closeAll, SeparatorMenuItem(), selectNextTab, selectPrevTab)
    }

    companion object {
        const val ICON_SIZE = 16.0
    }
}