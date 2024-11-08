package com.github.sheauoian.croissantsys.pve.equipment.data

import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.MainStatus
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class EquipmentData(
    val id: String,
    val name: Component,
    val bodyPart: BodyPart,
    val material: Material,
    val mainStatus: MainStatus
) {
    val item: ItemStack
        get() {
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.displayName(name)
            item.setItemMeta(meta)
            return item
        }

    override fun toString(): String {
        return "EquipmentData(id='$id', name=$name, bodyPart=$bodyPart, material=$material, mainStatus=$mainStatus)"
    }
}