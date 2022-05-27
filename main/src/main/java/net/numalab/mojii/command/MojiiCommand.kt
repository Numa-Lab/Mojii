package net.numalab.mojii.command

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.tinked.EventTask
import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.WaitEventTask
import dev.kotx.flylib.command.Command
import net.numalab.mojii.judge.StringGetter
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class MojiiCommand : Command("mojii") {
    companion object {
        private fun mapGetTask(p: Player, plugin: JavaPlugin): EventTask<Unit, PlayerInteractEntityEvent> {
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
                            strings.forEach { s ->
                                p.sendMessage(s)
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
            MojiiLangItemCommand()
        )
        usage {
            selectionArgument("Task", "MojiiMapGet")
            executes {
                when (val t = typedArgs[0] as String) {
                    "MojiiMapGet" -> {
                        val p = player
                        if (p != null) {
                            val task = mapGetTask(p, plugin)
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