package com.example.lifelog2

import com.example.lifelog2.StatsRepository.StatsEntry
import com.google.gson.JsonParser
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileReader
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class YamlStatsRepository(private val plugin: LifeLog2Plugin) : StatsRepository {

    private val file = File(plugin.dataFolder, "stats.yml")
    private val yaml = YamlConfiguration()
    private val data = ConcurrentHashMap<UUID, StatsEntry>()

    fun load() {
        plugin.dataFolder.mkdirs()
        if (file.exists()) yaml.load(file)

        yaml.getKeys(false).forEach { id ->
            val uuid = UUID.fromString(id)
            data[uuid] = StatsEntry(
                yaml.getLong("$id.deaths"),
                yaml.getLong("$id.playTicks")
            )
        }
        backfillWorldStats()
    }

    private fun backfillWorldStats() {
        Bukkit.getWorlds().forEach { world ->
            File(world.worldFolder, "stats")
                .listFiles { _, n -> n.endsWith(".json") }
                ?.forEach { f ->
                    val root = JsonParser.parseReader(FileReader(f)).asJsonObject
                    val custom = root["stats"].asJsonObject["minecraft:custom"].asJsonObject
                    val deaths = custom["minecraft:deaths"]?.asLong ?: 0L
                    val play   = custom["minecraft:play_time"]?.asLong ?: 0L
                    val uuid   = UUID.fromString(f.name.removeSuffix(".json"))
                    val entry  = data.computeIfAbsent(uuid) { StatsEntry() }
                    entry.deaths    = maxOf(entry.deaths, deaths)
                    entry.playTicks = maxOf(entry.playTicks, play)
                }
        }
    }

    private fun save() {
        data.forEach { (u, e) ->
            yaml.set("$u.deaths", e.deaths)
            yaml.set("$u.playTicks", e.playTicks)
        }
        yaml.save(file)
    }

    /* StatsRepository 実装 */
    override fun incrementDeath(uuid: UUID) {
        data.computeIfAbsent(uuid) { StatsEntry() }.deaths++
        save()
    }

    override fun startSession(uuid: UUID, millis: Long) {
        data.computeIfAbsent(uuid) { StatsEntry() }
        plugin.sessions[uuid] = millis
    }

    override fun endSession(uuid: UUID, millis: Long) {
        val start = plugin.sessions.remove(uuid) ?: return
        val ticks = (millis - start) / 50
        data.computeIfAbsent(uuid) { StatsEntry() }.playTicks += ticks
        save()
    }

    override fun all(): Map<UUID, StatsEntry> = data
}
