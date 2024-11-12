package com.github.sheauoian.croissantsys.user.online.ui

import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class StatusGui(
    private val user: UserData
): ChestGui(6, ComponentHolder.of(Component.text("ステータス"))) {
    init {
        setOnGlobalClick {event -> event.isCancelled = true}

        val background = OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST)
        background.addItem(GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
        background.setRepeat(true)
        addPane(background)
        val equipmentPane = StaticPane(3, 1, 3, 4)
        BodyPart.entries.forEach {
            val item = user.wearing.get(it)?.getItem() ?: it.empty()
            equipmentPane.addItem(GuiItem(item)
            { _ ->
                if (user is UserDataOnline)
                    user.openEStorage(it)
            },
                it.x, it.y)
        }
        addPane(equipmentPane)
        update()
    }
}