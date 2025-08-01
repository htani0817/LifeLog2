package com.example.lifelog2

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
        sender: CommandSender, cmd: Command, label: String, args: Array<out String>
    ): Boolean {

        /* /lifelog me */
        if (args.isEmpty() || args[0].equals("me", true)) {
            val p = sender as? Player ?: return true
            plugin.repo.all()[p.uniqueId]?.let { s ->
                p.sendMessage(
                    Component.text("Deaths: ${s.deaths}  PlayTime: ${s.hours}h")
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

        /* /lifelog top [deaths|playtime] */
        if (args[0].equals("top", true)) {
            val mode = args.getOrNull(1)?.lowercase() ?: "deaths"
            val sorted = plugin.repo.all().entries.let {
                if (mode.startsWith("play")) it.sortedByDescending { e -> e.value.hours }
                else it.sortedByDescending { e -> e.value.deaths }
            }.take(10)

            val header = if (mode.startsWith("play")) "=== Top PlayTime ===" else "=== Top Deaths ==="
            sender.sendMessage(Component.text("§6$header"))
            sorted.forEachIndexed { i, entry ->
                val name = Bukkit.getOfflinePlayer(entry.key).name ?: "?"
                val stat = if (mode.startsWith("play")) "${entry.value.hours}h" else "${entry.value.deaths}"
                sender.sendMessage(Component.text("${i + 1}. $name - $stat"))
            }
            return true
        }

        /* /lifelog gui */
        if (args[0].equals("gui", true) && sender is Player) {
            plugin.repo.all()[sender.uniqueId]?.let { s ->
                sender.openInventory(GuiUtil.createGui(sender.name, s.deaths, s.hours))
            }
            return true
        }

        return false
    }
}
