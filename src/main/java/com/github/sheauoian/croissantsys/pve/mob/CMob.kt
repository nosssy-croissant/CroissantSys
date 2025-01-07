package com.github.sheauoian.croissantsys.pve.mob

import io.lumine.mythic.bukkit.MythicBukkit
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

@Serializable
class CMob(val mythicId: String, val drops: MutableList<CMobDrop>) {
    object CMobManager {
        private val mobs = mutableMapOf<String, CMob>()

        fun get(id: String): CMob? {
            if (MythicBukkit.inst().mobManager.getMythicMob(id) == null) {
                return null
            }
            return mobs[id] ?: CMob(id, mutableListOf()).also {
                mobs[id] = it
            }
        }

        fun saveAll() {
            mobs.values.forEach {
                CMobLoader.save(it)
            }
        }

        fun reload() {
            mobs.clear()
            CMobLoader.loadAll().forEach {
                mobs[it.mythicId] = it
            }
        }
    }

    fun addDrop(drop: CMobDrop) {
        drops.add(drop)
    }

    fun onKilled(player: Player) {
        drops.mapNotNull {
            it.drop()
        }.forEach {
            player.inventory.addItem(it)
            player.sendMessage(
                Component.text("« ").append(
                    it.displayName()
                ).append(
                    Component.text(" × ${it.amount} »")
                )
            )
        }
    }
}