package com.github.sheauoian.croissantsys.pve.equipment.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class EquipmentStoringListener: Listener {
    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        val item = e.itemDrop.itemStack
        val id = Equipment.getId(item) ?: return
        e.isCancelled = true
        e.player.server.scheduler.runTaskLater(CroissantSys.instance, Runnable {
            e.player.inventory.removeItem(item)
        }, 1)

        e.player.sendMessage("id: $id")
        EquipmentManager.instance.store(item, e.player.uniqueId.toString())
    }
}