package com.example.lifelog2

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class RankingTask(private val plugin: LifeLog2Plugin) : BukkitRunnable() {
    override fun run() {
        val manager = plugin.server.scoreboardManager ?: return
        val board   = manager.mainScoreboard
        val obj     = board.getObjective("lifelog") ?: return

        obj.displayName(Component.text("Top Deaths"))
        board.entries.forEach { board.resetScores(it) }

        plugin.repo.all()
            .entries.sortedByDescending { it.value.deaths }
            .take(10)
            .forEach { entry ->
                val name = Bukkit.getOfflinePlayer(entry.key).name ?: "?"
                obj.getScore(name).score = entry.value.deaths.toInt()
            }
    }
}
