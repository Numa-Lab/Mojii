package net.numalab.mojii.command

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.tinked.EventTask
import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.TickedApplyedTask
import com.github.bun133.tinked.WaitEventTask
import com.google.gson.Gson
import dev.kotx.flylib.command.Command
import net.kunmc.lab.configlib.ConfigCommandBuilder
import net.numalab.mojii.MojiiConfig
import net.numalab.mojii.api.Query
import net.numalab.mojii.api.WikiMediaCache
import net.numalab.mojii.api.WikiMediaSearchRequest
import net.numalab.mojii.api.WikiMediaSearchResponse
import net.numalab.mojii.judge.StringGetter
import net.numalab.mojii.lang.Lang
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class MojiiCommand(val conf: MojiiConfig) : Command("mojii") {
    companion object {
        private fun isExistTask(
            keyWord: String,
            lang: Lang
        ): TickedApplyedTask<Unit, WikiMediaSearchResponse, Boolean> {
            return WikiMediaCache.instance.search(WikiMediaSearchRequest(keyWord, lang))
                .apply(RunnableTask {
                    return@RunnableTask it.query.isExist(Gson())
                })
        }

        private fun mapGetTask(p: Player, lang: Lang, plugin: JavaPlugin): EventTask<Unit, PlayerInteractEntityEvent> {
            return EventTask<Unit, PlayerInteractEntityEvent>({
                return@EventTask it.player == p && it.rightClicked is ItemFrame
            }, plugin, PlayerInteractEntityEvent::class.java)
                .apply {
                    then(RunnableTask {
                        it.isCancelled = true
                        val strings = StringGetter.getFrom(it.rightClicked.location.toBlockLocation())
                        if (strings.isEmpty()) {
                            p.sendMessage("文字列はありません")
                        } else {
                            p.sendMessage("===文字列===")
                            p.sendMessage("Size: ${strings.size}")
                            strings.map { s -> s to isExistTask(s.first, lang) }
                                .forEach { t ->
                                    t.second.apply(RunnableTask { b ->
                                        p.sendMessage("${t.first}: $b")
                                    }).run(Unit)
                                }
                        }
                    })
                }
        }
    }

    init {
        description("The root command of mojii")
        children(
            MojiiStartCommand(),
            MojiiSearchCommand(),
            MojiiCharItemCommand(),
            MojiiMapForceRedraw(),
            MojiiLangItemCommand(),
            MojiiTeamCommand(conf),
            ConfigCommandBuilder(conf).build()
        )
        usage {
            selectionArgument("Task", "MojiiMapGet")
            selectionArgument("LangCode", Lang.values().map { it.name })
            executes {
                val langCode = this.typedArgs[1] as String
                val lang: Lang
                try {
                    lang = Lang.valueOf(langCode)
                } catch (e: IllegalArgumentException) {
                    fail("LangCodeが不正です")
                    return@executes
                }
                when (val t = typedArgs[0] as String) {
                    "MojiiMapGet" -> {
                        val p = player
                        if (p != null) {
                            val task = mapGetTask(p, lang, plugin)
                            success("MojiiMapを回転させてください")
                            task.run(Unit)
                        } else {
                            fail("プレイヤーが見つかりません")
                        }
                    }
                    else -> {
                        fail("Unknown task: $t")
                    }
                }
            }
        }
    }
}