package net.numalab.mojii.listener

import com.github.bun133.bukkitfly.component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.numalab.mojii.Mojii
import net.numalab.mojii.MojiiConfig
import net.numalab.mojii.map.toMojiiMap
import org.bukkit.GameMode
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class LeftClickListener(val plugin: Mojii) : Listener {
    @EventHandler
    fun onLeftClick(event: EntityDamageByEntityEvent) {
        val itemFrame = event.entity as? ItemFrame
        val player = event.damager as? Player

        if (itemFrame != null && player != null && player.gameMode == plugin.config.targetGameMode.value()) {
            // MojiiMapを入れたItemFrameからMojiiMapを取り出せないようにする
            val item = itemFrame.item
            val mojiiMap = item.toMojiiMap()
            if (mojiiMap != null) {
                // MojiiMap取り出しだーっ！！！
                player.sendMessage(text("もじぴったんピースは取り出せません!", NamedTextColor.RED))
                event.isCancelled = true
            }
        }
    }
}