package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.EquipmentCommand
import com.github.sheauoian.croissantsys.command.MenuCmd
import com.github.sheauoian.croissantsys.command.StatusCmd
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.command.argument.WarpPointArgument
import com.github.sheauoian.croissantsys.command.op.WarpPointSettingCmd
import com.github.sheauoian.croissantsys.command.op.WearingCmd
import com.github.sheauoian.croissantsys.discord.RabbitBot
import com.github.sheauoian.croissantsys.user.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.pve.DamageListener
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.pve.equipment.weapon.WeaponListener
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.UserRunnable
import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import mc.obliviate.inventory.InventoryAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class CroissantSys: JavaPlugin() {
    companion object {
        // Singleton
        lateinit var instance: CroissantSys
    }

    private var liteCommands: LiteCommands<CommandSender>? = null
    var rabbit: RabbitBot? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        InventoryAPI(this).init()

        liteCommandSetup()
        discordSetup()
        eventSetup()

        EDataManager.instance.reload()
        UserRunnable().runTaskTimer(this, 5, 2)
        WarpPointManager.instance.reload()
    }

    override fun onDisable() {
        saveConfig()
        UserDataManager.instance.saveAll()
        EDataManager.instance.saveAll()
        DbDriver.instance.close()
    }

    private fun discordSetup() {
        val token = config.getString("discord_token")
        val guild = config.getString("discord_guild")
        if (token != null && guild != null) rabbit = RabbitBot(token, guild)
    }

    private fun liteCommandSetup() {
        liteCommands = LiteBukkitFactory
            .builder("sys", this)
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
            }
            .commands(
                EquipmentCommand(),
                StatusCmd(),
                MenuCmd(),
                WarpPointSettingCmd(),
                WearingCmd()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .argument(
                WarpPoint::class.java, WarpPointArgument()
            )
            .build()
    }

    private fun eventSetup() {
        val manager = Bukkit.getPluginManager()
        manager.registerEvents(PlayerJoinListener(), this)
        manager.registerEvents(DamageListener(), this)
        manager.registerEvents(WeaponListener(), this)
    }


    var initialSpawnPoint: Location
        get() {
            return config.getLocation("initial_spawn_point", server.getWorld("world")?.spawnLocation)!!
        }
        set(location) {
            config.set("initial_spawn_point", location)
            saveConfig()
        }
}