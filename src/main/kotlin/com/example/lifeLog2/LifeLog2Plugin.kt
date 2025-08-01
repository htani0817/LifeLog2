package com.example.lifelog2

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.util.*

class LifeLog2Plugin : JavaPlugin(), Listener {

    lateinit var repo: YamlStatsRepository; private set
    val sessions = HashMap<UUID, Long>()

    override fun onEnable() {
        repo = YamlStatsRepository(this).apply { load() }
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(GuiListener(), this)

        val board = server.scoreboardManager!!.mainScoreboard
        val obj = board.getObjective("lifelog")
            ?: board.registerNewObjective(
                "lifelog",
                Criteria.DUMMY,
                Component.text("Deaths")
            )
        obj.displaySlot = DisplaySlot.SIDEBAR

        RankingTask(this).runTaskTimer(this, 0, 20 * 300)
        getCommand("lifelog")!!.setExecutor(StatsCommand(this))
    }

    @EventHandler fun onDeath(e: PlayerDeathEvent) =
        repo.incrementDeath(e.entity.uniqueId)

    @EventHandler fun onJoin(e: PlayerJoinEvent) =
        repo.startSession(e.player.uniqueId, System.currentTimeMillis())

    @EventHandler fun onQuit(e: PlayerQuitEvent) =
        repo.endSession(e.player.uniqueId, System.currentTimeMillis())
}
