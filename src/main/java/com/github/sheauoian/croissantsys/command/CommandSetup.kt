package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.command.argument.CMobArgument
import com.github.sheauoian.croissantsys.command.argument.CStoreArgument
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.command.argument.UDataOnlineArgument
import com.github.sheauoian.croissantsys.command.argument.WarpPointArgument
import com.github.sheauoian.croissantsys.command.context.UDataContextProvider
import com.github.sheauoian.croissantsys.command.op.CMobCmd
import com.github.sheauoian.croissantsys.command.op.CStoreCmd
import com.github.sheauoian.croissantsys.command.op.UserFlagCmd
import com.github.sheauoian.croissantsys.command.op.WarpPointSettingCmd
import com.github.sheauoian.croissantsys.command.op.WearingCmd
import com.github.sheauoian.croissantsys.command.op.equipment.EquipmentCommand
import com.github.sheauoian.croissantsys.mining.CToolCmd
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.pve.mob.CMob
import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.world.warppoint.WarpPoint
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import org.bukkit.command.CommandSender

object CommandSetup {
    private var liteCommands: LiteCommands<CommandSender>? = null

    fun setup(plugin: CroissantSys) {
        liteCommands = LiteBukkitFactory
            .builder("sys", plugin)
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
            }
            .commands(
                EquipmentCommand(),
                StatusCmd(),
                MenuCmd(),
                WarpPointSettingCmd(),
                WearingCmd(),
                UserFlagCmd(),
                CStoreCmd(),
                CToolCmd(),
                CMobCmd()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .argument(
                WarpPoint::class.java, WarpPointArgument()
            )
            .argument(
                UserDataOnline::class.java, UDataOnlineArgument()
            )
            .argument(
                CStore::class.java, CStoreArgument()
            )
            .argument(
                CMob::class.java, CMobArgument()
            )
            .context(
                UserDataOnline::class.java, UDataContextProvider()
            )
            .build()
    }
}