package com.github.sheauoian.croissantsys.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(val material: Material, customModel: Int = 0) {
    val item = ItemStack(material)
    val meta: ItemMeta = item.itemMeta
    init {
        meta.setCustomModelData(customModel)
    }
    fun build(glow: Boolean = false): ItemStack {
        val meta = meta.clone()
        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        item.setItemMeta(meta)
        return item
    }

    fun displayName(name: Component): ItemBuilder {
        meta.displayName(name.decoration(TextDecoration.ITALIC, false))
        return this
    }

    fun displayName(name: String): ItemBuilder {
        return displayName(MiniMessage.miniMessage().deserialize(name))
    }

    fun lore(lore: List<String>): ItemBuilder {
        meta.lore(
            lore.map { MiniMessage.miniMessage().deserialize(it).decoration(TextDecoration.ITALIC, false) }
        )
        return this
    }

    fun lore(lore: String): ItemBuilder {
        return lore(lore.split("\n"))
    }
}