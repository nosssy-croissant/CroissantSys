package com.github.sheauoian.croissantsys.store.price

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import io.typst.bukkit.kotlin.serialization.ItemStackSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

@Serializable
@SerialName("ItemPriceType")
class ItemPriceType(val item: ItemStackSerializable) : PriceType {
    override fun canPurchase(user: UserDataOnline): Boolean {
        return user.hasItem(item)
    }

    override fun purchase(user: UserDataOnline) {
        user.removeItem(item)
    }

    override fun getFailMessage(user: UserDataOnline): Component {
        return Component.text("アイテムが足りません！")
    }

    override fun getLore(user: UserDataOnline): Component {
        return MiniMessage.miniMessage().deserialize("<!italic><color:gray>アイテム: ")
            .append(item.displayName())
    }
}