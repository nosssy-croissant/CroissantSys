package com.github.sheauoian.croissantsys.listener

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent

class ElevatorListener : Listener {

    // 上昇
    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        handleElevatorEvent(e.player.location, 1.0, e.player)
    }

    // 下降
    @EventHandler
    fun onSneakToggle(e: PlayerToggleSneakEvent) {
        if (e.isSneaking) handleElevatorEvent(e.player.location, -1.0, e.player)
    }

    private fun handleElevatorEvent(location: Location, dy: Double, player: Player) {
        var loc = location
        if (isElevatorBlock(loc)) {
            loc = loc.add(0.0, dy*2, 0.0)
            repeat(20) {
                loc = loc.add(0.0, dy, 0.0)
                if (isElevatorBlock(loc)) {
                    loc = loc.add(0.0, 1.0, 0.0)
                    player.teleport(loc)
                    player.playSound(loc, Sound.ENTITY_WITHER_SHOOT, 0.8f, 2.0f)
                    return
                }
            }
        }
    }

    private fun isElevatorBlock(location: Location): Boolean {
        return location.block.type == Material.HEAVY_WEIGHTED_PRESSURE_PLATE &&
                location.add(0.0, -1.0, 0.0).block.type == Material.IRON_BLOCK
    }
}