package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.store.CStore
import com.github.sheauoian.croissantsys.store.CStoreManager
import com.github.sheauoian.croissantsys.store.product.CProductHolder
import com.github.sheauoian.croissantsys.user.UserDataManager
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Command(name = "store")
class CStoreCmd {

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        CStoreManager.instance.reload()
        sender.sendMessage("ストアをリロードしました")
    }

    @Execute(name = "save")
    fun save(@Context sender: CommandSender) {
        CStoreManager.instance.save()
        sender.sendMessage("ストアを保存しました")
        sender.sendMessage("ストアを保存しました")
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("ストア一覧: ")
        CStoreManager.instance.stores.forEach { store ->
            sender.sendMessage(" - ${store.name}")
        }
    }

    @Execute(name = "add")
    fun add(@Context sender: CommandSender, @Arg("id") id: String, @Arg("name") name: String) {
        val store = CStoreManager.instance.addStore(id, name)
        sender.sendMessage("ストアを追加しました: ${store.name}")
        sender.sendMessage("ID: ${store.id}")
    }

    @Execute(name = "remove")
    fun remove(@Context sender: CommandSender, @Arg("id") store: CStore) {
        CStoreManager.instance.stores.remove(store)
        sender.sendMessage("ストアを削除しました: ${store.name}")
    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg("store_id") store: CStore) {
        sender.sendMessage("ストア情報: ")
        sender.sendMessage(" - ID: ${store.id}")
        sender.sendMessage(" - Name: ${store.name}")
        sender.sendMessage(" - Products: ${store.products.size}")
    }

    @Execute(name = "open")
    fun openGui(@Context player: Player, @Arg("store_id") store: CStore) {
        val user = UserDataManager.instance.get(player)
        store.open(user)
    }

    @Execute(name = "test_item")
    fun testItem(@Context sender: CommandSender) {
        val store = CStoreManager.instance.getStore("test") ?: CStoreManager.instance.addStore("test", "Test Store")
        store.addProduct(CProductHolder.Companion.createSimple(ItemStack(Material.STONE), 100))
        CStoreManager.instance.save()
        sender.sendMessage("アイテムを追加しました")
    }

    @Execute(name = "add_item")
    fun addItem(@Context sender: CommandSender, @Arg("store_id") store: CStore,  @Arg("money") money: Int) {
        val item = if (sender is Player) {
            sender.inventory.itemInMainHand
        } else {
            ItemStack(Material.STONE)
        }
        Equipment.fromItem(item)?.let {
            val product = CProductHolder.Companion.createSimple(it.data, money)
            store.addProduct(product)
            CStoreManager.instance.save()
            sender.sendMessage("装備品アイテムを追加しました")
            return
        }
        val product = CProductHolder.Companion.createSimple(item, money)
        store.addProduct(product)
        CStoreManager.instance.save()
        sender.sendMessage("アイテムを追加しました")
    }
}