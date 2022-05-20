package net.numalab.mojii.command

import com.github.bun133.tinked.RunnableTask
import com.google.gson.Gson
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import dev.kotx.flylib.command.arguments.StringArgument
import net.numalab.mojii.api.WikiMediaSearchRequest
import net.numalab.mojii.api.WikiMediaSearchRequestTask
import net.numalab.mojii.api.WikiMediaSummaryRequest
import net.numalab.mojii.api.WikiMediaSummaryRequestTask
import net.numalab.mojii.lang.Lang
import javax.lang.model.type.UnionType

class MojiiSearchCommand : Command("search") {
    companion object {
        fun summaryTask(ctx: CommandContext, title: String, lang: Lang, exChar: Int): WikiMediaSummaryRequestTask {
            ctx.success("Summaryを取得します")
            val t =
                WikiMediaSummaryRequestTask(WikiMediaSummaryRequest(title, lang, exChar))
            t.apply {
                then(RunnableTask { e ->
                    ctx.success("${title}のSummary:${e.query.getPages(Gson())[0].extract}")
                })
            }

            return t
        }

        fun searchTask(ctx: CommandContext, keyword: String, lang: Lang,exChar: Int): WikiMediaSearchRequestTask {
            val task = WikiMediaSearchRequestTask(WikiMediaSearchRequest(keyword, lang))
                .apply {
                    then(RunnableTask {
                        ctx.success("検索結果 : $keyword in ${lang.langSet.langName()}")
                        val result = it.query.getPages(Gson())
                        ctx.success("${result.size}件")
                        result.forEach { p ->
                            ctx.success("${p.title}:${p.extract}")
                        }

                        if (result.size == 1) {
                            val t = summaryTask(ctx, result[0].title, lang, exChar)

                            t.run(Unit)
                        }
                    })
                }

            return task
        }
    }

    init {
        description("Searching in WikiMedia")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
            integerArgument("exChar", 1)
            stringArgument("Keyword", StringArgument.Type.PHRASE)

            executes {
                val lang: Lang
                try {
                    lang = Lang.valueOf(this.typedArgs[0] as String)
                } catch (e: IllegalArgumentException) {
                    fail("Invalid LangCode")
                    return@executes
                }
                val exChar = this.typedArgs[1] as Int
                val keyword = this.typedArgs[2] as String

                val task = searchTask(this, keyword, lang,exChar)

                task.run(Unit)
            }
        }
    }
}