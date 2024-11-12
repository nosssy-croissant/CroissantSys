package com.github.sheauoian.croissantsys.user.online.ui.equipment

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class ELevelUpUI(val user: UserDataOnline): ChestGui(6, title){
    companion object{
        private val title = ComponentHolder.of(
            Component.text("装備品の強化")
                .decorate(TextDecoration.BOLD)
                .color(TextColor.color(0x7752ee)
                )
        )
    }
    private val itemPane = StaticPane(4, 2, 1, 1)

    init {
        addPane(itemPane)
        update()
    }

    fun setEquipment(equipment: Equipment?) {
        val item: GuiItem
        if (equipment != null) {
            item = GuiItem(equipment.getItem()) { event ->
                when (event.click) {
                    ClickType.CONTROL_DROP, ClickType.DROP -> {
                        setEquipment(null)
                    }
                    ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT, ClickType.SWAP_OFFHAND -> {
                        user.openELevelingStorage(null)
                    }
                    else -> {
                        user.eManager.levelUp(equipment)
                        setEquipment(equipment)
                    }
                }
            }
        } else {
            item = GuiItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)) {
                user.openELevelingStorage(null)
            }
        }
        itemPane.addItem(item, 0, 0)
        update()
    }
}