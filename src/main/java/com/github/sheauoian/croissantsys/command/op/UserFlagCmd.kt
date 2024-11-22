package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.Async

@Command(name = "userflag", aliases = ["uf"])
class UserFlagCmd {

    @Execute(name = "setInt")
    fun setInt(
        @Context sender: CommandSender,
        @Arg("player") user: UserDataOnline,
        @Arg("flag") flag: String,
        @Arg("value") value: Int) {
        user.setFlag(flag, value)
        sender.sendMessage("フラグを設定しました")
    }

    @Execute(name = "setBoolean")
    fun setBoolean(
        @Context sender: CommandSender,
        @Arg("player") user: UserDataOnline,
        @Arg("flag") flag: String,
        @Arg("value") value: Boolean) {
        user.setFlag(flag, value)
        sender.sendMessage("フラグを設定しました")
    }

    @Execute(name = "setString")
    fun setString(
        @Context sender: CommandSender,
        @Arg("player") user: UserDataOnline,
        @Arg("flag") flag: String,
        @Arg("value") value: String) {
        user.setFlag(flag, value)
        sender.sendMessage("フラグを設定しました")
    }

    @Execute(name = "remove")
    fun remove(
        @Context sender: CommandSender,
        @Arg("player") user: UserDataOnline,
        @Arg("flag") flag: String) {
        user.removeFlag(flag)
        sender.sendMessage("フラグを削除しました")
    }

    @Execute(name = "list")
    fun list(
        @Context sender: CommandSender,
        @Arg("player") user: UserDataOnline) {
        sender.sendMessage("フラグ一覧: ")
        user.getAllFlags().forEach { (key, value) ->
            sender.sendMessage(" - $key: $value")
        }
    }
}