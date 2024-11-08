package com.github.sheauoian.croissantsys.world.warppoint

import com.github.sheauoian.croissantsys.DbDriver

class UnlockedWarpPointRepository(val uuid: String) {
    companion object {
        private val con = DbDriver.con
        init {
            con.createStatement().execute(
                """
            CREATE TABLE IF NOT EXISTS unlocked_warp_points (
                uuid        STRING      NOT NULL,
                warp_id     STRING      NOT NULL,
                PRIMARY KEY (uuid, warp_id)
            )
            """.trimIndent()
            )
        }
    }

    fun save(warpId: String) {
        try {
            con.createStatement().executeUpdate(
                "INSERT INTO unlocked_warp_points (uuid, warp_id) VALUES ('$uuid', '$warpId')"
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun load(): List<String> {
        return con.createStatement().executeQuery(
            "SELECT warp_id FROM unlocked_warp_points WHERE uuid = '$uuid'"
        ).use { rs ->
            val list = mutableListOf<String>()
            while (rs.next()) {
                list.add(rs.getString(1))
            }
            list
        }
    }
}