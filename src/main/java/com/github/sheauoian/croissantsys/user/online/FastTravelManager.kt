package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import java.sql.PreparedStatement

class FastTravelManager {
    companion object {
        val instance = FastTravelManager()
    }


    private val loadStm: PreparedStatement
    private val saveStm: PreparedStatement

    init {
        DbDriver.con.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS fast_travel(
                user_uuid       STRING  NOT NULL,
                warp_point_id   STRING  NOT NULL,
                UNIQUE(user_uuid, warp_point_id)
            )
        """.trimIndent())

        loadStm = DbDriver.con.prepareStatement("""
            SELECT warp_point_id FROM fast_travel WHERE user_uuid = ?
        """.trimIndent())

        saveStm = DbDriver.con.prepareStatement("""
            INSERT INTO fast_travel(user_uuid, warp_point_id) VALUES(?, ?)
        """.trimIndent())
    }

    fun save(uuid: String, w: Collection<WarpPoint>) {
        saveStm.setString(1, uuid)
        w.forEach {
            saveStm.setString(2, it.id)
            saveStm.executeUpdate()
        }
    }

    fun reload(v: FastTravel) {
        loadStm.setString(1, v.uuid)
        val rs = loadStm.executeQuery()
        while (rs.next()) {
            WarpPointManager.find(rs.getString(1))?.let {
                v.add(it)
            }
        }
    }
}