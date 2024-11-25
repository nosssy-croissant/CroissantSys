package com.github.sheauoian.croissantsys.command.argument

import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class UDataOnlineArgument: ArgumentResolver<CommandSender, UserDataOnline>() {
    override fun parse(
        p0: Invocation<CommandSender>, p1: Argument<UserDataOnline>, p2: String
    ): ParseResult<UserDataOnline> {
        val player = Bukkit.getPlayer(p2) ?:
            return ParseResult.failure("そのプレイヤーは存在しません")
        val data = UserDataManager.instance.get(player)
        return ParseResult.success(data)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>, argument: Argument<UserDataOnline>, context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(
            Bukkit.getOnlinePlayers().map { it.name }
        )
    }
}