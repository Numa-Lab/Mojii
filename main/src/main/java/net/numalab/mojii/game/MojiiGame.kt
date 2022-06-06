package net.numalab.mojii.game

import com.github.bun133.bukkitfly.advancement.ToastData
import com.github.bun133.bukkitfly.advancement.toast
import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.entity.firework.spawnFireWork
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.scoreboard.MojiiGameScoreBoard
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Team
import java.time.Duration

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
    fun checkWinner(plugin: JavaPlugin): Team? {
        val score = updateScore()
        val winner = score.toList().find { it.second >= setting.clearCardAmount }

        if (winner != null) {
            onClear(winner.first, winner.second, plugin)
            return winner.first
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

private fun onClear(winner: Team, score: Int, plugin: JavaPlugin) {
    val players = Bukkit.getOnlinePlayers()
    players.forEach {
        it.showTitle(
            Title.title(
                winner.displayName() + text("がクリアしました", NamedTextColor.GREEN), text("${score}枚獲得"),
                Title.Times.of(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
            )
        )
    }

    val winnerFireWork: (FireworkMeta) -> Unit = { m: FireworkMeta ->
        m.power = 1
        m.addEffects(
            FireworkEffect.builder().trail(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.YELLOW).build()
        )
    }

    players.filter { winner.entries.contains(it.name) }.forEach {
        // 勝ったチームのメンバー
        it.toast(winnerToast(), plugin)
        it.location.world.spawnFireWork(it.location.clone().add(.0, 10.0, .0), winnerFireWork)
    }

    players.filter { !winner.entries.contains(it.name) }.forEach {
        // 負けたチームのメンバー
        it.toast(loserToast(), plugin)
    }
}

private val winnerToast: () -> ToastData = { ToastData(Material.END_CRYSTAL, "勝利!", ToastData.FrameType.Goal) }
private val loserToast: () -> ToastData = { ToastData(Material.STONE, "敗北!", ToastData.FrameType.Goal) }