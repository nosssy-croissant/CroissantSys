package com.github.sheauoian.croissantsys.item

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import io.typst.bukkit.kotlin.serialization.ItemStackSerializable
import io.typst.bukkit.kotlin.serialization.ItemStackSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import java.io.File

class SellMenu(val user: UserDataOnline) : ChestGui(4, "売却メニュー") {
    val sellableItems = mutableListOf<ItemStackSerializable>()
    val itemsPane: OutlinePane = OutlinePane(0, 0, 9, 3)
    val controlPane = StaticPane(0, 3, 9, 1)

    fun save() {
        val file = File("plugins/CroissantSys/sell/${user.uuid}.yml")
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        val items = json.encodeToString(ListSerializer(ItemStackSerializer()), sellableItems)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        file.writeText(items)
    }

    fun load() {
        val file = File("plugins/CroissantSys/sell/${user.uuid}.yml")
        if (!file.exists()) {
            return
        }
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        val items = json.decodeFromString(ListSerializer(ItemStackSerializer()), file.readText())
        sellableItems.addAll(items)
    }

    init {
        this.load()

        setOnGlobalClick { event ->
            event.isCancelled = true
            if (event.clickedInventory?.type == InventoryType.PLAYER)
                onPlayerInventoryClick(event)
        }
        controlPane.addItem(GuiItem(ItemStack(Material.STONE_BUTTON)) { event ->
            sellAll()
        }, 4, 0)
        addPane(itemsPane)
        addPane(controlPane)
        refresh()
    }

    private fun onPlayerInventoryClick(event: InventoryClickEvent) {
        val item = event.clickedInventory?.getItem(event.slot) ?: return
        sellableItems.add(item)
        user.removeItem(item)
        refresh()
    }

    fun sellAll() {
        user.addMoney(sellableItems.sumOf { Sellable(it).getPrice() * it.amount })
        sellableItems.clear()
        refresh()
    }

    fun refresh() {
        itemsPane.clear()
        sellableItems.forEach { item ->
            itemsPane.addItem(GuiItem(item) { event ->
                if (user.canAddItem(item)) {
                    user.addItem(item)
                    sellableItems.remove(item)
                    refresh()
                }
            })
        }
        update()
    }
}