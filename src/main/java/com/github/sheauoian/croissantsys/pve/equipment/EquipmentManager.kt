package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.Manager
import com.github.sheauoian.croissantsys.util.status.Status
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.sql.PreparedStatement
import java.util.*
import kotlin.collections.ArrayList

class EquipmentManager: Manager<Int, Equipment>() {
    companion object {
        val instance = EquipmentManager()
    }

    private var loadStm: PreparedStatement
    private var insertStm: PreparedStatement
    private var loadUserEquipmentsStm: PreparedStatement
    private var saveStm: PreparedStatement
    init {
        con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS equipments(
                    id              INTEGER     PRIMARY KEY,
                    data_id         STRING      NOT NULL,
                    user_uuid       STRING      NOT NULL,
                    level           INTEGER     DEFAULT 0,
                    rarity          INTEGER     DEFAULT 1,
                    sub_status      STRING
                )
            """.trimIndent())

        loadStm = con.prepareStatement("""
                SELECT data_id, level, rarity, sub_status FROM equipments WHERE id = ?
            """.trimIndent())

        insertStm = con.prepareStatement("""
                INSERT INTO 
                    equipments  (data_id, user_uuid, rarity, sub_status) 
                    VALUES      (?, ?, ?, ?);
                SELECT last_insert_rowid();
            """.trimIndent())

        loadUserEquipmentsStm = con.prepareStatement("""
                SELECT id, data_id, level, rarity, sub_status FROM equipments WHERE user_uuid = ?
            """.trimIndent())

        saveStm = con.prepareStatement("""
                UPDATE equipments SET level = ?, sub_status = ? WHERE id = ?
            """.trimIndent())
    }

    override fun load(k: Int): Equipment? {
        loadStm.setInt(1, k)
        val rs = loadStm.executeQuery()

        if (rs.next()) {
            val data = EDataManager.instance.get(rs.getString(1)) ?: return null
            val level = rs.getInt(2)
            val rarity = rs.getInt(3)
            val subStatus: List<Status> = try {
                Json.decodeFromString<List<Status>>(rs.getString(4))
            } catch (e: Exception) {
                listOf()
            }
            return Equipment(k, data, level, rarity, subStatus)
        }
        return null
    }

    override fun save(v: Equipment) {
        saveStm.setInt(1, v.level)
        saveStm.setString(2, Json.encodeToString(v.subStatus))
        saveStm.setInt(3, v.id)
        saveStm.executeUpdate()
    }



    fun generate(data: EquipmentData, userUuid: String): Equipment {
        val rarity = Random().nextInt(0, 12000)
        val sub: MutableList<Status> = ArrayList()
        var i = 10
        while (rarity / i >= 1) {
            sub.add(Status.generate(sub))
            i *= 10
        }
        insertStm.setString(1, data.id)
        insertStm.setString(2, userUuid)
        insertStm.setInt(3, rarity)
        insertStm.setString(4, Json.encodeToString(sub))
        insertStm.executeUpdate()
        val rs = insertStm.generatedKeys
        rs.next()
        val id = rs.getInt(1)
        return Equipment(id, data, 0, rarity, sub)
    }


    fun loadUserEquipments(uuid: String, bodyPart: BodyPart?): List<Equipment> {
        loadUserEquipmentsStm.setString(1, uuid)
        val rs = loadUserEquipmentsStm.executeQuery()
        val list: MutableList<Equipment> = ArrayList()
        while (rs.next()) {
            if (bodyPart == null ||
                bodyPart == EDataManager.instance.get(rs.getString(2))?.bodyPart) {
                val data = EDataManager.instance.get(rs.getString(2)) ?: continue
                list.add(
                    Equipment(
                        rs.getInt(1),
                        data,
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5) ?: "[]"
                    )
                )
            }
        }
        return list
    }
}