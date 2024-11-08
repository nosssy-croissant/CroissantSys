package com.github.sheauoian.croissantsys.world.treasure

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.world.WorldObject
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.metadata.FixedMetadataValue

class TreasureChest(val data: TreasureChestData, override val location: Location): WorldObject {
    override fun update() {
        val block = location.block
        block.type = Material.CHEST
        block.setMetadata("treasure", FixedMetadataValue(CroissantSys.instance, this))
    }

    override fun use(user: UserDataOnline) {
        data.open(user.player)
        val block = location.block
        block.type = Material.AIR
        block.removeMetadata("treasure", CroissantSys.instance)
    }
}