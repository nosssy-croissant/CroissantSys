package com.github.sheauoian.croissantsys.mining

import com.github.sheauoian.croissantsys.mining.ui.CToolGui
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

@Command(name = "tool")
class CToolCmd {
    @Execute
    @Permission("croissantsys.tool")
    fun openGui(@Context player: UserDataOnline) {
        player.openGui(CToolGui(player))
    }
}