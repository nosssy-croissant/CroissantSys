package com.github.sheauoian.croissantsys.store.price

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import net.kyori.adventure.text.Component

@Serializable
sealed interface PriceType {
    companion object {
        val module = SerializersModule {
            polymorphic(PriceType::class) {
                subclass(MoneyPriceType.serializer())
                subclass(LevelPriceType.serializer())
                subclass(ItemPriceType.serializer())
            }
        }
    }

    fun canPurchase(user: UserDataOnline): Boolean
    fun purchase(user: UserDataOnline)
    fun getFailMessage(user: UserDataOnline): Component
    fun getLore(user: UserDataOnline): Component
}