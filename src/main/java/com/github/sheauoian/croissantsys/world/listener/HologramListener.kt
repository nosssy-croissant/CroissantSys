package com.github.sheauoian.croissantsys.world.listener

import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import eu.decentsoftware.holograms.event.HologramClickEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class HologramListener: Listener {
    @EventHandler
    fun onInteract(e: HologramClickEvent) {
        if (e.hologram.name.contains("warp_")) {
            WarpPointManager.instance.find(e.hologram.name)?.use(e.player)
        }
    }
}