package net.numalab.mojii.command

import dev.kotx.flylib.command.Command
import net.numalab.mojii.MojiiConfig
import org.bukkit.Bukkit

class MojiiTeamCommand(val conf: MojiiConfig) : Command("team") {
    init {
        description("Team Command of Mojii Plugin")
        usage {
            selectionArgument("task", "add", "remove")
            stringArgument("teamName", {
                suggestAll((Bukkit.getServer().scoreboardManager.mainScoreboard.teams + conf.team()).map { it.name })
            })

            executes {
                val teamName = typedArgs[1] as String
                val team = Bukkit.getServer().scoreboardManager.mainScoreboard.getTeam(teamName)
                if (team == null) {
                    fail("チームが見つかりませんでした")
                    return@executes
                }
                when (typedArgs[0] as String) {
                    "add" -> {
                        conf.addTeam(team)
                        success("チームを追加しました")
                    }
                    "remove" -> {
                        conf.removeTeam(team)
                        success("チームを削除しました")
                    }
                }
            }
        }
    }
}