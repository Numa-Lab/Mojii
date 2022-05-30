package net.numalab.mojii.game

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

class MojiiGame(val setting: MojiiGameSetting) {
    private var now = 0

    /**
     * 今おけるチームを返す
     */
    fun getTurnTeam(): Team = setting.teams[now]

    /**
     * 次のターンにする
     */
    fun nextTurn() {
        now = (now + 1) % setting.teams.size

        // ターンお知らせ
        Bukkit.broadcast(getTurnTeam().displayName() + text("のターンです"))
    }

    private val score = mutableMapOf<Team, Int>()
    fun updateScore(team: Team, scoreInt: Int) {
        score[team] = scoreInt
    }
}