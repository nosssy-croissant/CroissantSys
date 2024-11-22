package com.github.sheauoian.croissantsys.world.warppoint

import com.github.sheauoian.croissantsys.CroissantSys
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class WarpPointManager {
    companion object {
        val instance = WarpPointManager()
    }

    private val datum: ConcurrentHashMap<String, WarpPoint> = ConcurrentHashMap()

    fun reload() {
        datum.clear()
        val file = File(CroissantSys.instance.dataFolder, "warp_point.yml")
        val config = YamlConfiguration.loadConfiguration(file)

        // load all warp points from config
        config.getKeys(false).forEach {
            val name = config.getString("$it.name") ?: return@forEach
            val location = config.getLocation("$it.location") ?: return@forEach
            datum[it] = WarpPoint(it, name, location)
        }

        reloadHologram()
    }

    fun reloadHologram() {
        datum.values.forEach { it.update() }
    }

    fun insert(id: String, name: String, location: Location): WarpPoint? {
        if (datum.containsKey(id)) {
            return null
        }
        val warpPoint = WarpPoint(id, name, location)
        save(warpPoint)
        datum[id] = warpPoint
        return warpPoint
    }

    fun delete(id: String): Boolean {
        if (datum.containsKey(id)) {
            datum[id]?.removeHologram()
            datum.remove(id)
            val file = File(CroissantSys.instance.dataFolder, "warp_point.yml")
            val config = YamlConfiguration.loadConfiguration(file)
            config.set(id, null)
            config.save(file)
            return true
        }
        return false
    }

    fun move(id: String, location: Location): Boolean {
        val oldWarpPoint = datum[id] ?: return false
        val warpPoint = WarpPoint(id, oldWarpPoint.name, location)
        datum[id] = warpPoint
        save(warpPoint)
        return true
    }

    fun find(k: String): WarpPoint? {
        return datum[k]
    }

    fun save(v: WarpPoint) {
        val file = File(CroissantSys.instance.dataFolder, "warp_point.yml")
        val config = YamlConfiguration.loadConfiguration(file)
        config.set("${v.id}.name", v.name)
        config.set("${v.id}.location", v.location)
        config.save(file)
    }

    @Deprecated("use find instead")
    fun load(k: String): WarpPoint? {
        return datum[k]
    }

    val ids: Collection<String>
        get() {
            return datum.keys
        }

    val warps: Collection<WarpPoint>
        get() {
            return datum.values
        }
}