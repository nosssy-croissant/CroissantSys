package com.github.sheauoian.croissantsys.world.treasure.listener

import com.github.sheauoian.croissantsys.world.treasure.TreasureChest
import com.github.sheauoian.croissantsys.world.treasure.TreasureManager
import de.tr7zw.nbtapi.NBTBlock
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class TreasureChestListener: Listener {
    @EventHandler
    fun onOpen(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return
        if (block is Chest) {
            val chest = block as Chest
            NBTBlock(block).data.getString("treasure")?.let {
                e.isCancelled = true
                TreasureManager.instance.find(it)?.let { inventory ->
                    inventory.open(e.player)
                }
            }
        }
    }
}