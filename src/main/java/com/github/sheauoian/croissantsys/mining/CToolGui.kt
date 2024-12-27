package com.github.sheauoian.croissantsys.mining

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.mining.attr.EfficiencyCToolAttr
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.ItemBuilder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class CToolGui(val user: UserDataOnline): ChestGui(4, "ピッケル強化") {
    var builder: CToolBuilder? = null
    val builderPane = StaticPane(0, 0, 9, 2)
    val breakablePane = OutlinePane(0, 2, 9, 2)

    init {
        CroissantSys.Companion.instance.config.getItemStack("tool.editor.item")?.let { item ->
            builder = CToolBuilder(item)
        }
        this.setOnGlobalClick { event ->
            event.isCancelled = true
            CroissantSys.Companion.broadcast(event.clickedInventory?.type.toString())
            if (event.clickedInventory?.type == InventoryType.PLAYER)
                onPlayerInventoryClick(event)
        }
        addPane(builderPane)
        addPane(breakablePane)
        update()
    }

    private fun onPlayerInventoryClick(event: InventoryClickEvent) {
        val item = event.clickedInventory?.getItem(event.slot) ?: return
        if (item.type.isBlock && builder != null) {
            builder?.addBreakable(item.type)
            update()
        } else if (!item.isEmpty) {
            builder = CToolBuilder(item)
            event.clickedInventory?.remove(item)
            update()
        }
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
        builderPane.addItem(icon, 4, 0)

        builder?.let { builder ->
            listOf(
                EfficiencyCToolAttr(builder)
            ).forEachIndexed { index, attr ->
                CroissantSys.broadcast(attr.name)
                val guiItem = attr.getGuiItem(user)
                guiItem.setAction {
                    if (attr.tryLevelUp(user))
                        update()
                }
                builderPane.addItem(guiItem, index*2 + 1, 1)
            }

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