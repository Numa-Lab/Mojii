package net.numalab.mojii.command

import com.github.bun133.tinked.RunnableTask
import com.google.gson.Gson
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.arguments.StringArgument
import net.numalab.mojii.api.WikiMediaSearchRequest
import net.numalab.mojii.api.WikiMediaSearchRequestTask
import net.numalab.mojii.lang.Lang

class MojiiSearchCommand : Command("search") {
    init {
        description("Searching in WikiMedia")
        usage {
            selectionArgument("LangCode", Lang.values().map { it.name })
            stringArgument("Keyword", StringArgument.Type.PHRASE)

            executes {
                val lang: Lang
                try {
                    lang = Lang.valueOf(this.typedArgs[0] as String)
                } catch (e: IllegalArgumentException) {
                    fail("Invalid LangCode")
                    return@executes
                }
                val keyword = this.typedArgs[1] as String

                val task = WikiMediaSearchRequestTask(WikiMediaSearchRequest(keyword, lang))
                    .apply {
                        then(RunnableTask {
                            success("検索結果 : $keyword in ${lang.langSet.langName()}")
                            val result = it.query.getPages(Gson())
                            success("${result.size}件")
                            result.forEach { p ->
                                success("${p.title}:${p.extract}")

                                if (p.extract == null) {
                                    success("Printing Body...")
                                    success("PageId:${p.pageid}")
                                    success("ns:${p.ns}")
                                }
                            }
                        })
                    }

                task.run(Unit)
            }
        }
    }
}