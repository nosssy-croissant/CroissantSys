package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.pve.mob.CMob
import com.github.sheauoian.croissantsys.pve.mob.CMobDrop
import com.github.sheauoian.croissantsys.pve.mob.CMobLoader
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "mob")
class CMobCmd {

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        // Execute the /mob reload
        CMob.CMobManager.reload()
        sender.sendMessage("モブをリロードしました")
    }

    @Execute(name = "add_drop")
    fun addDrop(@Context sender: Player, @Arg(value = "MythicMob ID") mob: CMob) {
        mob.addDrop(
            CMobDrop(
                sender.inventory.itemInMainHand,
                1.0,
                1,
                1,
            )
        )
        CMobLoader.save(mob)
        sender.sendMessage("ドロップを追加しました")
    }
}