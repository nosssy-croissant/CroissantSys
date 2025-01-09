package com.github.sheauoian.croissantsys.command.op.equipment

import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.util.BodyPart
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import org.bukkit.command.CommandSender

@Command(name = "equipment data")
class EquipmentDataCmd {
    val manager = EDataManager

    fun showList(sender: CommandSender, page: Int) {
        // 1 page has 10 data
        val data = this.manager.getAll()
        val maxPage = (data.size + 9) / 10
        if (page < 1 || page > maxPage) {
            sender.sendMessage("ページが存在しません")
            return
        }
        sender.sendMessage("Page $page/$maxPage")
        for (i in (page - 1) * 10 until page * 10) {
            if (i >= data.size) {
                break
            }
            sender.sendMessage(data[i].toString())
        }
    }
    @Execute
    fun executeEquipmentData(@Context sender: CommandSender) {
        this.showList(sender, 1)
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender, @OptionalArg("page") page: Int?) {
        this.showList(sender, page ?: 1)
    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg data: EquipmentData) {
        sender.sendMessage(data.toString())
    }

    @Execute(name = "create")
    fun create(
        @Context sender: CommandSender,
        @Arg(value = "データID") dataId: String,
        @OptionalArg(value = "bodyPart") bodyPart: BodyPart = BodyPart.MainHand){
        if (this.manager.addInitialData(dataId, bodyPart) != null) {
            sender.sendMessage("追加に成功しました")
        }
        else {
            sender.sendMessage("追加に失敗しました(データIDの重複があります)")
        }
    }

    @Execute(name = "delete")
    fun delete(@Context sender: CommandSender, @Arg(value = "データID") data: EquipmentData) {
        if (this.manager.removeData(data.id)) {
            sender.sendMessage("削除に成功しました")
        }
        else {
            sender.sendMessage("削除に失敗しました(存在しないデータIDです)")
        }
    }

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        this.manager.reload()
        sender.sendMessage("リロードしました")
    }
}