package net.numalab.mojii

import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.EnumValue
import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.collection.TeamSetValue
import org.bukkit.DyeColor
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.Team

class MojiiConfig(plugin: Plugin) : BaseConfig(plugin) {
    private val teams = TeamSetValue()
    fun team() = teams.value()
    fun addTeam(team: Team) {
        teams.add(team)
    }

    fun removeTeam(team: Team) {
        teams.remove(team)
    }

    fun clearTeam() {
        teams.clear()
    }

    // Summaryの取得文字数
    val exChars = IntegerValue(50, 1, Int.MAX_VALUE)

    // 単語が成立したときに発生するエフェクトの持続時間
    val effectTick = IntegerValue(20 * 1, 1, Int.MAX_VALUE)
}