package com.example.lifelog2

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object GuiUtil {
    fun createGui(name: String, deaths: Long, hours: Long): Inventory {
        val inv = Bukkit.createInventory(null, 27,
            Component.text("$name の統計").color(NamedTextColor.GREEN))

        val filler = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
            itemMeta = itemMeta.apply { displayName(Component.text(" ")) }
        }
        repeat(inv.size) { inv.setItem(it, filler) }

        inv.setItem(11, stat(Material.TOTEM_OF_UNDYING, "死亡回数", deaths.toString()))
        inv.setItem(15, stat(Material.CLOCK, "プレイ時間 (h)", hours.toString()))
        return inv
    }

    private fun stat(mat: Material, title: String, value: String) =
        ItemStack(mat).apply {
            itemMeta = itemMeta.apply {
                displayName(Component.text(title).color(NamedTextColor.YELLOW))
                lore(listOf(Component.text(value).color(NamedTextColor.AQUA)))
            }
        }
}
