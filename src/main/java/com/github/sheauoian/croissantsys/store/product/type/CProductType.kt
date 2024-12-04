package com.github.sheauoian.croissantsys.store.product.type

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.Serializable

@Serializable
sealed interface CProductType {
    fun canPurchase(user: UserDataOnline): Boolean { return true }
    fun purchase(user: UserDataOnline)
}