package com.github.sheauoian.croissantsys.mining

import com.github.sheauoian.croissantsys.CroissantSys
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable

class MiningListener : Listener {
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        if (player.gameMode.name == "CREATIVE") return

        val block = event.block
        val oreType = OreType.entries.find { it.oreMaterial == block.type } ?: return

        event.isCancelled = true
        block.blockData = oreType.oreReplaceMaterial.createBlockData()
        player.inventory.addItem(oreType.dropMaterial)
        ReplaceTask(block, oreType).runTaskLater(CroissantSys.instance, 20L * oreType.regenerateTimeSecond)
    }

    private class ReplaceTask(val block: Block, val oreType: OreType) : BukkitRunnable() {
        override fun run() {
            block.blockData = oreType.oreMaterial.createBlockData()
        }
    }
}