package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.RootCommand
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.join.Join
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@RootCommand
class MenuCmd {
    @Execute(name = "menu")
    fun menu(@Context sender: Player) {
        val user = UserDataManager.instance.get(sender)
        user.openMenu()
    }

    @Execute(name = "equipment_storage", aliases = ["es"])
    fun eStorage(@Context sender: Player, @OptionalArg bodyPart: BodyPart?) {
        val user = UserDataManager.instance.get(sender)
        user.openEStorage(bodyPart)
    }

    @Execute(name = "spawn")
    fun spawn(@Context sender: Player) {
        sender.teleport(CroissantSys.instance.initialSpawnPoint)
    }

    @Execute(name = "userinfo")
    fun userInfo(@Context sender: CommandSender, @Arg mcid: String) {
        val user = UserDataManager.instance.get(CroissantSys.instance.server.getOfflinePlayer(mcid).uniqueId)
        if (user == null) {
            sender.sendMessage("そのアカウントは存在しません")
        }
        else {
            sender.sendMessage(user.wearing.getWearingComponent())
        }
    }

    @Execute(name = "discord_broadcast")
    fun discordBroadcast(@Arg name: String, @Join message: String) {
        CroissantSys.instance.rabbit?.broadcast(name, message)
    }

    @Execute(name = "skilltest")
    fun skillTest(@Context sender: Player) {
        val user = UserDataManager.instance.get(sender)
        user.useSkill()
    }

    @Execute(name = "skillinfo")
    fun skillInfo(@Context sender: Player) {
        sender.sendMessage("Skill Info:")
        UserDataManager.instance.get(sender).let { user ->
            sender.sendMessage(user.skill.getDescriptionString(user))
            sender.sendMessage(user.skill.toString())
        }
    }
}