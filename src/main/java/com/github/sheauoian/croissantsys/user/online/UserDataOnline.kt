package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.pve.skill.NormalSkill
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.online.ui.StatusGui
import com.github.sheauoian.croissantsys.user.online.ui.equipment.ELevelUpUI
import com.github.sheauoian.croissantsys.user.online.ui.equipment.EStorageUI
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.StatusType
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import com.github.stefvanschie.inventoryframework.gui.type.util.NamedGui
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*

class UserDataOnline(
    val player: Player,
    override var money: Int,
    override val health: Double,
    override val maxHealth: Double,
    override var level: Int,
    override var exp: Int,
): UserData(player.uniqueId, money, health, maxHealth, level, exp), DamageLayer {
    val eManager: EquipmentStorage = EquipmentStorage(this)
    private val fastTravel: FastTravel = FastTravel(this.uuid.toString())
    private val flags: MutableMap<String, Any> = mutableMapOf()

    init {
        val file: File = File("plugins/CroissantSys/playerdata/${uuid}.yml")
        if (file.exists()) {
            val config = YamlConfiguration.loadConfiguration(file)
            config.getConfigurationSection("flags")?.let {
                it.getKeys(false).forEach { key ->
                    flags[key] = it.get(key) ?: return@forEach
                }
            }
        }
    }

    var skill: NormalSkill? = NormalSkill.skills["test"]

    fun useSkill() {
        skill?.use(this) ?: player.sendMessage("スキルが設定されていません")
    }

    fun sendMiniMessage(message: String) {
        player.sendMessage(
            MiniMessage.miniMessage().deserialize(message)
        )
    }

    override fun save() {
        super.save()
        eManager.save()
        fastTravel.save()
        val file: File = File("plugins/CroissantSys/playerdata/${uuid}.yml")
        val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        config.set("flags", flags)
        config.save(file)
    }

    fun setFlag(key: String, value: Any) {
        flags[key] = value
    }

    fun containFlag(key: String): Boolean {
        return flags.containsKey(key)
    }

    fun getFlag(key: String): Any? {
        return flags[key]
    }

    fun <T> getFlag(key: String, clazz: Class<T>): T? {
        return flags[key]?.let {
            if (clazz.isInstance(it)) {
                clazz.cast(it)
            } else {
                null
            }
        }
    }

    fun getFlag(key: String, default: Int): Int {
        return flags[key] as? Int ?: default
    }

    fun getAllFlags(): Map<String, Any> {
        return flags
    }

    fun removeFlag(key: String) {
        flags.remove(key)
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

    fun addItems(items: List<ItemStack>) {
        items.forEach {
            player.inventory.addItem(it)
        }
    }

    fun addItem(item: ItemStack) {
        player.inventory.addItem(item)
    }

    fun canAddItem(item: ItemStack): Boolean {
        if (player.inventory.firstEmpty() != -1) {
            return true
        }
        val currentItems = player.inventory.contents.filter{ it != null && it.isSimilar(item) }
        val itemAmount = currentItems.sumOf { it?.amount ?: 0 }
        val itemSlotAmount = currentItems.size
        return itemAmount + item.amount <= item.maxStackSize * itemSlotAmount
    }

    fun hasItem(item: ItemStack): Boolean {
        return player.inventory.containsAtLeast(item, 1)
    }

    fun removeItem(item: ItemStack) {
        player.inventory.removeItem(item)
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