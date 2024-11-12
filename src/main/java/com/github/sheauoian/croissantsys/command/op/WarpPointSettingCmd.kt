package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import com.github.sheauoian.croissantsys.world.warppoint.WarpPointManager
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "warp_setting")
class WarpPointSettingCmd {
    @Execute
    fun help(@Context sender: CommandSender) {
        sender.sendMessage("/warp_setting list: ワープポイントのリストを表示します")
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("WarpPoint List:")
        WarpPointManager.instance.warps.forEach {
            sender.sendMessage(" - ${it.id}")
        }
    }

    @Execute(name = "create")
    fun create(@Context sender: Player, @Arg id: String, @Arg name: String) {
        val location = sender.location
        if (WarpPointManager.instance.insert(id, name, location)) {
            sender.sendMessage("ワープポイントを作成しました")
        }
        else {
            sender.sendMessage("そのIDは既に使用されています")
        }
    }

    @Execute(name = "delete")
    fun delete(@Context sender: Player, @Arg warp: WarpPoint) {
        if (WarpPointManager.instance.delete(warp.id)) {
            sender.sendMessage("ワープポイントを削除しました")
        }
        else {
            sender.sendMessage("そのIDは存在しません")
        }
    }

    @Execute(name = "move")
    fun move(@Context sender: Player, @Arg warp: WarpPoint) {
        val location = sender.location
        if (WarpPointManager.instance.move(warp.id, location)) {
            sender.sendMessage("ワープポイントを移動しました")
        }
        else {
            sender.sendMessage("そのIDは存在しません")
        }
    }

    @Execute(name = "reload_hologram")
    fun reloadHologram(@Context sender: Player) {
        WarpPointManager.instance.reloadHologram()
        sender.sendMessage("ホログラムを更新しました")
    }

    @Execute(name = "warp")
    fun warp(@Context sender: Player, @Arg warp: WarpPoint) {
        sender.teleport(warp.location)
    }
}