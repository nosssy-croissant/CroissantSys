package com.github.sheauoian.croissantsys.world

import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import org.bukkit.Location
import org.bukkit.entity.Player

interface WorldObject {
    val location: Location
    fun update()
    fun use(user: UserDataOnline)
    fun use(player: Player) {
        use(UserDataManager.instance.get(player))
    }
    fun distance(other: WorldObject): Double {
        return location.distance(other.location)
    }
    fun distance(other: Location): Double {
        return location.distance(other)
    }
}