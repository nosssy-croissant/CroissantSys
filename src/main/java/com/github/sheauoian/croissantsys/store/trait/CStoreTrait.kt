package com.github.sheauoian.croissantsys.store.trait

import com.github.sheauoian.croissantsys.store.CStoreManager
import com.github.sheauoian.croissantsys.user.UserDataManager
import net.citizensnpcs.api.event.NPCRightClickEvent
import net.citizensnpcs.api.persistence.Persist
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import org.bukkit.event.EventHandler


@TraitName("croissant_store")
class CStoreTrait: Trait("croissant_store") {
    @Persist var storeId: String = ""
    val store get() = CStoreManager.instance.stores.find { it.id == storeId }

    @EventHandler
    fun click(e: NPCRightClickEvent) {
        if (e.npc != npc) return
        val user = UserDataManager.instance.get(e.clicker)
        store?.open(user)
    }
}