package net.numalab.mojii.game

import net.numalab.mojii.lang.Lang
import org.bukkit.scoreboard.Team

data class MojiiGameSetting(
    // Should not empty
    val teams: List<Team>,
    val lang: Lang,
    // 何枚とったら勝つか
    val clearCardAmount: Int
){
    init {
        require(teams.isNotEmpty())
        require(clearCardAmount > 0)
    }
}