package net.numalab.mojii.listener

import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.WaitTask
import net.numalab.mojii.Mojii
import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.map.toMojiiMap
import org.bukkit.Location
import org.bukkit.Rotation
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.scoreboard.Team

class RightClickListener(val plugin: Mojii) : Listener {
    @EventHandler
    fun onRightClickEntity(e: PlayerInteractEntityEvent) {
        val en = e.rightClicked
        val team = plugin.config.team().find { it.entries.contains(e.player.name) }
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
        updateFromMojiiMap(frame.location.block.location, team)
    }

    /**
     * 指定された座標中心に単語ができていないか確認する
     */
    private fun updateFromMojiiMap(loc: Location, team: Team) {

    }
}