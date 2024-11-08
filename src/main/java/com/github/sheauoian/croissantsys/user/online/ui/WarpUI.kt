package com.github.sheauoian.croissantsys.user.online.ui

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import mc.obliviate.inventory.Gui
import mc.obliviate.inventory.Icon
import mc.obliviate.inventory.configurable.util.XMaterial
import mc.obliviate.inventory.pagination.PaginationManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class WarpUI(user: UserDataOnline)
    : Gui(user.player, "warp." + user.uuid, MiniMessage.miniMessage()
        .deserialize("<gradient:#e63355:#aa8899:#78606f>ワープメニュー</gradient>"), 5) {
    private val pagination = PaginationManager(this)

    init {
        pagination.registerPageSlotsBetween(0, 35)
    }

    override fun onOpen(event: InventoryOpenEvent) {
        fillGui(ItemStack(Material.BLACK_STAINED_GLASS_PANE))
        calculateAndUpdatePagination()
        if (pagination.currentPage != 0) {
            addItem(
                36, Icon(XMaterial.ARROW.parseItem()).setName("§aPrevious").onClick { e: InventoryClickEvent? ->
                    pagination.goPreviousPage()
                    calculateAndUpdatePagination()
                }
            )
        }
        if (!pagination.isLastPage) {
            addItem(
                44, Icon(XMaterial.ARROW.parseItem()).setName("§aNext").onClick { e: InventoryClickEvent? ->
                    pagination.goNextPage()
                    calculateAndUpdatePagination()
                }
            )
        }
    }


    /*
    private fun calculateProducts() {
        pagination.items.clear()
        for (point: WarpPoint in WarpPointManager.getInstance().all) {
            if (UnlockedWarpPointDao.getInstance().has(user.player, point.id)) {
                pagination.addItem(
                    Icon(Material.PAPER).setName(point.name)
                        .appendLore(
                            "§7" +
                                    point.location.blockX + ", " +
                                    point.location.blockY + ", " +
                                    point.location.blockZ + ", " +
                                    point.location.world
                        )
                        .onClick { e: InventoryClickEvent? ->
                            //TODO warpの処理・演出を行う関数を作成する。
                            point.warp(user.player)
                        }
                )
            } else {
                pagination.addItem(Icon(Material.BEDROCK).setName("未開放のワープポイント"))
            }
        }
    }
    */

    private fun calculateAndUpdatePagination() {
        //calculateProducts()
        pagination.update()
    }
}