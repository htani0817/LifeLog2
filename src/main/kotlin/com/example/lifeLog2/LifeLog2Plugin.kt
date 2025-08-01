package com.example.lifelog2

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class LifeLog2Plugin : JavaPlugin(), Listener {

    lateinit var repo: YamlStatsRepository; private set
    val sessions = HashMap<UUID, Long>()

    override fun onEnable() {
        repo = YamlStatsRepository(this).apply { load() }
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(GuiListener(), this)

        getCommand("lifelog")!!.setExecutor(StatsCommand(this))
        logger.info("LifeLog2 enabled without scoreboard.")
    }

    @EventHandler fun onDeath(e: PlayerDeathEvent) =
        repo.incrementDeath(e.entity.uniqueId)

    @EventHandler fun onJoin(e: PlayerJoinEvent) =
        repo.startSession(e.player.uniqueId, System.currentTimeMillis())

    @EventHandler fun onQuit(e: PlayerQuitEvent) =
        repo.endSession(e.player.uniqueId, System.currentTimeMillis())
}
