package com.github.sheauoian.croissantsys.user.online.ui.equipment.pane

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import org.bukkit.event.inventory.ClickType
import java.util.ArrayList

class EStoragePane(
    val user: UserDataOnline,
    bodyPart: BodyPart?,
    private val isLevelUpUi: Boolean,
    x: Int,
    y: Int,
    private val l: Int,
    private val h: Int
): PaginatedPane(x, y, l, h)
{
    private lateinit var collection: Collection<Equipment>

    init {
        reset(bodyPart)
    }

    fun reset(bodyPart: BodyPart?) {
        collection = user.eManager.getAll(bodyPart)
        setEquipments()
    }

    private fun getItemPane(queue: ArrayDeque<Equipment>): List<OutlinePane> {
        val list = ArrayList<OutlinePane>()
        var pane = OutlinePane(0, 0, l, h)
        var i = 0
        while (queue.size >= 1) {
            if (i >= l*h) {
                list.add(pane)
                pane = OutlinePane(0, 0, l, h)
                i = 0
            }
            val e = queue.removeFirst()
            pane.addItem(GuiItem(e.getItem()) { event ->
                if (isLevelUpUi) {
                    user.openELeveling(e)
                } else {
                    when (event.click) {
                        ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> { user.openELeveling(e) }
                        ClickType.DROP, ClickType.CONTROL_DROP -> {
                            if (user.player.inventory.firstEmpty() != -1)
                                user.player.inventory.addItem(e.getItem())
                        }

                        else -> {
                            user.eManager.load(e)
                            user.wearing.setWearing(e)
                            user.openStatusMenu()
                        }
                    }
                }
            })
            i ++
        }
        list.add(pane)
        return list
    }
    private fun getItemPane(collection: Collection<Equipment>): List<OutlinePane> {
        return getItemPane(ArrayDeque(collection))
    }

    fun update(collection: Collection<Equipment>) {
        clear()
        getItemPane(collection).forEach {
            addPage(it)
        }
    }

    fun setEquipments() {
        setEquipments(0, false)
    }

    fun setEquipments(sortMode: Int, reverse: Boolean) {
        val sorted = when(sortMode) {
            1 -> collection.sortedBy { -1 * it.level }
            2 -> collection.sortedBy { it.data.id }
            3 -> collection.sortedBy { -1 * it.rarity }
            else -> collection
        }
        if (reverse)
            update(sorted.reversed())
        else
            update(sorted)
    }
}