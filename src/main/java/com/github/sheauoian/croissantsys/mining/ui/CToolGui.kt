package com.github.sheauoian.croissantsys.mining.ui

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.mining.CToolBuilder
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.ItemBuilder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class CToolGui(val user: UserDataOnline): ChestGui(4, "ピッケル強化") {
    var builder: CToolBuilder? = null
    val builderPane = StaticPane(3, 0, 3, 1)
    val breakablePane = OutlinePane(0, 1, 9, 2)

    init {
        CroissantSys.Companion.instance.config.getItemStack("tool.editor.item")?.let { item ->
            builder = CToolBuilder(item)
        }

        this.setOnGlobalClick { event ->
            event.isCancelled = true
            CroissantSys.broadcast(event.clickedInventory?.type.toString())
            if (event.clickedInventory?.type == InventoryType.PLAYER) {
                val item = event.clickedInventory?.getItem(event.slot) ?: return@setOnGlobalClick
                CroissantSys.broadcast(item.toString())
                if (item.type.isBlock && builder != null) {
                    builder?.addBreakable(item.type)
                    update()
                } else if (!item.isEmpty) {
                    builder = CToolBuilder(item)
                    event.clickedInventory?.remove(item)
                    update()
                }
            }
        }

        addPane(builderPane)
        addPane(breakablePane)
        update()
    }

    override fun update() {
        builderPane.clear()
        breakablePane.clear()

        val icon: GuiItem = builder?.let {
            val item = it.build()
            GuiItem(item.clone()) {
                if (user.canAddItem(item)) {
                    user.addItem(item)
                    this.builder = null
                    update()
                }
            }
        } ?: GuiItem(
            ItemBuilder(Material.BARRIER).displayName("<color:#ddff00>[ ツール強化 ]").build()
        )
        builderPane.addItem(icon, 1, 0)

        builder?.let { builder ->
            val info = GuiItem(
                ItemBuilder(Material.DIAMOND_PICKAXE).displayName("<color:#ddff00>[ ツール情報 ]").lore(
                    listOf(
                        "<color:#ddee00>効率性: ${builder.efficiency}",
                        "<color:#ddee00>破壊可能: ${builder.breakable.size}"
                    )
                ).build()
            )
            builderPane.addItem(info, 2, 0)

            builder.breakable.forEach { material ->
                val guiItem = GuiItem(ItemStack(material)) {
                    builder.removeBreakable(material)
                    update()
                }
                breakablePane.addItem(guiItem)
            }
        }
        super.update()
    }
}