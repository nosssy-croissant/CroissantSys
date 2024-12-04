package com.github.sheauoian.croissantsys.store.product

import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.store.price.MoneyPriceType
import com.github.sheauoian.croissantsys.store.price.PriceType
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import io.typst.bukkit.kotlin.serialization.ItemStackSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


@Serializable
@SerialName("CProductHolder")
class CProductHolder(val price: List<PriceType>, val iconItemStack: ItemStackSerializable, val product: CProduct) {
    companion object {
        fun createSimple(item: ItemStack, money: Int): CProductHolder {
            val item = item.clone()
            val price = listOf(MoneyPriceType(money))
            val product = ItemCProduct(item)
            return CProductHolder(price, item, product)
        }

        fun createSimple(equipmentData: EquipmentData, money: Int): CProductHolder {
            val price = listOf(MoneyPriceType(money))
            val product = WearingCProduct(equipmentData.id)
            return CProductHolder(price, equipmentData.item, product)
        }
    }

    fun getIcon(user: UserDataOnline): GuiItem {
        val item = iconItemStack.clone()
        val meta = item.itemMeta
        val lore = meta.lore() ?: mutableListOf()

        lore.add(MiniMessage.miniMessage().deserialize("<color:white><b>購入費用: </b>"))
        val mini = MiniMessage.miniMessage().deserialize("<color:white> - ")
        for (priceType in price) {
            lore.add(mini.append(priceType.getLore(user)))
        }
        lore.add(Component.empty())
        if (canPurchase(user)) {
            lore.add(MiniMessage.miniMessage().deserialize("<color:green>購入可能"))
        } else {
            lore.add(MiniMessage.miniMessage().deserialize("<color:red>購入不可"))
        }

        meta.lore(lore)
        item.itemMeta = meta

        return GuiItem(item) { event ->
            val player = event.whoClicked as Player
            val user = UserDataManager.Companion.instance.get(player)
            startPurchase(user)
        }
    }

    fun startPurchase(user: UserDataOnline) {
        val failedPrice = price.filter { !it.canPurchase(user) }
        if (failedPrice.isNotEmpty()) {
            failedPrice.forEach {
                CStore.sendMessage(user, it.getFailMessage(user))
            }
            user.player.closeInventory()
            user.player.playSound(user.player.location, "block.note_block.bass", 1f, 1f)
            return
        }
        if (product.canPurchase(user)) {
            price.forEach { it.purchase(user) }
            product.purchase(user)
            user.player.playSound(user.player.location, "block.note_block.harp", 1f, 1f)
        }
    }

    private fun canPurchase(user: UserDataOnline): Boolean {
        if (!price.all { it.canPurchase(user) }) {
            return false
        }
        return product.canPurchase(user)
    }
}