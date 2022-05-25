package net.numalab.mojii.command

import dev.kotx.flylib.command.Command
import net.numalab.mojii.Mojii
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.game.MojiiGameSetting
import net.numalab.mojii.lang.Lang

class MojiiStartCommand : Command("start") {
    init {
        description("start mojii game")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
            integerArgument("ClearLine", 1)
            executes {
                val lang: Lang
                try {
                    lang = Lang.valueOf(this.typedArgs[0] as String)
                } catch (e: IllegalArgumentException) {
                    fail("Invalid LangCode")
                    return@executes
                }
                val clearLine = this.typedArgs[1] as Int

                val pl = plugin as Mojii
                if (pl.currentGame != null) {
                    success("進行中のゲームを中断します")
                }

                pl.currentGame = MojiiGame(MojiiGameSetting(pl.config.team().toList(), lang, clearLine))
            }
        }
    }
}