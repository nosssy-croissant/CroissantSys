package com.github.sheauoian.croissantsys.world.warppoint

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import java.sql.SQLException
import java.util.*

class UnlockedWarpPointManager(uuid: UUID) {
    private val repository = UnlockedWarpPointRepository(uuid.toString())
    private val unlockedWP: MutableList<String> = repository.load().toMutableList()

    fun unlock(warp: WarpPoint) {
        try {
            repository.save(warp.id)
        } catch (e: SQLException) {
            throw e
        }
        unlockedWP.add(warp.id)
    }

    fun isUnlocked(id: String): Boolean {
        return unlockedWP.contains(id)
    }

    fun getAll(): List<String> {
        return unlockedWP
    }

    private fun getAllWarpPoints(): List<WarpPoint> {
        return unlockedWP.mapNotNull { WarpPointManager.instance.load(it) }
    }

    fun getUIPane(): OutlinePane {
        val pane = OutlinePane(0, 0, 9, 6)
        for (warpPoint in getAllWarpPoints()) {
            pane.addItem(warpPoint.getGuiItem())
        }
        return pane
    }

    fun getUI(): ChestGui {
        return WarpPointUI(this)
    }

    private class WarpPointUI(manager: UnlockedWarpPointManager): ChestGui(6, "ワープポイント") {
        init {
            setOnGlobalClick { event ->
                event.isCancelled = true
            }
            addPane(manager.getUIPane())
            update()
        }
    }
}