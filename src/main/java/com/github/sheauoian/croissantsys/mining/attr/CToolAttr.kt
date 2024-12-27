package com.github.sheauoian.croissantsys.mining.attr

import com.github.sheauoian.croissantsys.mining.CToolBuilder
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.ItemBuilder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import org.bukkit.Material

abstract class CToolAttr(val builder: CToolBuilder) {
    abstract fun getLevel(): Int
    abstract fun getPrice(): Int
    abstract fun levelUp()
    abstract val maxLevel: Int
    abstract val icon: Material
    abstract val name: String

    fun tryLevelUp(user: UserDataOnline): Boolean {
        val price = getPrice()
        if (user.money < price) {
            user.sendMiniMessage(
                "<color:red>お金が足りません！ </color><color:gray>(必要: <color:#dddd00>$price</color>)</color>"
            )
            return false
        }
        user.money -= price
        levelUp()
        user.sendMiniMessage("<color:yellow>レベルアップしました！</color>")
        return true
    }

    fun getGuiItem(user: UserDataOnline): GuiItem {
        val price = getPrice()
        val lore = mutableListOf(
            "<color:gray>価格: <color:#dddd00>$price</color></color>"
        )
        if (getLevel() < maxLevel) {
            lore.add("<color:gray>次のレベルまで: <color:#dddd00>${getPrice()}</color></color>")
        }
        return GuiItem(
            ItemBuilder(icon).displayName(
                "<color:gray>${name} Lv.${getLevel()}</color>"
            ).lore(lore).build()
        )
    }
}