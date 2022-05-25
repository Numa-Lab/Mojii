package net.numalab.mojii

import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.collection.TeamSetValue
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
}