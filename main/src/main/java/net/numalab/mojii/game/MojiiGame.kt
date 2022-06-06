package net.numalab.mojii.game

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.scoreboard.MojiiGameScoreBoard
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

    private val maps = mutableListOf<MojiiMap>()

    fun registerMap(map: MojiiMap): MojiiGame {
        if (!maps.any { it.stack == map.stack }) {
            maps.add(map)
        }
        return this
    }

    /**
     * 勝者がいるかどうか判定する
     * @return いたらそのチームを返す
     */
    fun checkWinner(): Team? {
        val score = updateScore()
        val winner = score.toList().find { it.second >= setting.clearCardAmount }?.first

        if (winner != null) {
            Bukkit.broadcast(winner.displayName() + text("の勝利です", NamedTextColor.GREEN))
            return winner
        }
        return null
    }

    fun updateScore(): MutableMap<Team, Int> {
        println("mapsSize: ${maps.size}")
        val score = maps.groupBy { it.ownedTeam }.mapValues { it.value.size }.filterKeys { it != null }
        val filteredScore = mutableMapOf<Team, Int>()
        score.forEach { (t, u) ->
            filteredScore[t!!] = u
        }
        // Update ScoreBoard
        MojiiGameScoreBoard.getInstance().let {
            it.score.clear()
            it.score.putAll(filteredScore)
            it.update()
        }
        return filteredScore
    }
}