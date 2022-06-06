package net.numalab.mojii.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import dev.kotx.flylib.command.Command
import net.numalab.mojii.lang.Lang
import net.numalab.mojii.map.genMojiiMap

class MojiiLangItemCommand : Command("stackLang") {
    init {
        description("特定Langの内のすべてのCharItemStackを生成する")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
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

                val items =
                    lang.langSet.chars().chars.map { genMojiiMap(it, p.location.world).also { m -> m.redraw() }.stack }
                p.inventory.addOrDrop(*items.toTypedArray())
                success("${items.size}個のItemStackを${lang.langSet.langName()}から生成しました")
            }
        }
    }
}