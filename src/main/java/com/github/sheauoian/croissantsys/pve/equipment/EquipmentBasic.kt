package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.util.status.Status
import com.github.sheauoian.croissantsys.util.status.StatusType
import de.tr7zw.nbtapi.NBT
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.ComponentBuilderApplicable
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

open class EquipmentBasic(
    open val rarity: Int,
    open val level: Int,
    open val subStatus: List<Status>,
    open val data: EquipmentData
) {
    companion object {
        fun generate(data: EquipmentData): EquipmentBasic {
            val level = 0
            val rarity = Random().nextInt(0, 12000)
            val sub: MutableList<Status> = ArrayList()
            var i = 10
            while (rarity / i >= 1) {
                sub.add(Status.generate(sub))
                i *= 10
            }
            return EquipmentBasic(rarity, level, sub, data)
        }
    }

    private fun basicLore(): List<Component> {
        val lore: MutableList<Component> = mutableListOf()

        lore.add(LegacyComponentSerializer.legacy('&')
            .deserialize("&#ddaaaa [ メインステータス ]"))
        lore.add(LegacyComponentSerializer.legacy('&')
            .deserialize("&7 ≫ &f&l${data.mainStatus.type.displayName} &f: ${"%.2f".format(data.mainStatus.getVolumeFromLevel(level))}"))

        lore.add(Component.empty())
        lore.add(LegacyComponentSerializer.legacy('&')
            .deserialize("&#cccc99 [ サブステータス ]"))
        subStatus.forEach { status ->
            lore.add(LegacyComponentSerializer.legacy('&')
                .deserialize("&#cccc99 - ${status.type.displayName} &#cccc99: ${"%.2f".format(status.volume)}"))
        }
        lore.add(Component.empty())
        lore.add(LegacyComponentSerializer.legacy('&')
            .deserialize("&7 [ &fレアリティ : $rarity ]"))
        return lore.map {it.decoration(TextDecoration.ITALIC, false)}
    }

    open fun getItem(): ItemStack {
        val item = data.item
        NBT.modify(item) {
            it.getOrCreateCompound("equipment").let { nbt ->
                nbt.setInteger("level", level)
                nbt.setInteger("rarity", rarity)
                subStatus.forEach { status ->
                    nbt.getOrCreateCompound("sub_status").setDouble(status.type.name, status.volume)
                }
                nbt.setString("data_id", data.id)
            }
        }
        val meta = item.itemMeta
        meta.displayName(
            data.name.append(
                Component.text(" +$level")
            )
        )
        meta.lore(basicLore())
        item.itemMeta = meta
        return item
    }

    fun getStatus(): Map<StatusType, Double> {
        val map: MutableMap<StatusType, Double> = mutableMapOf()
        map[data.mainStatus.type] = data.mainStatus.getVolumeFromLevel(level)
        subStatus.forEach {
            if (map[it.type] != null)
                map[it.type] = map[it.type]!! + it.volume
            else
                map[it.type] = it.volume
        }
        return map
    }
}