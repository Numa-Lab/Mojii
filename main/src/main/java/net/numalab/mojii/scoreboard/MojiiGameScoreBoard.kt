package net.numalab.mojii.scoreboard

import com.github.bun133.bukkitfly.component.text
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team


class MojiiGameScoreBoard() {
    companion object {
        private fun getObjective(): Objective {
            return Bukkit.getServer().scoreboardManager.mainScoreboard.getObjective("MojiiGame")
                ?: Bukkit.getServer().scoreboardManager.mainScoreboard.registerNewObjective(
                    "MojiiGame",
                    "dummy",
                    text("スコア")
                )
        }

        private var instance: MojiiGameScoreBoard? = null
        fun getInstance(): MojiiGameScoreBoard {
            if (instance == null) {
                instance = MojiiGameScoreBoard()
            }
            return instance!!
        }
    }

    private val obj = getObjective()

    val score = mutableMapOf<Team, Int>()

    fun update() {
        score.forEach { (t, u) ->
            val score = obj.getScore(t.name)
            score.score = u
        }
    }
}