package com.github.sheauoian.croissantsys.item

import de.tr7zw.nbtapi.NBT
import org.bukkit.inventory.ItemStack

class Sellable(val itemStack: ItemStack) {
    val sellPrice: Int
    init {
        NBT.modify(itemStack) {
             it.getOrCreateCompound("sellable")
        }
        val nbt = NBT.readNbt(itemStack).getCompound("sellable")
        sellPrice = nbt?.getInteger("price") ?: 0
    }

    fun setPrice(price: Int) {
        NBT.modify(itemStack) {
            it.getOrCreateCompound("sellable").setInteger("price", price)
        }
    }

    fun removePrice() {
        NBT.modify(itemStack) {
            it.removeKey("sellable")
        }
    }

    fun getPrice(): Int {
        return sellPrice
    }

    fun hasPrice(): Boolean {
        return sellPrice != 0
    }

    fun getItem(): ItemStack {
        return itemStack
    }
}