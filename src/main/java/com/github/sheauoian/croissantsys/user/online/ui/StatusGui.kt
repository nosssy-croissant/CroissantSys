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
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
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
            val item = user.wearing.get(it)?.getItem() ?: getEmptyItem(it)
            val (x, y) = when (it) {
                BodyPart.MainHand -> 0 to 1
                BodyPart.SubHand -> 2 to 1
                BodyPart.Head -> 1 to 0
                BodyPart.Body -> 1 to 1
                BodyPart.Leg -> 1 to 2
                BodyPart.Foot -> 1 to 3
            }


            equipmentPane.addItem(GuiItem(item) { _ ->
                if (user is UserDataOnline)
                    user.openEStorage(it)
            }, x, y)
        }
        addPane(equipmentPane)
        update()
    }

    private fun getEmptyItem(body: BodyPart): ItemStack {
        val material = body.material
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName(MiniMessage.miniMessage()
            .deserialize("<color:#aaaaaa>[ 空のスロット ]").decoration(TextDecoration.ITALIC, false))
        item.setItemMeta(meta)
        return item
    }
}