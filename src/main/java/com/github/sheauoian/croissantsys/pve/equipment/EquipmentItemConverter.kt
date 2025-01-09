package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.util.status.Status
import com.github.sheauoian.croissantsys.util.status.StatusType
import de.tr7zw.nbtapi.NBT
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.inventory.ItemStack

class EquipmentItemConverter {
    fun convertToEquipment(item: ItemStack): EquipmentBasic? {
        val nbtItem = NBT.itemStackToNBT(item) ?: return null
        val nbt = nbtItem.getCompound("equipment") ?: return null

        val dataId = nbt.getString("data_id")
        val data = EDataManager.get(dataId) ?: return null

        val level = nbt.getInteger("level")
        val rarity = nbt.getInteger("rarity")

        val subStatusCompound = nbt.getOrCreateCompound("sub_status")
        val subStatus: MutableList<Status> = mutableListOf()
        subStatusCompound.keys.forEach {
            val type = StatusType.valueOf(it)
            val volume = subStatusCompound.getDouble(it)
            subStatus.add(Status(volume, type))
        }

        val basic = EquipmentBasic(rarity, level, subStatus, data)
        val id = nbt.getOrNull<Int>("id", Int::class.java) ?: return basic
        return Equipment(id, data, level, rarity, subStatus)
    }
}