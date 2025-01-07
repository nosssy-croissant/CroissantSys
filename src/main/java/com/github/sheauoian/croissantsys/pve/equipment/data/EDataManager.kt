package com.github.sheauoian.croissantsys.pve.equipment.data

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.pve.skill.Skill
import com.github.sheauoian.croissantsys.pve.skill.SkillManager
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.MainStatus
import com.github.sheauoian.croissantsys.util.status.StatusType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class EDataManager {
    companion object {
        val instance = EDataManager()

        private fun getFile(): File {
            return File(CroissantSys.instance.dataFolder, "equipment_data.yml")
        }
        private fun getConfig(f: File): FileConfiguration {
            return YamlConfiguration.loadConfiguration(f)
        }
    }
    private var datum: MutableMap<String, EquipmentData> = mutableMapOf()
    private val serializer = MiniMessage.miniMessage()

    fun reload() {
        datum.clear()
        val conf = YamlConfiguration.loadConfiguration(
            File(CroissantSys.instance.dataFolder, "equipment_data.yml")
        )
        for (id in conf.getKeys(false)) {
            loadFromDatabase(id)?.let {
                datum[id] = it
            }
        }
        UserDataManager.instance.getAll().forEach {
            it.wearing.reloadWearing()
        }
    }


    fun load(k: String): EquipmentData? {
        if (datum.containsKey(k))
            return datum[k]
        return loadFromDatabase(k)
    }

    private fun loadFromDatabase(k: String): EquipmentData? {
        return loadFromDatabase(k, YamlConfiguration.loadConfiguration(
            File(CroissantSys.instance.dataFolder, "equipment_data.yml")
        ))
    }

    fun removeData(k: String): Boolean {
        if (!datum.containsKey(k)) return false
        datum.remove(k)
        val file = getFile()
        val conf = getConfig(file)
        conf.set(k, null)
        conf.save(file)
        return true
    }

    private fun loadFromDatabase(k: String, conf: FileConfiguration): EquipmentData? {
        val name = conf.getComponent("$k.name", serializer)
            ?: return null
        val bodyPart = BodyPart.valueOf(conf.getString("$k.body_part") ?: "MainHand")
        val material = Material.valueOf(conf.getString("$k.material") ?: bodyPart.material.name)
        val baseStatusTypeId = conf.getString("$k.main_status.type", "STR")
        val mainStatus = MainStatus(
            conf.getDouble("$k.main_status.volume", 10.0),
            conf.getDouble("$k.main_status.slope", 1.0),
            StatusType.valueOfOrNull(baseStatusTypeId) ?: StatusType.STR
        )
        val skills: List<Skill> = conf.getStringList("$k.skills").map {
            SkillManager.instance.getSkill(it) ?: return@map null
        }.filterNotNull()



        return EquipmentData(k, name.decoration(TextDecoration.ITALIC, false), bodyPart, material, mainStatus, skills)
    }

    fun save(v: EquipmentData) {
        val file = getFile()
        val conf = getConfig(file)
        setToConfig(v, conf)
        conf.save(file)
    }
    fun saveAll() {
        val file = getFile()
        val conf = getConfig(file)
        datum.values.forEach {
            setToConfig(it, conf)
        }
        // Save File
        conf.save(file)
    }

    private fun setToConfig(data: EquipmentData, conf: FileConfiguration) {
        val id = data.id
        conf.setComponent("$id.name", serializer, data.name)
        conf.set("$id.body_part", data.bodyPart.name)
        conf.set("$id.material", data.material.name)

        conf.set("$id.main_status.volume", data.mainStatus.volume)
        conf.set("$id.main_status.slope", data.mainStatus.slope)
        conf.set("$id.main_status.type", data.mainStatus.type.name)
    }


    fun get(id: String): EquipmentData? {
        return datum[id]
    }

    fun getIds(): List<String> {
        return datum.keys.toList()
    }

    fun getAll(): List<EquipmentData> {
        return datum.values.toList()
    }

    fun addInitialData(dataId: String, optionalBodyPart: BodyPart?): EquipmentData? {
        if (datum.containsKey(dataId)) return null
        val bodyPart = optionalBodyPart ?: BodyPart.MainHand
        val data = EquipmentData(
            dataId,
            Component.text(dataId),
            bodyPart,
            bodyPart.material,
            MainStatus(10.0, 1.0, StatusType.STR),
            listOf()
        )
        datum[dataId] = data
        save(data)
        return data
    }
}