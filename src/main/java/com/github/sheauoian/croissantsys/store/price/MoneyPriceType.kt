package com.github.sheauoian.croissantsys.store.price

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

@Serializable
@SerialName("MoneyPriceType")
class MoneyPriceType(val money: Int) : PriceType {
    override fun canPurchase(user: UserDataOnline): Boolean {
        return user.money >= money
    }

    override fun purchase(user: UserDataOnline) {
        user.money -= money
    }

    override fun getFailMessage(user: UserDataOnline): Component {
        var message = Component.empty()
        message = message.append(MiniMessage.miniMessage().deserialize("お金が足りません！ あと"))
        message = message.append(Component.text(money - user.money))
        message = message.append(MiniMessage.miniMessage().deserialize("$ 必要です"))
        return message
    }

    override fun getLore(user: UserDataOnline): Component {
        return MiniMessage.miniMessage().deserialize("<!italic><color:white>金額: <color:yellow><b>$money</b></color>")
    }
}