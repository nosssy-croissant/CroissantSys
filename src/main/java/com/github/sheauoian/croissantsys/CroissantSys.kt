package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.MenuCmd
import com.github.sheauoian.croissantsys.command.StatusCmd
import com.github.sheauoian.croissantsys.command.argument.CStoreArgument
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.command.argument.UDataOnlineArgument
import com.github.sheauoian.croissantsys.command.argument.WarpPointArgument
import com.github.sheauoian.croissantsys.command.context.UDataContextProvider
import com.github.sheauoian.croissantsys.command.op.CStoreCmd
import com.github.sheauoian.croissantsys.command.op.UserFlagCmd
import com.github.sheauoian.croissantsys.command.op.WarpPointSettingCmd
import com.github.sheauoian.croissantsys.command.op.WearingCmd
import com.github.sheauoian.croissantsys.command.op.equipment.EquipmentCommand
import com.github.sheauoian.croissantsys.discord.RabbitBot
import com.github.sheauoian.croissantsys.listener.ElevatorListener
import com.github.sheauoian.croissantsys.user.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.pve.DamageListener
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.pve.equipment.listener.EquipmentStoringListener
import com.github.sheauoian.croissantsys.pve.equipment.weapon.WeaponListener
import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.UserRunnable
import com.github.sheauoian.croissantsys.user.listener.SkillListener
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.world.listener.HologramListener
import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class CroissantSys: JavaPlugin() {
    companion object {
        lateinit var instance: CroissantSys
    }

    private var liteCommands: LiteCommands<CommandSender>? = null
    var rabbitDiscordBot: RabbitBot? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

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
        if (token != null && guild != null) rabbitDiscordBot = RabbitBot(token, guild)
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
                WearingCmd(),
                UserFlagCmd(),
                CStoreCmd()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .argument(
                WarpPoint::class.java, WarpPointArgument()
            )
            .argument(
                UserDataOnline::class.java, UDataOnlineArgument()
            )
            .argument(
                CStore::class.java, CStoreArgument()
            )
            .context(
                UserDataOnline::class.java, UDataContextProvider()
            )
            .build()
    }

    private fun eventSetup() {
        val manager = Bukkit.getPluginManager()
        manager.registerEvents(PlayerJoinListener(), this)
        manager.registerEvents(DamageListener(), this)
        manager.registerEvents(WeaponListener(), this)
        manager.registerEvents(HologramListener(), this)
        manager.registerEvents(EquipmentStoringListener(), this)
        manager.registerEvents(SkillListener(), this)

        manager.registerEvents(ElevatorListener(), this)
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