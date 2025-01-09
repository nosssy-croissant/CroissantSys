package com.github.sheauoian.croissantsys.command.argument

import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import org.bukkit.command.CommandSender

class EDataArgument: ArgumentResolver<CommandSender, EquipmentData>() {
    override fun parse(
        p0: Invocation<CommandSender>,
        p1: Argument<EquipmentData>,
        p2: String
    ): ParseResult<EquipmentData> {
        val data = EDataManager.get(p2) ?: return ParseResult.failure("存在しない Equipment ID です")
        return ParseResult.success(data)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<EquipmentData>,
        context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(EDataManager.getIds())
    }
}