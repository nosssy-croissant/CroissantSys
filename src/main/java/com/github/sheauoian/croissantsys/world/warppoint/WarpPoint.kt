package com.github.sheauoian.croissantsys.world.warppoint

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.world.WorldObject
import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Location
import java.sql.SQLException

class WarpPoint(val id: String, val name: String, override val location: Location): WorldObject {
    override fun update() {
        DHAPI.createHologram(
            "warp_$id",
            location.clone().add(0.0, 2.0, 0.0),
            listOf(
                "<color:#aaaaaa>ワープポイント [ $name ]",
                "<color:#aaaaaa>クリックしてアンロック"
            )
        ).showAll()
    }

    override fun use(user: UserDataOnline) {
        try {
            user.unlockedWarpPointManager.unlock(this)
            user.player.sendMessage("ワープポイント [ $name ] をアンロックしました")
        } catch (e: SQLException) {
            user.player.sendMessage("既にワープポイント [ $name ] はアンロックされています")
        }
    }
}