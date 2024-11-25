package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.Manager
import com.github.sheauoian.croissantsys.util.status.Status
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.inventory.ItemStack
import java.sql.PreparedStatement
import kotlin.collections.ArrayList

class EquipmentManager: Manager<Int, Equipment>() {
    companion object {
        val instance = EquipmentManager()
    }
    private val converter = EquipmentItemConverter()
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

    /*
    fun getNextId(): Int {
        val rs = con.createStatement().executeQuery("SELECT MAX(id) FROM equipments")
        return if (rs.next()) rs.getInt(1) + 1 else 1
    }
    */

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

    fun checkAndLoad(id: Int, uuid: String): Equipment? {
        return if (checkIdAndUUID(id, uuid)) {
            load(id)
        } else {
            null
        }
    }


    override fun save(v: Equipment) {
        saveStm.setInt(1, v.level)
        saveStm.setString(2, Json.encodeToString(v.subStatus))
        saveStm.setInt(3, v.id)
        saveStm.executeUpdate()
    }

    // id に紐づけられたUUID が正しいかどうかのチェック
    private fun checkIdAndUUID(id: Int, uuid: String): Boolean {
        val rs = con.createStatement().executeQuery("SELECT (user_uuid) FROM equipments WHERE id = $id")
        return if (rs.next()) {
            rs.getString(1) == uuid
        } else {
            false
        }
    }

    fun store(item: ItemStack, uuid: String){
        converter.convertToEquipment(item)?.let {
            if (it !is Equipment) {
                insert(it.data.id, uuid, it.rarity, it.subStatus)
            }
        }
    }

    private fun insert(dataId: String, uuid: String, rarity: Int, sub: List<Status>): Equipment {
        insertStm.setString(1, dataId)
        insertStm.setString(2, uuid)
        insertStm.setInt(3, rarity)
        insertStm.setString(4, Json.encodeToString(sub))
        insertStm.executeUpdate()
        val rs = insertStm.generatedKeys
        rs.next()
        return Equipment(rs.getInt(1), EDataManager.instance.get(dataId)!!, 0, rarity, sub)
    }

    fun generate(data: EquipmentData, user: UserData): Equipment {
        return generate(data, user.uuid.toString())
    }

    fun generate(data: EquipmentData, userUuid: String): Equipment {
        val basic = EquipmentBasic.generate(data)
        return insert(data.id, userUuid, basic.rarity, basic.subStatus)
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