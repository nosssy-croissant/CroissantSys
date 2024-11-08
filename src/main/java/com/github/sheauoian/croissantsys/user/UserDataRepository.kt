package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID

class UserDataRepository(con: Connection) {
    private val loadStm: PreparedStatement
    private val saveStm: PreparedStatement
    private val insertStm: PreparedStatement

    init {
        con.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS users(
                uuid        TEXT        PRIMARY KEY,
                money       INTEGER     DEFAULT $DEFAULT_MONEY,
                health      REAL        DEFAULT $DEFAULT_HEALTH,
                max_health  REAL        DEFAULT $DEFAULT_MAX_HEALTH
            )
        """.trimIndent())

        loadStm = con.prepareStatement("""
            SELECT * FROM users WHERE uuid = ?
        """.trimIndent())

        saveStm = con.prepareStatement("""
            INSERT INTO
                users   (uuid, money, health, max_health)
                VALUES  (?, ?, ?, ?)
            ON CONFLICT(uuid)
                DO UPDATE SET
                    money=excluded.money,
                    health=excluded.health,
                    max_health=excluded.max_health
        """.trimIndent())

        insertStm = con.prepareStatement("""
            INSERT INTO users (uuid) VALUES (?)
        """.trimIndent())
    }

    fun load(uuid: UUID): UserData? {
        return try {
            loadStm.setString(1, uuid.toString())
            loadStm.executeQuery().use { rs ->
                if (rs.next()) {
                    UserData(
                        uuid,
                        rs.getInt("money"),
                        rs.getDouble("health"),
                        rs.getDouble("max_health")
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun save(userData: UserData) {
        try {
            saveStm.setString(1, userData.uuid.toString())
            saveStm.setInt(2, userData.money)
            saveStm.setDouble(3, userData.health)
            saveStm.setDouble(4, userData.maxHealth)
            saveStm.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun load(player: Player): UserDataOnline {
        loadStm.setString(1, player.uniqueId.toString())
        val rs = loadStm.executeQuery()
        if (!rs.next()) {
            insert(player.uniqueId)
            return UserDataOnline(
                player,
                DEFAULT_MONEY,
                DEFAULT_HEALTH,
                DEFAULT_MAX_HEALTH
            )
        }

        return UserDataOnline(
            player,
            rs.getInt("money"),
            rs.getDouble("health"),
            rs.getDouble("max_health")
        )
    }

    fun insert(uuid: UUID) {
        insertStm.setString(1, uuid.toString())
        insertStm.executeUpdate()
    }
}