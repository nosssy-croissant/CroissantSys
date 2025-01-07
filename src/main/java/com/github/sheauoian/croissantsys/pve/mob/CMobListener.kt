package com.github.sheauoian.croissantsys.pve.mob

import com.github.sheauoian.croissantsys.pve.mob.CMob.CMobManager
import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CMobListener: Listener {
    @EventHandler
    fun onMythicMobDeath(e: MythicMobLootDropEvent) {
        val mob = e.mob
        val killer = e.killer
        (killer as? Player)?.sendMessage("You killed a ${mob.type.internalName}")
        val cMob = CMobManager.get(mob.type.internalName)
        if (cMob != null && killer is Player) {
            cMob.onKilled(killer)
        }
    }
}