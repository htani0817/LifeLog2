package com.example.lifelog2

import net.kyori.adventure.text.Component
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

        if (args.isEmpty() || args[0].equals("me", true)) {
            val p = sender as? Player ?: return true
            plugin.repo.all()[p.uniqueId]?.let {
                sender.sendMessage(
                    Component.text("Deaths: ${it.deaths}  PlayTime: ${it.hours}h")
                )
            }
            return true
        }

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
        return false
    }
}
