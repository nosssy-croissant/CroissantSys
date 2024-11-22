package com.github.sheauoian.croissantsys.user.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.UserDataManager
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.server.ServerListPingEvent

class PlayerJoinListener : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        UserDataManager.instance.join(e.player)
        e.player.sendMessage("データがロードされました")
        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1.0f, 1.0f)
        e.player.playSound(sound)
    }
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        UserDataManager.instance.quit(e.player)
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        e.respawnLocation = CroissantSys.instance.initialSpawnPoint
    }

    @EventHandler
    fun onServerPing(e: ServerListPingEvent) {
        e.motd(
            MiniMessage.miniMessage()
                .deserialize("<gradient:#7755BA:#AA5591><bold>CROISSANT WORLD</bold></gradient> ❚ <color:#AAAAAA>Original RPG Server</color>")
        )
    }
}
