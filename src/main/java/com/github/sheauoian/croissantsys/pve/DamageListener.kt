package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import de.tr7zw.nbtapi.NBTItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

class DamageListener: Listener {
    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e is EntityDamageByEntityEvent) {
            val victim = e.entity
            val attacker = (e.damager as? Projectile)?.shooter as? Entity ?: e.damager // Projectile

            if (victim is Player) {
                if (attacker is Player) {
                    e.isCancelled = true
                    return
                }
                UserDataManager.instance.get(victim).let {
                    e.damage = it.getReceiveDamage(e.damage)
                }
            }
            else if (attacker is Player) {
                UserDataManager.instance.get(attacker).let { user ->
                    if (attacker.inventory.itemInMainHand.isEmpty) {
                        e.damage = 1.0
                        return@let
                    }
                    val nbt = NBTItem(attacker.inventory.itemInMainHand).getCompound("equipment")
                    if (nbt == null) {
                        e.damage = 1.0
                        return@let
                    }
                    else if (nbt.getInteger("id") != user.wearing.getId(BodyPart.MainHand)) {
                        val equipment = user.eManager.get(nbt.getInteger("id"))
                        if (equipment == null || equipment.data.bodyPart != BodyPart.MainHand) {
                            e.damage = 1.0
                            return@let
                        }
                        user.wearing.setWearing(BodyPart.MainHand, equipment)
                        attacker.sendMessage(Component.text("使用武器を変更しました")
                            .color(TextColor.color(200, 10, 50)))
                        user.update()
                    }
                    e.damage = user.getInflictDamage(e.damage)
                }
            }
        }
    }

    @EventHandler
    fun onKill(e: EntityDeathEvent) {
        val entity = e.entity
        val killer = entity.killer
        if (killer is Player) {
            UserDataManager.instance.get(killer).let {
                it.addExp(10)
                it.update()
            }
        }
    }
}