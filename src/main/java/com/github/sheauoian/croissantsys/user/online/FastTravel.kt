package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint

class FastTravel(val uuid: String) {
    private val datum: MutableMap<String, WarpPoint> = mutableMapOf()

    init {
        reload()
    }


    fun teleport(user: UserDataOnline, k: String) {
        datum[k]?.let {
            user.player.teleport(it.location)
        }
    }

    fun reload() {
        FastTravelManager.instance.reload(this)
    }

    fun add(w: WarpPoint) {
        datum[w.id] = w
    }

    fun save() {
        FastTravelManager.instance.save(uuid, datum.values)
    }
}