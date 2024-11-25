package com.github.sheauoian.croissantsys.command.argument

import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.store.CStoreManager
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import org.bukkit.command.CommandSender

class CStoreArgument: ArgumentResolver<CommandSender, CStore>() {
    override fun parse(
        p0: Invocation<CommandSender?>?,
        p1: Argument<CStore>?,
        p2: String?
    ): ParseResult<CStore>? {
        val store = CStoreManager.instance.stores.find { it.id == p2 }
        return if (store != null) {
            ParseResult.success(store)
        } else {
            ParseResult.failure("そのストアは存在しません")
        }
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<CStore>,
        context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(CStoreManager.instance.stores.map { it.id })
    }
}