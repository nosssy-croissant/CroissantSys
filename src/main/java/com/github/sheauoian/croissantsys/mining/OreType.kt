package com.github.sheauoian.croissantsys.mining

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class OreType(
    val oreMaterial: Material,
    val oreReplaceMaterial: Material,
    val dropMaterial: ItemStack,
    val regenerateTimeSecond: Int = 20,
    val dropAmount: Int = 1,
    val dropExp: Int = 0,
) {
    Coal(
        Material.COAL_ORE,
        Material.STONE,
        ItemStack(Material.COAL),
        20,
        1,
        0,
    ),
    Iron(
        Material.IRON_ORE,
        Material.STONE,
        ItemStack(Material.IRON_INGOT),
        20,
        1,
        0,
    ),
}