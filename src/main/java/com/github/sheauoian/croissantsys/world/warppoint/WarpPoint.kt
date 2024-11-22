package com.github.sheauoian.croissantsys.world.warppoint

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.world.WorldObject
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import eu.decentsoftware.holograms.api.DHAPI
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.sql.SQLException

class WarpPoint(val id: String, val name: String, override val location: Location): WorldObject {
    override fun update() {
        val holo = DHAPI.getHologram("warp_$id")
        if (holo != null) {
            holo.location = location.clone().add(0.0, 2.0, 0.0)
            return
        }
        else
            DHAPI.createHologram(
                "warp_$id",
                location.clone().add(0.0, 2.0, 0.0),
                listOf(
                    "<color:#aaaaaa>ワープポイント [ $name ]",
                    "<color:#aaaaaa>クリックしてアンロック"
                )
            )
    }

    override fun use(user: UserDataOnline) {
        try {
            user.unlockedWarpPointManager.unlock(this)
            user.player.sendMessage("ワープポイント [ $name ] をアンロックしました")
        } catch (e: SQLException) {
            user.player.sendMessage("既にワープポイント [ $name ] はアンロックされています")
        }
    }

    fun getGuiItem(): GuiItem {
        val item = ItemStack(Material.ENDER_PEARL)
        val meta = item.itemMeta
        meta.displayName(Component.text("ワープポイント [ $name ]"))
        item.setItemMeta(meta)
        return GuiItem(item) {
            warp(it.whoClicked as Player)
        }
    }

    fun removeHologram() {
        DHAPI.removeHologram("warp_$id")
    }


    private fun warp(player: Player) {
        player.teleport(location)
    }
}