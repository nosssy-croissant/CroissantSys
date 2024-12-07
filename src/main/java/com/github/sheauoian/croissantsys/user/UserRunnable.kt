package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import org.bukkit.scheduler.BukkitRunnable

class UserRunnable: BukkitRunnable() {
    override fun run() {
        CroissantSys.instance.server.onlinePlayers.forEach {
            UserDataManager.instance.get(it).update()
        }
    }
}