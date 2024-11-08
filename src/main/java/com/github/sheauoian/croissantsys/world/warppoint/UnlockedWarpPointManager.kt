package com.github.sheauoian.croissantsys.world.warppoint

import java.sql.SQLException
import java.util.*

class UnlockedWarpPointManager(val uuid: UUID) {
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

    fun getAllWarpPoints(): List<WarpPoint> {
        return unlockedWP.mapNotNull { WarpPointManager.instance.load(it) }
    }
}