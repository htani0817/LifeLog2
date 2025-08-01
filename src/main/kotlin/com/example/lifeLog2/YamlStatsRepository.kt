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
        backfill()
    }

    private fun backfill() {
        Bukkit.getWorlds().forEach { w ->
            File(w.worldFolder, "stats").listFiles { _, n -> n.endsWith(".json") }?.forEach { f ->
                val json = JsonParser.parseReader(FileReader(f)).asJsonObject
                val custom = json["stats"].asJsonObject["minecraft:custom"].asJsonObject
                val deaths = custom["minecraft:deaths"]?.asLong ?: 0
                val play   = custom["minecraft:play_time"]?.asLong ?: 0
                val uuid   = UUID.fromString(f.name.removeSuffix(".json"))
                val entry  = data.computeIfAbsent(uuid) { StatsEntry() }
                entry.deaths = maxOf(entry.deaths, deaths)
                entry.playTicks = maxOf(entry.playTicks, play)
            }
        }
    }

    private fun save() {
        data.forEach { (u, s) ->
            yaml.set("$u.deaths", s.deaths)
            yaml.set("$u.playTicks", s.playTicks)
        }
        yaml.save(file)
    }

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
        data.computeIfAbsent(uuid) { StatsEntry() }.playTicks += (millis - start) / 50
        save()
    }

    override fun all(): Map<UUID, StatsEntry> = data
}
