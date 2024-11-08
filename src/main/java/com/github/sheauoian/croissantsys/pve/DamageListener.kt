package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.user.UserDataManager
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class DamageListener: Listener {
    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e is EntityDamageByEntityEvent) {
            val victim = e.entity
            val attacker = (e.damager as? Projectile)?.shooter as? Entity ?: e.damager // Projectile

            if (victim is Player) {
                if (attacker is Player) {
                    // PvP の禁止
                    e.isCancelled = true
                    return
                }
                UserDataManager.instance.get(victim)?.let {
                    e.damage = it.getReceiveDamage(e.damage)
                }
            }
            else if (attacker is Player) {
                UserDataManager.instance.get(attacker)?.let {
                    e.damage = it.getInflictDamage(e.damage)
                }
            }
        }
    }
}