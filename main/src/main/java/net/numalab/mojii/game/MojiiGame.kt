package net.numalab.mojii.game

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
    }
}