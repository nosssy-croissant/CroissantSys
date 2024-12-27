package com.github.sheauoian.croissantsys.mining.attr

import com.github.sheauoian.croissantsys.mining.CToolBuilder
import org.bukkit.Material

class EfficiencyCToolAttr(builder: CToolBuilder): CToolAttr(builder) {
    override fun getLevel(): Int {
        return builder.efficiency
    }
    override fun getPrice(): Int {
        return 100 * builder.efficiency
    }
    override val maxLevel = 10
    override val icon = Material.DIAMOND
    override val name = "採掘効率"

    override fun levelUp() {
        builder.efficiency += 1
    }
}