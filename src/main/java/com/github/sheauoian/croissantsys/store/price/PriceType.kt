package com.github.sheauoian.croissantsys.store.price

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
sealed interface PriceType {
    fun canPurchase(user: UserDataOnline): Boolean
    fun purchase(user: UserDataOnline)
    fun getFailMessage(user: UserDataOnline): Component
    fun getLore(user: UserDataOnline): Component
}