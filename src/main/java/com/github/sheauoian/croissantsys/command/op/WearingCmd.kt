package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.UserDataManager
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.entity.Player

@Command(name = "wearing")
class WearingCmd {
    @Execute(name = "get")
    fun get(@Context sender: Player, @Arg(value = "装備データID") data: EquipmentData) {
        // Execute the /wearing get
        UserDataManager.instance.get(sender).let { user ->
            val equipment = EquipmentManager.instance.generate(data, user.uuid.toString())
            user.wearing.setWearing(equipment)
            sender.sendMessage("装備しました")
        }
    }
}