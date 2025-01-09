package com.github.sheauoian.croissantsys.command.op.equipment

import com.github.sheauoian.croissantsys.pve.equipment.EquipmentBasic
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.BodyPart
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import dev.rollczi.litecommands.annotations.permission.Permission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "equipment")
@Permission("croissant.equipment")
class EquipmentCommand {

    @Execute
    fun executeEquipment(@Context sender: CommandSender) {
        sender.sendMessage("Equipment Command")
    }


    @Execute(name = "info")
    fun info(@Context user: UserDataOnline) {

    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg(value = "player") user: UserDataOnline) {
        sender.sendMessage(user.wearing.getWearingComponent())
    }

    @Execute(name = "create")
    fun create(@Context sender: CommandSender, @Arg(value = "データID") dataId: String, @OptionalArg bodyPart: BodyPart?) {
        if (EDataManager.addInitialData(dataId, bodyPart) != null) {
            sender.sendMessage("追加に成功しました")
        }
        else {
            sender.sendMessage("追加に失敗しました(データIDの重複があります)")
        }
    }

    @Execute(name = "delete")
    fun delete(@Context sender: CommandSender, @Arg(value = "データID") data: EquipmentData) {
        if (EDataManager.removeData(data.id)) {
            sender.sendMessage("削除に成功しました")
        }
        else {
            sender.sendMessage("削除に失敗しました(存在しないデータIDです)")
        }
    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg(value = "データID") data: EquipmentData) {
        sender.sendMessage(data.toString())
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("Equipment ID 一覧:")
        EDataManager.getAll().forEach {
            sender.sendMessage(" - ${it.id} : ${it.name}")
        }
    }

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        EDataManager.reload()
        sender.sendMessage("Equipment Data のリロードが完了しました。")
    }

    @Execute(name = "get_item")
    @Permission("equipment.get_item")
    fun getItem(
        @Context sender: Player,
        @Arg(value = "equipment_id") data: EquipmentData
    ) {
        sender.inventory.addItem(EquipmentBasic.Companion.generate(data).getItem())
    }
}