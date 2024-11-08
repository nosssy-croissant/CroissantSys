package com.github.sheauoian.croissantsys.command.argument

import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import org.bukkit.command.CommandSender

class WarpPointArgument: ArgumentResolver<CommandSender, WarpPoint>() {
    override fun parse(
        p0: Invocation<CommandSender>,
        p1: Argument<WarpPoint>,
        p2: String
    ): ParseResult<WarpPoint> {
        val data = WarpPointManager.instance.find(p2) ?: return ParseResult.failure("存在しないワープポイントです")
        return ParseResult.success(data)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<WarpPoint>,
        context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(WarpPointManager.instance.ids)
    }
}