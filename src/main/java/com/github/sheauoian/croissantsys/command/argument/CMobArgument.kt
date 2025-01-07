package com.github.sheauoian.croissantsys.command.argument

import com.github.sheauoian.croissantsys.pve.mob.CMob
import com.github.sheauoian.croissantsys.pve.mob.CMob.CMobManager
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.command.CommandSender

class CMobArgument : ArgumentResolver<CommandSender, CMob>() {
    override fun parse(
        p0: Invocation<CommandSender>,
        p1: Argument<CMob>,
        p2: String
    ): ParseResult<CMob> {
        val data = CMobManager.get(p2)
        return ParseResult.success(data)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<CMob>,
        context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(MythicBukkit.inst().mobManager.mobNames)
    }
}