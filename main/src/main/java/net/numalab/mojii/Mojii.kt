package net.numalab.mojii

import dev.kotx.flylib.flyLib
import net.numalab.mojii.command.MojiiCommand
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.listener.Listeners
import org.bukkit.plugin.java.JavaPlugin

class Mojii : JavaPlugin() {
    init {
        flyLib {
            command(MojiiCommand())
        }
    }

    override fun onEnable() {
        Listeners(this)
    }

    var currentGame: MojiiGame? = null   // 今のゲーム
}