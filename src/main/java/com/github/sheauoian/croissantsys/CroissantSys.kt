package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.CommandSetup
import com.github.sheauoian.croissantsys.discord.RabbitBot
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.mob.CMob
import com.github.sheauoian.croissantsys.store.CStoreManager
import com.github.sheauoian.croissantsys.store.trait.CStoreTrait
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.UserRunnable
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class CroissantSys: JavaPlugin() {
    companion object {
        lateinit var instance: CroissantSys
        fun broadcast(message: String) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message))
        }
    }
    var rabbitDiscordBot: RabbitBot? = null
    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        CommandSetup.setup(this)
        EventSetup.setup(this)
        EDataManager.reload()
        UserRunnable().runTaskTimer(this, 5, 2)
        WarpPointManager.reload()
        CStoreManager.instance.reload()
        CMob.CMobManager.reload()

        val citizens = server.pluginManager.getPlugin("Citizens")
        if (citizens == null)
            logger.log(Level.SEVERE, "Citizens 2.0 not found")
        else if (!citizens.isEnabled)
            logger.log(Level.SEVERE, "Citizens 2.0 not enabled")
        else {
            try {
                CitizensAPI.getTraitFactory().registerTrait(
                    TraitInfo.create(CStoreTrait::class.java)
                )
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "an error occur while registering CStoreTrait")
                e.printStackTrace()
            }
        }

        discordSetup()
    }

    override fun onDisable() {
        saveConfig()
        UserDataManager.instance.saveAll()
        EDataManager.saveAll()
        DbDriver.close()
    }

    private fun discordSetup() {
        val token = config.getString("discord_token")
        val guild = config.getString("discord_guild")
        if (token != null && guild != null) rabbitDiscordBot = RabbitBot(token, guild)
    }

    fun getSpawnPoint(): Location {
        config.getLocation("initial_spawn_point")?.let {
            return it
        }
        val world = Bukkit.getWorld("world")
        if (world == null) {
            logger.log(Level.SEVERE, "World is not loaded!")
            return Bukkit.getWorlds()[0].spawnLocation
        }
        return world.spawnLocation
    }

    fun changeSpawnPoint(location: Location) {
        config.set("initial_spawn_point", location)
        saveConfig()
    }

    var initialSpawnPoint: Location
        get() {
            return getSpawnPoint()
        }
        set(location) {
            changeSpawnPoint(location)
        }
}