package com.example.lifelog2

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatsCommand(private val plugin: LifeLog2Plugin) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        /* ---------- /lifelog me ---------- */
        if (args.isEmpty() || args[0].equals("me", true)) {
            val p = sender as? Player ?: return true
            plugin.repo.all()[p.uniqueId]?.let {
                p.sendMessage(
                    Component.text()
                        .append(Component.text("Deaths: ${it.deaths}  PlayTime: ${it.hours}h"))
                        .append(Component.space())
                        .append(
                            Component.text("[GUIを開く]")
                                .color(NamedTextColor.AQUA)
                                .decorate(TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand("/lifelog gui"))
                        )
                )
            }
            return true
        }

        /* ---------- /lifelog top ---------- */
        if (args[0].equals("top", true)) {
            val list = plugin.repo.all()
                .entries.sortedByDescending { it.value.deaths }
                .take(10)

            sender.sendMessage(Component.text("§6=== Top Deaths ==="))
            list.forEachIndexed { i, entry ->
                val name = Bukkit.getOfflinePlayer(entry.key).name ?: "?"
                sender.sendMessage(
                    Component.text("${i + 1}. $name - ${entry.value.deaths}")
                )
            }
            return true
        }

        /* ---------- /lifelog gui ---------- */
        if (args[0].equals("gui", true) && sender is Player) {
            val stats = plugin.repo.all()[sender.uniqueId] ?: return true
            val inv = GuiUtil.createGui(sender.name, stats.deaths, stats.hours)
            sender.openInventory(inv)
            return true
        }
        return false
    }
}
