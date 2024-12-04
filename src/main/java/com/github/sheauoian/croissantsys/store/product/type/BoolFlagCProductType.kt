package com.github.sheauoian.croissantsys.store.product.type

import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FlagCProduct")
class BoolFlagCProductType(val id: String): CProductType {
    override fun canPurchase(user: UserDataOnline): Boolean {
        if (user.getFlag(id) as? Boolean != true) {
            return true
        } else {
            CStore.sendMessage(user, "既に購入済みです！")
            return false
        }
    }

    override fun purchase(user: UserDataOnline) {
        user.setFlag(id, true)
    }
}