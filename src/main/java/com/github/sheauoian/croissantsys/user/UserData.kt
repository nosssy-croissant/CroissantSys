package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.util.status.StatusType
import com.github.sheauoian.croissantsys.world.warppoint.UnlockedWarpPointManager
import java.util.*

open class UserData(
    val uuid: UUID,
    open val money: Int,
    open val health: Double,
    open val maxHealth: Double
): DbDriver() {
    var lastAccessed: Long = System.currentTimeMillis()

    fun access() {
        lastAccessed = System.currentTimeMillis()
    }

    val baseStatus: MutableMap<StatusType, Double> = EnumMap(StatusType::class.java)
    val wearing: Wearing = Wearing(uuid.toString())
    val unlockedWarpPointManager: UnlockedWarpPointManager = UnlockedWarpPointManager(uuid)

    open fun save() {
        UserDataManager.instance.saveAsync(this)
        wearing.saveWearing()
    }

    init {
        updateStatus()
    }

    fun updateStatus() {
        updateBaseStatus()
    }

    private fun updateBaseStatus() {
        StatusType.entries.forEach {
            baseStatus[it] = it.baseVolume
        }
        baseStatus += wearing.getWearingStatus()
    }
}