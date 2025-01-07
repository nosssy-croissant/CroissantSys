package com.github.sheauoian.croissantsys.pve.mob

import io.typst.bukkit.kotlin.serialization.ItemStackSerializable
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack

@Serializable
class CMobDrop(
    val itemStack: ItemStackSerializable,
    val chance: Double,
    val min: Int,
    val max: Int
) {
    fun drop(): ItemStack? {
        return if (Math.random() < chance) {
            val item = itemStack.clone()
            item.amount = (min..max).random()
            item
        } else {
            null
        }
    }
}