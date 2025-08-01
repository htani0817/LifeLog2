package com.example.lifelog2

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class GuiListener : Listener {
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        /* 当プラグインの GUI だけを判定：タイトル先頭に §a */
        if (e.view.title.startsWith("§a") && e.view.title.endsWith(" の統計")) {
            e.isCancelled = true         // アイテム持ち出し禁止
        }
    }
}
