package com.github.sheauoian.croissantsys.world.treasure

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class TreasureChestData(val id: String, val name: String, private val minItem: Int, private val maxItem: Int) {
    private val lootTable: List<ItemStack> = listOf()
    constructor(id: String, name: String):
            this(id, name, 1, 9)
    
    fun open(player: Player) {
        val chest = TreasureChestInventory()
        chest.addPane(getPane())
        chest.show(player)
    }

    private fun getPane(): StaticPane {
        val pane = StaticPane(0, 0, 9, 3)

        repeat((0 until Random.nextInt(minItem, maxItem)).count()) {
            val item = lootTable[Random.nextInt(0, lootTable.size)]
            pane.addItem(GuiItem(item), Random.nextInt(9), Random.nextInt(3))
        }

        return pane
    }

    class TreasureChestInventory: ChestGui(3, "Treasure Chest") {
        init {
            update()
        }
    }
}