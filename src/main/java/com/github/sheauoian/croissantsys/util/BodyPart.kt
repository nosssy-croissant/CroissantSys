package com.github.sheauoian.croissantsys.util

import org.bukkit.Material

enum class BodyPart(val material: Material) {
    MainHand(Material.IRON_SWORD),
    SubHand(Material.SHIELD),
    Head(Material.IRON_HELMET),
    Body(Material.IRON_CHESTPLATE),
    Leg(Material.IRON_LEGGINGS),
    Foot(Material.IRON_BOOTS);
}