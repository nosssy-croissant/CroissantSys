package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.util.status.Status
import de.tr7zw.nbtapi.NBT
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

class Equipment(
    val id: Int,
    override val data: EquipmentData,
    override var level: Int,
    override val rarity: Int,
    override var subStatus: List<Status>
) : EquipmentBasic(rarity, level, subStatus, data){
    constructor(id: Int, data: EquipmentData, level: Int, rarity: Int, subStatusStr: String):
            this(id, data, level, rarity, Json.decodeFromString<List<Status>>(subStatusStr))

    fun save() {
        EquipmentManager.instance.save(this)
    }

    override fun getItem(): ItemStack {
        val item = super.getItem()
        NBT.modify(item) {
            it.getOrCreateCompound("equipment").setInteger("id", id)
        }
        return item
    }

    fun levelUp() {
        level += 1
    }

    fun addSubStatus(status: Status) {
        val list = subStatus.toMutableList()
        list.add(status)
        subStatus = list
    }

    fun getComponent(): Component {
        return data.name.append(Component.text(toString()))
    }

    override fun toString(): String {
        return "Equipment{UniqueId: ${id}, EquipmentId: ${data.id}}"
    }
}