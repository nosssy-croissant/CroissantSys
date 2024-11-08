package com.github.sheauoian.croissantsys.world.treasure.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.world.treasure.TreasureChest
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class TreasureChestListener : Listener {
    @EventHandler
    fun onOpen(e: PlayerInteractEvent) {
        val block = e.clickedBlock as? Chest ?: return
        val meta = block.getMetadata("treasure").firstOrNull {
            it.owningPlugin == CroissantSys.instance && it.value() is TreasureChest
        } ?: return
        (meta.value() as TreasureChest).use(UserDataManager.instance.get(e.player))
    }
}