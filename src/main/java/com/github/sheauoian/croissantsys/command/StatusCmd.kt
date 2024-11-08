package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.user.online.ui.StatusGui
import dev.rollczi.litecommands.annotations.async.Async
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@Command(name = "status")
class StatusCmd {
    @Execute
    @Description("ステータス情報を表示します")
    fun executeWearing(@Context sender: Player, @OptionalArg @Async target: OfflinePlayer?) {
        val me = UserDataManager.instance.get(sender) ?: return
        val user: UserData? = if (target != null) {
            if (target is Player) {
                UserDataManager.instance.get(target)
            } else
                UserDataManager.instance.get(target.uniqueId)
        } else
            me
        if (user != null) {
            val gui = StatusGui(user)
            me.openGui(gui)
        }
        else {
            sender.sendMessage("ユーザーが存在しませんでした")
        }
    }
}
