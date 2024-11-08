package com.github.sheauoian.croissantsys.world.warppoint

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.DbDriver
import org.bukkit.Location
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.concurrent.ConcurrentHashMap

class WarpPointManager(private val con: Connection){
    companion object {
        val instance = WarpPointManager(DbDriver.con)
    }

    private val saveStm: PreparedStatement
    private val loadStm: PreparedStatement

    init {
        con.createStatement().use { stmt ->
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS warp_point (
                    id      STRING  PRIMARY KEY,
                    name    STRING  NOT NULL,
                    w       STRING  NOT NULL,
                    x       REAL    NOT NULL,
                    y       REAL    NOT NULL,
                    z       REAL    NOT NULL,
                    yaw     REAL    NOT NULL,
                    pitch   REAL    NOT NULL
                )
            """.trimIndent())
        }

        loadStm = con.prepareStatement("""
            SELECT name, w, x, y, z, yaw, pitch FROM warp_point WHERE id = ?
        """.trimIndent())

        saveStm = con.prepareStatement("""
            INSERT INTO warp_point(id, name, w, x, y, z, yaw, pitch) VALUES(?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent())
    }

    private val datum: ConcurrentHashMap<String, WarpPoint> = ConcurrentHashMap()

    fun reload() {
        con.createStatement().use { stmt ->
            stmt.executeQuery("SELECT id FROM warp_point").use { rs ->
                while (rs.next()) {
                    loadFromDatabase(rs.getString(1))
                }
            }
        }
        reloadHologram()
    }

    fun reloadHologram() {
        datum.values.forEach { it.update() }
    }

    fun insert(id: String, name: String, location: Location): Boolean {
        if (datum.containsKey(id)) {
            return false
        }
        val warpPoint = WarpPoint(id, name, location)
        save(warpPoint)
        datum[id] = warpPoint
        return true
    }

    fun delete(id: String): Boolean {
        if (datum.containsKey(id)) {
            datum.remove(id)
            con.createStatement().use { stmt ->
                stmt.executeUpdate("DELETE FROM warp_point WHERE id = '$id'")
            }
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
        saveStm.apply {
            setString(1, v.id)
            setString(2, v.name)
            setString(3, v.location.world.name)
            setDouble(4, v.location.x)
            setDouble(5, v.location.y)
            setDouble(6, v.location.z)
            setFloat(7, v.location.yaw)
            setFloat(8, v.location.pitch)
            executeUpdate()
        }
    }

    fun load(k: String): WarpPoint? {
        return datum[k] ?: loadFromDatabase(k)
    }

    private fun loadFromDatabase(k: String): WarpPoint? {
        loadStm.setString(1, k)
        loadStm.executeQuery().use { rs ->
            if (rs.next()) {
                val l = Location(
                    CroissantSys.instance.server.getWorld(rs.getString("w")),
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch")
                )
                val warpPoint = WarpPoint(k,  rs.getString("name"), l)
                datum[k] = warpPoint
                return warpPoint
            }
        }
        return null
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