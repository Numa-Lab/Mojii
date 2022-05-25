package net.numalab.mojii

import dev.kotx.flylib.flyLib
import net.numalab.mojii.command.MojiiCommand
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.listener.Listeners
import net.numalab.mojii.scoreboard.MojiiGameScoreBoard
import org.bukkit.plugin.java.JavaPlugin

class Mojii : JavaPlugin() {
    lateinit var config: MojiiConfig
    lateinit var scoreBoard: MojiiGameScoreBoard

    init {
        flyLib {
            command(MojiiCommand())
        }
    }

    override fun onEnable() {
        config = MojiiConfig(this).also {
            it.saveConfigIfAbsent()
            it.loadConfig()
        }
        scoreBoard = MojiiGameScoreBoard()
        Listeners(this)
    }

    var currentGame: MojiiGame? = null   // 今のゲーム
}