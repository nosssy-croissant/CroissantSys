package com.github.sheauoian.croissantsys.user.online.ui

import com.github.sheauoian.croissantsys.pve.skill.SkillListGui
import com.github.sheauoian.croissantsys.user.online.EquipmentStorage
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.user.online.ui.equipment.EStorageUI
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MainMenu(private val user: UserDataOnline): ChestGui(6, ComponentHolder.of(
    MiniMessage.miniMessage().deserialize("<gradient:#776233:#623377><b>メインメニュー</b></gradient>")
)) {
    init {
        this.setOnGlobalClick { event ->
            event.isCancelled = true
        }
        val pane = StaticPane(0, 0, 9, 6)
        this.addPane(pane)

        pane.addItem(GuiItem(ItemStack(Material.STONE_BUTTON)) {
            SkillListGui(user).show(user.player)
        }, 0, 0)

        pane.addItem(GuiItem(ItemStack(Material.STONE_BUTTON)) {
            EStorageUI(user, null).show(user.player)
        }, 1, 0)

        pane.addItem(GuiItem(ItemStack(Material.STONE_BUTTON)) {
            StatusGui(user).show(user.player)
        }, 2, 0)

        this.update()
    }
}
