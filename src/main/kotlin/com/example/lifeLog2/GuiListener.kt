package com.example.lifelog2

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class GuiListener : Listener {
    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val title = PlainTextComponentSerializer.plainText().serialize(e.view.title())
        if (title.endsWith(" の統計")) e.isCancelled = true
    }
}
