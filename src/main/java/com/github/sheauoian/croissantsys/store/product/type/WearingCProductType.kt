package com.github.sheauoian.croissantsys.store.product.type

import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("WearingCProduct")
class WearingCProductType(val dataId: String): CProductType {
    override fun canPurchase(user: UserDataOnline): Boolean {
        val data = EDataManager.instance.get(dataId)
        if (data == null) {
            CStore.sendMessage(user, "商品が見つかりません！")
            return false
        }
        val equipment = EquipmentManager.instance.generate(data, user)
        if (user.canAddItem(equipment.getItem())) {
            return true
        } else {
            CStore.sendMessage(user, "インベントリがいっぱいです！")
            return false
        }
    }

    override fun purchase(user: UserDataOnline) {
        val data = EDataManager.instance.get(dataId) ?: return
        val equipment = EquipmentManager.instance.generate(data, user)
        if (user.canAddItem(equipment.getItem())) {
            user.addItem(equipment.getItem())
        }
    }
}