package net.numalab.mojii.game

import net.numalab.mojii.lang.Lang
import org.bukkit.scoreboard.Team

data class MojiiGameSetting(
    val teams: List<Team>,
    val lang: Lang,
    // 何枚とったら勝つか
    val clearLine: Int
)