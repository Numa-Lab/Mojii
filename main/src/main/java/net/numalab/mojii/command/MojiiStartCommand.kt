package net.numalab.mojii.command

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import dev.kotx.flylib.command.Command
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.numalab.mojii.Mojii
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.game.MojiiGameSetting
import net.numalab.mojii.lang.Lang
import org.bukkit.Bukkit

class MojiiStartCommand : Command("start") {
    init {
        description("start mojii game")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
            integerArgument("ClearCardAmount", 1)
            executes {
                val lang: Lang
                try {
                    lang = Lang.valueOf(this.typedArgs[0] as String)
                } catch (e: IllegalArgumentException) {
                    fail("Invalid LangCode")
                    return@executes
                }
                val clearCardAmount = this.typedArgs[1] as Int

                val pl = plugin as Mojii
                if (pl.currentGame != null) {
                    success("進行中のゲームを中断します")
                }

                val tm = pl.config.team().toList()
                if (tm.isEmpty()) {
                    fail("チームが設定されていません")
                } else {
                    pl.currentGame = MojiiGame(MojiiGameSetting(pl.config.team().toList(), lang, clearCardAmount))
                    Bukkit.broadcast(text("ゲームを開始しました",NamedTextColor.GREEN))

                    // 著作権上の表示
                    Bukkit.broadcast(
                        text("【著作権上必要な表示】") + Component.newline() +
                                text(
                                    "このプラグインはCC BY-SA 3.0(https://creativecommons.org/licenses/by-sa/3.0/)で二次利用許諾されたWikipedia(https://www.wikipedia.org/)のコピーを使用しています。",
                                    NamedTextColor.RED
                                )
                    )
                }
            }
        }
    }
}