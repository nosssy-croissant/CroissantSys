package com.github.sheauoian.croissantsys.user.listener

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class SkillListener: Listener {
    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (!e.action.name.contains("RIGHT_CLICK")) return
        if (e.player.inventory.itemInMainHand.isEmpty) return

        val user = UserDataManager.instance.get(e.player)
        val equipment = Equipment.fromItem(e.player.inventory.itemInMainHand)

        if (equipment != null && equipment.id == user.wearing.getId(BodyPart.MainHand)){
            e.player.sendMessage("スキルを使いました")
            UserDataManager.instance.get(e.player).useSkill()
        }
    }
}