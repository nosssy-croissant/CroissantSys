package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.online.ui.MainMenu
import com.github.sheauoian.croissantsys.user.online.ui.StatusGui
import com.github.sheauoian.croissantsys.user.online.ui.equipment.ELevelUpUI
import com.github.sheauoian.croissantsys.user.online.ui.equipment.EStorageUI
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.pve.skill.Skill
import com.github.sheauoian.croissantsys.util.Formula
import com.github.sheauoian.croissantsys.util.status.StatusType
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import com.github.stefvanschie.inventoryframework.gui.type.util.NamedGui
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import java.util.*

class UserDataOnline
    (val player: Player, override val money: Int, override val health: Double, override val maxHealth: Double):
    UserData(player.uniqueId, money, health, maxHealth),
    DamageLayer
{
    val eManager: EquipmentStorage = EquipmentStorage(this)
    private val fastTravel: FastTravel = FastTravel(this.uuid.toString())

    val skill: Skill = Skill(
        listOf(
            "半径10ブロックに",
            Formula("STR × 10") { it.baseStatus[StatusType.STR]?.times(10) ?: 1.0 },
            "のダメージを与える"),
        mapOf()
    ) {
        player.world.entities.forEach {
            if (it.location.distance(player.location) <= 10.0) {
                (it as? Mob)?.damage((baseStatus[StatusType.STR] ?: 1.0) * 10.0)
            }
        }
    }
        get() {
            return field
        }

    fun useSkill() {
        skill.use(this)
    }

    override fun save() {
        super.save()
        eManager.save()
        fastTravel.save()
    }

    fun openMenu() {
        MainMenu(this).open()
    }

    fun openGui(gui: NamedGui) {
        gui.show(player)
    }
    // Status
    fun openStatusMenu() {
        StatusGui(this).show(player)
    }

    fun openEStorage(bodyPart: BodyPart?) {
        EStorageUI(this, bodyPart).show(player)
    }
    fun openELevelingStorage(bodyPart: BodyPart?) {
        EStorageUI(this, bodyPart, true).show(player)
    }

    fun openELeveling(equipment: Equipment?) {
        val ui = ELevelUpUI(this)
        ui.setEquipment(equipment)
        ui.show(player)
    }

    fun update() {
        updateStatus()
        val jump = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)
        if (jump != null) {
            jump.baseValue = 0.5
        }
        player.sendActionBar(Component.text("$health / $maxHealth | ${money}\$"))
    }

    fun warp(id: String) {
        val warpPoint = WarpPointManager.instance.find(id)
        if (warpPoint != null) {
            player.teleport(warpPoint.location)
        } else {
            player.sendMessage(
                Component.text("そのワープポイントは存在しません")
                    .color(TextColor.color(0xe0bbaa)))
        }
    }

    override fun getInflictDamage(d: Double): Double {
        val isCritical = Random().nextDouble() <= (baseStatus[StatusType.CRITICAL_RATE] ?: 0.0)

        val damage = d * (baseStatus[StatusType.STR] ?: 1.0) * (if(isCritical) 100 else 1)
        player.sendMessage(Component.text(damage).color(TextColor.color(0xccaa88)))
        return damage
    }

    override fun getReceiveDamage(d: Double): Double {
        return d / (baseStatus[StatusType.DEF] ?: 1.0)
    }
}