package com.github.sheauoian.croissantsys.store.price

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

@Serializable
@SerialName("LevelPriceType")
class LevelPriceType(val level: Int): PriceType {
    override fun canPurchase(user: UserDataOnline): Boolean {
        return user.level >= level
    }

    override fun purchase(user: UserDataOnline) {
        user.level -= level
    }

    override fun getFailMessage(user: UserDataOnline): Component {
        var message = Component.empty()
        message = message.append(MiniMessage.miniMessage().deserialize("レベルが足りません！ あと"))
        message = message.append(Component.text(level - user.level))
        message = message.append(MiniMessage.miniMessage().deserialize("$ 必要です"))
        return message
    }

    override fun getLore(user: UserDataOnline): Component {
        return MiniMessage.miniMessage().deserialize("<!italic><color:white>必要レベル: <color:cyan><b>$level</b></color>")
    }
}