package com.github.sheauoian.croissantsys.world.treasure

class TreasureManager {
    companion object {
        val instance: TreasureManager = TreasureManager()
    }

    private val treasures: MutableMap<String, TreasureChest> = mutableMapOf()

    fun addTreasure(treasure: TreasureChest) {
        treasures.put(treasure.id, treasure)
    }

    fun find(id: String): TreasureChest? {
        return treasures[id]
    }
}