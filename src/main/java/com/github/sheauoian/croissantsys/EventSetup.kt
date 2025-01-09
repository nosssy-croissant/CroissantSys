package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.listener.ElevatorListener
import com.github.sheauoian.croissantsys.mining.MiningListener
import com.github.sheauoian.croissantsys.pve.DamageListener
import com.github.sheauoian.croissantsys.pve.equipment.listener.EquipmentStoringListener
import com.github.sheauoian.croissantsys.pve.equipment.weapon.WeaponListener
import com.github.sheauoian.croissantsys.pve.mob.CMobListener
import com.github.sheauoian.croissantsys.user.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.user.listener.SkillListener
import com.github.sheauoian.croissantsys.world.listener.HologramListener
import org.bukkit.Bukkit

object EventSetup {
    fun setup(plugin: CroissantSys) {
        val manager = Bukkit.getPluginManager()
        manager.registerEvents(PlayerJoinListener(), plugin)
        manager.registerEvents(DamageListener(), plugin)
        manager.registerEvents(WeaponListener(), plugin)
        manager.registerEvents(HologramListener(), plugin)
        manager.registerEvents(EquipmentStoringListener(), plugin)
        manager.registerEvents(SkillListener(), plugin)
        manager.registerEvents(ElevatorListener(), plugin)
        manager.registerEvents(MiningListener(), plugin)
        manager.registerEvents(CMobListener(), plugin)
    }
}