package net.numalab.mojii.listener

import net.numalab.mojii.Mojii
import net.numalab.mojii.map.toMojiiMap
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class PickupListener(val plugin: Mojii) : Listener {
    @EventHandler
    fun onPickUp(e: EntityPickupItemEvent) {
        if (e.entity is Player) {
            // redraw Map
            e.item.itemStack.toMojiiMap()?.redraw()
        }
    }
}