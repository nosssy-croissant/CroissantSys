package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
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

            // Projectileの場合は Shooter を取得
            // それ以外の場合は Damager を取得
            val attacker = (e.damager as? Projectile)?.shooter as? Entity ?: e.damager

            if (victim is Player) {
                if (attacker is Player) {
                    // プレイヤーがプレイヤーを攻撃した場合
                    e.isCancelled = true
                    return
                }
                // プレイヤーが攻撃された場合
                UserDataManager.instance.get(victim).let {
                    e.damage = it.getReceiveDamage(e.damage)
                }
            }
            else if (attacker is Player) {
                // プレイヤーが攻撃した場合
                e.damage = damageFromPlayerToEntity(attacker, e.damage)
            }
        }
    }

    fun damageFromPlayerToEntity(attacker: Player, damage: Double): Double {
        val attackerUserData = UserDataManager.instance.get(attacker)
        val item = attacker.inventory.itemInMainHand
        if (item.isEmpty) {
            return 1.0
        }
        val id = Equipment.getId(item) ?: return 1.0
        val currentId = attackerUserData.wearing.getId(BodyPart.MainHand)

        if (id != currentId) {
            val equipment = attackerUserData.eManager.get(id)
            if (equipment == null || equipment.data.bodyPart != BodyPart.MainHand) {
                return 1.0
            }
            // 武器を変更
            attackerUserData.wearing.setWearing(BodyPart.MainHand, equipment)
            attacker.sendMessage(
                Component.text("使用武器を変更しました")
                    .color(TextColor.color(200, 10, 50))
            )
            attackerUserData.update()
        }
        return attackerUserData.getInflictDamage(damage)
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