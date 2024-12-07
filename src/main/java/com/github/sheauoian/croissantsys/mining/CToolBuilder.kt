package com.github.sheauoian.croissantsys.mining

import de.tr7zw.nbtapi.NBT
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class CToolBuilder(val item: ItemStack) {
    val breakable: MutableSet<Material> = mutableSetOf()
    var efficiency: Int = item.itemMeta.getEnchantLevel(Enchantment.EFFICIENCY)
    init {
        NBT.modifyComponents(item) { nbt ->
            val predicates = nbt.getCompound("minecraft:can_break")?.getCompoundList("predicates")
            predicates?.forEach { predicate ->
                val material = Material.matchMaterial(predicate.getString("blocks"))
                if (material != null) {
                    breakable.add(material)
                }
            }
        }
    }

    fun build(): ItemStack {
        val meta = item.itemMeta
        ItemFlag.entries.forEach(meta::addItemFlags)

        val lore: MutableList<Component> = mutableListOf()
        lore.addAll(breakableLore())

        meta.lore(lore)
        item.itemMeta = meta

        NBT.modifyComponents(item) { nbt ->
            val comp = nbt.getOrCreateCompound("minecraft:can_break")
            val predicates = comp.getCompoundList("predicates")
            predicates.clear()
            breakable.forEach { material ->
                val predicate = predicates.addCompound()
                predicate.setString("blocks", material.key.toString())
            }
        }
        return item
    }

    fun addBreakable(material: Material): CToolBuilder {
        breakable.add(material)
        return this
    }
    fun removeBreakable(material: Material): CToolBuilder {
        breakable.remove(material)
        return this
    }
    private fun breakableLore(): List<Component> {
        val lore = mutableListOf<Component>()
        lore.add(Component.text("[ 破壊可能 ]").color(TextColor.color(0xaaff33)).decoration(
            TextDecoration.ITALIC, false
        ))
        breakable.forEach { material ->
            lore.add(Component.translatable(material).color(TextColor.color(0xffffff)).decoration(
                TextDecoration.ITALIC, false
            ))
        }
        return lore
    }
}