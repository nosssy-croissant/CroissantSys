package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.Status

class EquipmentStorage(val user: UserDataOnline) {
    private val datum: MutableMap<Int, Equipment> = HashMap()

    fun get(id: Int): Equipment? {
        if (datum[id] == null) {
            val equipment = EquipmentManager.instance.load(id)
            if (equipment != null) {
                datum[id] = equipment
            }
        }
        return datum[id]
    }

    private fun load(equipment: Equipment, force: Boolean) {
        if (force || !datum.containsKey(equipment.id)) datum[equipment.id] = equipment
    }

    fun load(equipment: Equipment) {
        load(equipment, false)
    }

    fun getAll(bodyPart: BodyPart?): Collection<Equipment> {
        return getAll(bodyPart, false)
    }

    private fun getAll(bodyPart: BodyPart?, load: Boolean): Collection<Equipment> {
        if (!load) {
            val list = EquipmentManager.instance.loadUserEquipments(user.uuid.toString(), bodyPart).toMutableList()
            for ((i, e) in list.withIndex()) {
                if (datum.containsKey(e.id)) {
                    list[i] = datum[e.id]!!
                }
            }
            return list
        }

        EquipmentManager.instance.loadUserEquipments(user.uuid.toString(), bodyPart).forEach {
            load(it)
        }
        return datum.values
    }


    fun save() {
        datum.values.forEach {
            it.save()
        }
    }

    fun levelUp(equipment: Equipment): Equipment {
        load(equipment)
        equipment.levelUp()
        return equipment
    }

    fun addSubStatus(equipment: Equipment, status: Status) {
        load(equipment)
        equipment.addSubStatus(status)
    }
}