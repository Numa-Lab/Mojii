package net.numalab.mojii

import dev.kotx.flylib.flyLib
import net.numalab.mojii.command.MojiiCommand
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.listener.Listeners
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Font

class Mojii : JavaPlugin() {
    lateinit var config: MojiiConfig

    override fun onEnable() {
        val fontFile = getResource("MPLUSRounded1c-Light.ttf")
        font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(128.0F)

        config = MojiiConfig(this).also {
            it.saveConfigIfAbsent()
            it.loadConfig()
        }
        flyLib {
            command(MojiiCommand(config))
        }
        Listeners(this)
    }

    var currentGame: MojiiGame? = null   // 今のゲーム

    companion object {
        lateinit var font: Font
    }
}