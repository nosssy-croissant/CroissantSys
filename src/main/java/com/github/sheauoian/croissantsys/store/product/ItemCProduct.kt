package com.github.sheauoian.croissantsys.store.product

import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import io.typst.bukkit.kotlin.serialization.ItemStackSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Serializable
@SerialName("ItemCProduct")
class ItemCProduct(val item: ItemStackSerializable = ItemStack(Material.PAPER)): CProduct {
    override fun canPurchase(user: UserDataOnline): Boolean {
        if (user.canAddItem(item)) {
            return true
        } else {
            CStore.sendMessage(user, "インベントリがいっぱいです！")
            return false
        }
    }

    override fun purchase(user: UserDataOnline) {
        val item = item.clone()
        if (user.canAddItem(item)) {
            user.addItem(item)
        }
        CStore.sendMessage(user, "購入しました！")
    }
}