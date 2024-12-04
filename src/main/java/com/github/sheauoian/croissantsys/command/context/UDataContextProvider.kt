package com.github.sheauoian.croissantsys.command.context

import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import dev.rollczi.litecommands.context.ContextProvider
import dev.rollczi.litecommands.context.ContextResult
import dev.rollczi.litecommands.invocation.Invocation
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UDataContextProvider: ContextProvider<CommandSender, UserDataOnline> {
    val manager = UserDataManager.instance
    override fun provide(invocation: Invocation<CommandSender>): ContextResult<UserDataOnline> {
        invocation.sender()?.let { player ->
            if (player !is Player) {
                return ContextResult.error("プレイヤーのみがこのコマンドを実行できます")
            }
            val user = manager.get(player)
            return ContextResult.ok { user }
        }
        return ContextResult.error("プレイヤーのデータが見つかりませんでした")
    }
}