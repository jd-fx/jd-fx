package com.adlerd.gui.tabs

import javafx.scene.control.MenuItem
import javafx.scene.control.Tab

open class CustomTab: Tab() {
    val title = this.toString()

    init {
        this.text = title
        //TODO fix context menu
//        initContextMenu()
    }

    private fun initContextMenu() {
        val close = MenuItem("Close")
        close.setOnAction {
            this.tabPane.tabs.remove(this)
        }
        val closeOther = MenuItem("Close Other")
        closeOther.setOnAction {
            for (tab in this.tabPane.tabs) {
                if (tab != this) {
                    tab.tabPane.tabs.remove(tab)
                }
            }
        }
        val closeAll = MenuItem("Close All")
        closeAll.setOnAction {
            for (tab in this.tabPane.tabs) {
                tab.tabPane.tabs.remove(tab)
            }
        }

        this.contextMenu.items.addAll(close, closeOther, closeAll)
    }
}