package com.github.sheauoian.croissantsys.pve.equipment.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class EquipmentStoringListener: Listener {
    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        val item = e.itemDrop.itemStack
        val nbtItem = NBTItem(item)
        e.player.sendMessage(nbtItem.toString())
        if (nbtItem.getCompound("equipment") != null) {
            e.isCancelled = true
            EquipmentManager.instance.store(item, e.player.uniqueId.toString())

            // Wait a tick to remove the item from the player's inventory
            e.player.server.scheduler.runTaskLater(CroissantSys.instance, Runnable {
                e.player.inventory.removeItem(item)
            }, 1)
        }
    }
}