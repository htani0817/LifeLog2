package com.example.lifelog2

import java.util.UUID

interface StatsRepository {
    fun incrementDeath(uuid: UUID)
    fun startSession(uuid: UUID, millis: Long)
    fun endSession(uuid: UUID, millis: Long)
    fun all(): Map<UUID, StatsEntry>

    data class StatsEntry(
        var deaths: Long = 0,
        var playTicks: Long = 0
    ) {
        val hours get() = playTicks / 20 / 3600
    }
}
