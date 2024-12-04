package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.pve.skill.SkillTag
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.status.StatusType
import com.github.sheauoian.croissantsys.world.warppoint.UnlockedWarpPointManager
import java.util.*

open class UserData(
    val uuid: UUID,
    open var money: Int,
    open val health: Double,
    open val maxHealth: Double,
    open var level: Int,
    open var exp: Int
) {
    var lastAccessed: Long = System.currentTimeMillis()

    fun access() {
        lastAccessed = System.currentTimeMillis()
    }

    val baseStatus: MutableMap<StatusType, Double> = EnumMap(StatusType::class.java)
    val wearing: Wearing = Wearing(uuid.toString())
    val unlockedWarpPointManager: UnlockedWarpPointManager = UnlockedWarpPointManager(uuid)

    val skillTags = mutableListOf<SkillTag>()

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

    fun addExp(exp: Int) {
        if (this is UserDataOnline) {
            this.player.sendMessage("§a+${exp}exp")
        }
        this.exp += exp
        if (this.exp >= level * 100) {
            this.exp -= level * 100
            level++
            if (this is UserDataOnline) {
                this.player.sendMessage("§aレベルが上がりました！ 現在のレベル: $level")
            }
        }
    }

    fun addMoney(money: Int) {
        this.money += money
    }
}