package net.numalab.mojii.listener

import com.github.bun133.bukkitfly.component.toAWTColor
import com.github.bun133.bukkitfly.particle.BoxParticle
import com.github.bun133.bukkitfly.score.getColorSafe
import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.bukkitfly.util.Box
import com.github.bun133.tinked.RepeatTask
import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.Task
import com.github.bun133.tinked.WaitTask
import com.google.gson.Gson
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.numalab.mojii.Mojii
import net.numalab.mojii.api.WikiMediaCache
import net.numalab.mojii.api.WikiMediaSummaryRequest
import net.numalab.mojii.api.WikiMediaSummaryResponse
import net.numalab.mojii.game.MojiiGame
import net.numalab.mojii.judge.StringGetter
import net.numalab.mojii.lang.Lang
import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.map.toMojiiMap
import org.bukkit.*
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Team

class RightClickListener(val plugin: Mojii) : Listener {
    @EventHandler
    fun onRightClickEntity(e: PlayerInteractEntityEvent) {
        val en = e.rightClicked
        val team = Bukkit.getServer().scoreboardManager.mainScoreboard.teams.find { it.entries.contains(e.player.name) }
        if (en is ItemFrame && team != null) {
            onRightClickItemFrame(e, en, team)
        }
    }

    /**
     * 1tick待って、ItemFrameの中身がMojiiMapになっているか確認し、onRightClickMojiiを呼ぶ
     */
    private val checkTask = WaitTask<Triple<PlayerInteractEntityEvent, ItemFrame, Team>>(1, plugin)
        .apply {
            then(RunnableTask {
                val item = it.second.item
                val mojii = item.toMojiiMap()
                if (mojii != null) {
                    onRightClickMojii(it.first, it.second, it.third)
                }
            })
        }

    fun onRightClickItemFrame(e: PlayerInteractEntityEvent, itemFrame: ItemFrame, team: Team) {
        val item = itemFrame.item
        if (item.type.isEmpty) {
            // 新しくアイテムを設定するかも
            checkTask.run(Triple(e, itemFrame, team))
        } else {
            // アイテムを回転させる
            val mojiiMap = item.toMojiiMap()
            if (mojiiMap != null) {
                // 今すでにあるMojiiMapの回転をキャンセル
                e.isCancelled = true
            }
        }
    }

    /**
     * ItemFrameにMojiiMapが入れられた
     */
    fun onRightClickMojii(e: PlayerInteractEntityEvent, frame: ItemFrame, team: Team) {
        frame.rotation = Rotation.NONE // 回転を元に戻す
        updateFromMojiiMap(frame.location.block.location, team, e, frame)
    }

    /**
     * 指定された座標中心に単語ができていないか確認する
     */
    private fun updateFromMojiiMap(loc: Location, team: Team, e: PlayerInteractEntityEvent, frame: ItemFrame) {
        val g = plugin.currentGame
        if (g != null) {
            if (g.getTurnTeam() == team) {
                // 単語の成立判定をする
                val s = StringGetter.getFrom(loc)
                val task = getSummaryAll(g.setting.lang, exchars = plugin.config.exChars.value(), *s.toTypedArray())
                    .apply(filterSummary())
                    .apply(displayAll(team.getColorSafe().toAWTColor(), team, g))
                    .apply(RunnableTask {
                        if (it) {
                            // イベントをキャンセル
                            e.player.sendMessage(text("単語が成立しませんでした", NamedTextColor.GREEN))
                            e.isCancelled = true
                            // 強制的にアイテムを取り出す
                            val st = frame.item
                            e.player.inventory.addOrDrop(st)
                            frame.setItem(null)
                        } else {
                            // 次のターンへ
                            val winner = g.checkWinner(plugin)
                            if (winner != null) {
                                // 終了処理
                                plugin.currentGame = null
                            } else {
                                g.nextTurn()
                            }
                        }
                    })
                task.run(Unit)
            } else {
                // 今ターンのチームではない
                e.isCancelled = true
                e.player.sendMessage(text("${team.name}のターンではありません", NamedTextColor.RED))
            }
        } else {
            // ゲームが開始していない
        }
    }

    /**
     * KeyWordからWikiMediaSummaryを取得する
     */
    private fun getSummaryAll(
        lang: Lang,
        exchars: Int,
        vararg keyWord: Pair<String, List<Pair<MojiiMap, ItemFrame>>>
    ): Task<Unit, List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>> {
        return Task.all(
            *(keyWord.map {
                WikiMediaCache.instance.summary(
                    WikiMediaSummaryRequest(it.first, lang, exchars)
                )
                    .apply(RunnableTask<WikiMediaSummaryResponse, Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>> { s ->
                        s to it.second
                    })
            }).toTypedArray()
        )
    }

    /**
     * 2文字以上で存在するWikiMediaSummaryを取得するタスク
     */
    private fun filterSummary(): RunnableTask<List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>, List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>> {
        val gson = Gson()
        return RunnableTask<List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>, List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>> {
            return@RunnableTask it.filter { s ->
                s.first.query.isExist(gson) && (s.first.query.getTitle(gson)?.length ?: 0) >= 2
            }
        }
    }

    /**
     * 指定されたFrameをコンフィグに基づいて指定時間背景の色を変える+エフェクトを出す
     */
    private fun effect(
        backGroundColor: java.awt.Color,
        frame: Pair<MojiiMap, ItemFrame>
    ): Task<Unit, Unit> {
        val boxParticle = BoxParticle(
            Box.of(frame.second.boundingBox),
            plugin.config.particleType.value(),
            0.1
        )

        return RepeatTask(plugin.config.effectTick.value().toLong(), plugin) { u: Unit, now: Long ->
            boxParticle.playEffect(frame.second.location.world)
        }
    }

    /**
     * 指定されたフレームとSummaryに基づいて、指定された時間で背景色を変える+Summaryを出す
     */
    private fun display(
        backGroundColor: java.awt.Color,
        team: Team,
        g: MojiiGame
    ): RunnableTask<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>, Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>> {
        return RunnableTask<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>, Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>> {
            it.second.forEach { p ->
                // すべてにエフェクトをかける
                effect(backGroundColor, p).run(Unit)
                // すべてをチームの物にする
                p.first.ownedTeam = team
                // すべてをゲームデータと結びつける
                g.registerMap(p.first)
            }

            // BroadCast Summary
            // TODO タイトル以外も出す
            Bukkit.broadcast(text(it.first.query.getTitle(Gson())!!, NamedTextColor.AQUA))

            return@RunnableTask it
        }
    }

    /**
     * すべての結果に対して、それぞれ時間をあけながらdisplayを実行する
     */
    private fun displayAll(
        backGroundColor: java.awt.Color,
        team: Team,
        g: MojiiGame
    ): Task<List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>, Boolean> {
        var r = false
        return RunnableTask<List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>, List<Pair<WikiMediaSummaryResponse, List<Pair<MojiiMap, ItemFrame>>>>> {
            r = it.isEmpty()
            return@RunnableTask it
        }.apply(Task.forEach {
            display(backGroundColor, team, g).apply(WaitTask(plugin.config.effectInterval.value().toLong(), plugin))
        }).apply(RunnableTask { r })
    }
}