package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.StatusType
import net.kyori.adventure.text.Component
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class Wearing(private val uuid: String) {
    private val wearing: MutableMap<BodyPart, Equipment?> = EnumMap(BodyPart::class.java)
    init {
        val file = File(CroissantSys.instance.dataFolder, "userdata.yml")
        val config = YamlConfiguration.loadConfiguration(file)
        BodyPart.entries.forEach {
            wearing[it] = EquipmentManager.instance.load(config.getInt("$uuid.wearing.${it.name}"))
        }
    }

    fun get(): Collection<Equipment?> {
        return wearing.values
    }

    fun get(bodyPart: BodyPart): Equipment? {
        return wearing[bodyPart]
    }

    fun reloadWearing() {
        BodyPart.entries.forEach {
            val w = wearing[it]
            if (w != null)
                wearing[it] = EquipmentManager.instance.load(w.id)
        }
    }

    fun getWearingStatus(): Map<StatusType, Double> {
        val baseStatus: MutableMap<StatusType, Double> = EnumMap(StatusType::class.java)
        wearing.values.filterNotNull().forEach {
            for ((type, volume) in it.getStatus()) {
                baseStatus[type] = (baseStatus[type]?:0.0) + volume
            }
        }
        return baseStatus
    }


    fun saveWearing() {
        val f = File(CroissantSys.instance.dataFolder, "userdata.yml")
        val c = YamlConfiguration.loadConfiguration(f)

        for ((bodyPart, equipment) in wearing) {
            equipment?.save()
            c.set("$uuid.wearing.${bodyPart.name}", equipment?.id)
        }
        c.save(f)
    }

    fun setWearing(bodyPart: BodyPart, equipment: Equipment): Boolean {
        if (equipment.data.bodyPart != bodyPart)
            return false
        wearing[bodyPart] = equipment
        return true
    }

    fun setWearing(equipment: Equipment) {
        wearing[equipment.data.bodyPart] = equipment
    }

    fun clearWearing() {
        wearing.clear()
    }

    fun getWearingComponent(): Component {
        var component: Component = Component.text("装備: ")
        BodyPart.entries.forEach {
            var comp = Component.text("${it.name}: ")
            val equipment = wearing[it]
            comp =
                if  (equipment != null)   comp.append(equipment.getComponent())
                else                      comp.append(Component.text("[ NONE ]"))
            component = component.append(comp.appendNewline())
        }
        return component
    }
}