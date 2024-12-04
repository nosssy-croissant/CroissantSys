package com.github.sheauoian.croissantsys.pve.skill

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SkillListGui(val user: UserDataOnline): ChestGui(6, "スキル一覧") {
    val pane = OutlinePane(1, 1, 7, 4)
    var mode = true

    init {
        this.setOnGlobalClick { event ->
            event.isCancelled = true
        }
        val buttonPane = StaticPane(2, 2, 3, 3)
        val button = GuiItem(ItemStack(Material.STONE_BUTTON)) {
            mode = !mode
            updatePane()
        }
        buttonPane.addItem(button, 1, 1)
        this.addPane(buttonPane)
        this.addPane(pane)
        updatePane()

        this.update()
    }

    private fun updatePane() {
        pane.clear()
        NormalSkill.skills.values.forEach { skill ->
            val guiItem = if (mode) {
                skill.iconBuilder.lore(skill.getDescriptionString())
            } else {
                skill.iconBuilder.lore(skill.getDescriptionString(user))
            }
            pane.addItem(GuiItem(guiItem.build(user.skill == skill)) {
                user.player.sendMessage("スキルを ${skill.skillName} に変更しました")
                user.skill = skill
                updatePane()
            })
        }
        this.update()
    }
}