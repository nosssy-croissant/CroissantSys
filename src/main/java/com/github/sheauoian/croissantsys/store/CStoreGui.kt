package com.github.sheauoian.croissantsys.store

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane

class CStoreGui(val user: UserDataOnline, val store: CStore): ChestGui(6, store.name) {
    val pane = OutlinePane(0, 0, 9, 6)

    init {
        this.setOnGlobalClick { event ->
            event.isCancelled = true
        }
        updatePane()
        this.addPane(pane)
        this.update()
    }

    private fun updatePane() {
        pane.clear()
        store.products.forEach { product ->
            val guiItem = product.getIcon(user)
            pane.addItem(guiItem)
        }
    }
}