package com.github.sheauoian.croissantsys.world.treasure

class TreasureManager {
    companion object {
        val instance: TreasureManager = TreasureManager()
    }

    private val treasures: MutableMap<String, TreasureChestData> = mutableMapOf()

    fun addTreasure(treasure: TreasureChestData) {
        treasures.put(treasure.id, treasure)
    }

    fun find(id: String): TreasureChestData? {
        return treasures[id]
    }
}