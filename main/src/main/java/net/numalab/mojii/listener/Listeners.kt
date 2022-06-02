package net.numalab.mojii.listener

import net.numalab.mojii.Mojii
import org.bukkit.event.Listener

class Listeners(val plugin: Mojii) {
    private val listeners = mutableListOf<Listener>()

    init {
        register(PickupListener(plugin))
        register(RightClickListener(plugin))
        register(LeftClickListener(plugin))
    }

    private fun register(listener: Listener) {
        listeners.add(listener)
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }
}