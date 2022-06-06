package net.numalab.mojii.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.SuggestionAction
import dev.kotx.flylib.command.arguments.StringArgument
import net.numalab.mojii.lang.Lang
import net.numalab.mojii.map.genMojiiMap

class MojiiCharItemCommand : Command("stack") {
    init {
        description("CharItemStackを生成する")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
            stringArgument("Char", StringArgument.Type.PHRASE, SuggestionAction {
                val langCode = this.typedArgs[0] as String
                try {
                    val lang = Lang.valueOf(langCode)
                    suggestAll(lang.langSet.chars().chars.map { "" + it.char })
                } catch (e: IllegalArgumentException) {
                    // No Suggestion
                }
            })

            executes {
                val p = this.player
                if (p == null) {
                    fail("Playerから実行してください")
                    return@executes
                }
                val langCode = this.typedArgs[0] as String
                val lang: Lang
                try {
                    lang = Lang.valueOf(langCode)
                } catch (e: IllegalArgumentException) {
                    fail("LangCodeが不正です")
                    return@executes
                }
                val char = (this.typedArgs[1] as String).toCharArray()[0]
                val drawableChar = lang.langSet.chars().chars.find { it.char == char }
                if (drawableChar == null) {
                    fail("存在しない文字です")
                    return@executes
                }
                val item = genMojiiMap(drawableChar, p.location.world).also { it.redraw() }
                p.inventory.addOrDrop(item.stack)
                success("「${char}」を生成しました")
            }
        }
    }
}